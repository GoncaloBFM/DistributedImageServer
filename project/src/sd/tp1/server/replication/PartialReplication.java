package sd.tp1.server.replication;

import sd.tp1.common.data.*;
import sd.tp1.common.protocol.Endpoint;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * Created by apontes on 5/23/16.
 */
public class PartialReplication extends TotalReplication {

    public static final int MILLIS_SLEEP = 5000;
    public static final int NR_REPLIC = 3;

    private AlbumPictureRegistry registry;
    private Map<URL, String> idMap = new ConcurrentHashMap<>();

    private Map<String, Endpoint> activeEndpoints = new ConcurrentHashMap<>();

    public PartialReplication(DataManager local) {
        super(local);
        registry = new AlbumPictureRegistry(local.getServerId(), local);
        logger = Logger.getLogger(PartialReplication.class.getName());

    }

    @Override
    protected void onServerAdd(URL url, Endpoint remote) {
        super.onServerAdd(url, remote);

        String remoteId = remote.getServerId();
        if(remoteId == null)
            return;

        activeEndpoints.put(remoteId, remote);
        idMap.put(url, remoteId);

        MetadataBundle meta = remote.getMetadata();
        if(meta.getAlbumList() != null && meta.getPictureList() != null)
            registry.put(remoteId, meta.getAlbumList(), meta.getPictureList());
    }

    @Override
    protected void onServerDel(URL url) {
        super.onServerDel(url);
        activeEndpoints.remove(
                idMap.get(url));

        replicate(registry.remove(idMap.remove(url)));

    }

    private void replicate(AlbumPictureRegistry.Bundle toReplicate){
        if(toReplicate.album.size() == 0 && toReplicate.pictures.size() == 0)
            return;

        toReplicate.album.entrySet().forEach(e ->
                new Thread(){
                    @Override
                    public void run(){
                        replicate(e.getKey(), e.getValue());
                    }
                }.start());

        toReplicate.pictures.entrySet().forEach(e ->
                new Thread() {
                    @Override
                    public void run(){
                        replicate(e.getKey().getAlbum(), e.getKey().getPicture(), e.getValue());
                    }
                }.start());
    }

    private void replicate(Album album, int count){
        SharedAlbum sAlbum = new SharedAlbum(album);
        List<Endpoint> targets = registry.getPossibleTargets(sAlbum, idMap.values())
                .parallelStream()
                .map(id -> activeEndpoints.get(id))
                .collect(Collectors.toList());

        Iterator<Endpoint> iterator = targets.iterator();
        while(count > 0 && iterator.hasNext()){
            Endpoint remote = iterator.next();
            if(remote == null)
                continue;

            if(!album.isDeleted()){
                if(remote.createAlbum(album))
                    count--;
            }
            else{
                if(remote.deleteAlbum(album))
                    count--;
            }
        }
    }

    private void replicate(Album album, Picture picture, int count){
        SharedAlbumPicture albumPicture = new SharedAlbumPicture(album, picture);
        List<Endpoint> targets = registry.getPossibleTargets(albumPicture, idMap.values())
                .parallelStream()
                .map(id -> activeEndpoints.get(id))
                .collect(Collectors.toList());

        Iterator<Endpoint> iterator = targets.iterator();
        while(count > 0 && iterator.hasNext()){
            Endpoint remote = iterator.next();
            if(remote == null)
                return;

            if(!picture.isDeleted()){
                byte[] data = null;
                while(data == null)
                    data = local.loadPictureData(album.getName(), picture.getPictureName());

                if(remote.uploadPicture(album, picture, data))
                    count--;
            }
            else if(remote.deletePicture(album, picture))
                count--;
        }
    }

    @Override
    protected void handleMetadata(URL url, Endpoint remote, List<SharedAlbum> albumList, List<SharedAlbumPicture> pictureList) {
        albumList = albumList
                .parallelStream()
                .filter(a -> local.getMetadata(a.getName()) != null)
                .collect(Collectors.toList());

        pictureList = pictureList
                .parallelStream()
                .filter(p -> local.getMetadata(p.getAlbum().getName(), p.getPicture().getPictureName()) != null)
                .collect(Collectors.toList());

        AlbumPictureRegistry.Bundle toDispose = registry.put(remote.getServerId(), albumList, pictureList);
        toDispose.album.forEach((a, c) -> local.dispose(a.getName()));
        toDispose.pictures.forEach((p, c) -> local.dispose(p.getAlbum().getName(), p.getPicture().getPictureName()));

        albumList = albumList
                .parallelStream()
                .filter(a -> !toDispose.album.containsKey(a))
                .collect(Collectors.toList());

        pictureList  = pictureList
                .parallelStream()
                .filter(p -> !toDispose.pictures.containsKey(p))
                .collect(Collectors.toList());

        replicate(registry.needReplicate(albumList, pictureList));

        super.handleMetadata(url, remote, albumList, pictureList);
    }
}

class AlbumPictureRegistry {

    private String localId;
    private DataManager local;

    AlbumPictureRegistry(String localId, DataManager local){
        this.localId = localId;
        this.local = local;
    }

    private Map<SharedAlbum, PartialReplicatedAlbum> albumMap = new ConcurrentHashMap<>();
    private Map<SharedAlbumPicture, PartialReplicatedPicture> pictureMap = new ConcurrentHashMap<>();

    private Map<String, List<SharedAlbum>> albumHistory = new ConcurrentHashMap<>();
    private Map<String, List<SharedAlbumPicture>> pictureHistory = new ConcurrentHashMap<>();

    private Lock lock = new ReentrantLock(false);

    private SharedAlbum getLocalMetadata(SharedAlbum album){
        Album lAlbum = local.getMetadata(album.getName());
        if(lAlbum != null)
            return new SharedAlbum(lAlbum);

        return null;
    }

    private SharedAlbumPicture getLocalMetadata(SharedAlbumPicture picture){
        Album lAlbum = local.getMetadata(picture.getAlbum().getName());
        Picture lPicture = local.getMetadata(picture.getAlbum().getName(), picture.getPicture().getPictureName());
        if(lAlbum != null && lPicture != null)
            return new SharedAlbumPicture(lAlbum, lPicture);

        return null;
    }

    public Bundle needReplicate(List<SharedAlbum> albumList, List<SharedAlbumPicture> pictureList){
        lock.lock();

        try{
            MetadataBundle meta = local.getMetadata();
            put(localId, meta.getAlbumList(), meta.getPictureList());

            Set<SharedAlbum> remoteAlbum = new HashSet<>();
            Set<SharedAlbumPicture> remotePicture = new HashSet<>();

            albumList.forEach(a -> remoteAlbum.add(a));
            pictureList.forEach(p -> remotePicture.add(p));

            Bundle bundle = new Bundle();
            albumMap.values()
                    .parallelStream()
                    .filter(r -> r.needReplicate() > 0)
                    .filter(r -> !remoteAlbum.contains(r))
                    .forEach(r -> {
                        SharedAlbum album = getLocalMetadata(r);
                        if(album != null)
                            bundle.album.put(album, r.canDispose());
                    });

            pictureMap.values()
                    .parallelStream()
                    .filter(r -> r.needReplicate() > 0)
                    .filter(r -> !remotePicture.contains(r))
                    .forEach(r -> {
                        SharedAlbumPicture picture = getLocalMetadata(r);
                        if(picture != null)
                            bundle.pictures.put(picture, r.canDispose());
                    });

            lock.unlock();

            if(bundle.album.size() > 0 || bundle.pictures.size() > 0)
                return bundle;

            return bundle;
        }
        catch (Throwable e){
            lock.unlock();
            throw e;
        }
    }

    public Bundle remove(String remoteId){
        lock.lock();

        try{
            List<SharedAlbum> albumList = albumHistory.remove(remoteId);
            List<SharedAlbumPicture> pictureList = pictureHistory.remove(remoteId);

            Bundle toReplicate = new Bundle();

            albumList.parallelStream()
                    .forEach(a -> {
                        PartialReplicatedAlbum album = albumMap.get(a);
                        album.remSouce(remoteId);
                        int needReplicate = album.needReplicate();
                        if(needReplicate > 0)
                            toReplicate.album.put(getLocalMetadata(a), needReplicate);
                    });

            pictureList.parallelStream()
                    .forEach(p -> {
                        PartialReplicatedPicture picture = pictureMap.get(p);
                        picture.remSouce(remoteId);
                        int needReplicate = picture.needReplicate();
                        if(needReplicate > 0)
                            toReplicate.pictures.put(getLocalMetadata(p), needReplicate);
                    });

            lock.unlock();
            return toReplicate;
        }
        catch (Throwable e){
            lock.unlock();
            throw e;
        }
    }

    public Bundle put(String remoteId, List<SharedAlbum> albumList, List<SharedAlbumPicture> pictureList){
        if(remoteId == null || albumList == null || pictureList == null)
            return new Bundle();

        Bundle toDispose = new Bundle();

        lock.lock();
        try{
            List<SharedAlbum> oldAlbum = albumHistory.get(remoteId);
            if(oldAlbum != null)
            oldAlbum
                    .parallelStream()
                    .filter(a -> !albumList.contains(a))
                    .forEach(a -> {
                        PartialReplicatedAlbum album = albumMap.get(a);
                        if(album != null)
                            album.remSouce(remoteId);
                    });

            albumHistory.put(remoteId, albumList);

            List<SharedAlbumPicture> oldPicture = pictureHistory.get(remoteId);
            if(oldPicture != null)
            oldPicture
                    .parallelStream()
                    .filter(p -> !pictureList.contains(p))
                    .forEach(p -> {
                        PartialReplicatedPicture picture = pictureMap.get(p);
                        if(picture != null)
                            picture.remSouce(remoteId);
                    });

            pictureHistory.put(remoteId, pictureList);


            albumList.parallelStream()
                    .forEach(a -> {
                        PartialReplicatedAlbum album = albumMap.get(a);
                        if(album == null){
                            album = new PartialReplicatedAlbum(a, localId);
                            albumMap.put(a, album);
                        }

                        album.addSource(remoteId);

                        int canDispose = album.canDispose();
                        if(canDispose > 0)
                            toDispose.album.put(album, canDispose);
                    });


            pictureList.parallelStream()
                    .forEach(p -> {
                        PartialReplicatedPicture picture = pictureMap.get(p);
                        if(picture == null){
                            picture = new PartialReplicatedPicture(p, localId);
                            pictureMap.put(p, picture);
                        }

                        picture.addSource(remoteId);

                        int canDispose = picture.canDispose();
                        if(canDispose > 0)
                            toDispose.pictures.put(picture, canDispose);
                    });

            lock.unlock();
            return toDispose;
        }
        catch (Throwable e){
            lock.unlock();
            throw e;
        }
    }

    public Collection<String> getPossibleTargets(SharedAlbum album, Collection<String> available){
        lock.lock();
        try{
            Collection<String> targets = albumMap.get(album).findReplicationTargets(available);
            lock.unlock();
            return targets;
        }
        catch(Throwable t){
            lock.unlock();
            throw t;
        }
    }

    public Collection<String> getPossibleTargets(SharedAlbumPicture picture, Collection<String> available){
        lock.lock();
        try{
            Collection<String> targets = pictureMap.get(picture).findReplicationTargets(available);
            lock.unlock();
            return targets;
        }
        catch(Throwable t){
            lock.unlock();
            throw t;
        }
    }

    public class Bundle {
        public Map<SharedAlbum, Integer> album = new ConcurrentHashMap<>();
        public Map<SharedAlbumPicture, Integer> pictures = new ConcurrentHashMap<>();
    }
}
