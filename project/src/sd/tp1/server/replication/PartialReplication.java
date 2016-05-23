package sd.tp1.server.replication;

import sd.tp1.common.data.*;
import sd.tp1.common.protocol.Endpoint;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by apontes on 5/23/16.
 */
public class PartialReplication extends TotalReplication {

    public static final int MILLIS_SLEEP = 5000;
    public static final int NR_REPLIC = 3;

    private AlbumPictureRegistry registry;
    private Map<URL, String> idMap = new ConcurrentHashMap<>();

    private Map<URL, Endpoint> activeEndpoints = new ConcurrentHashMap<>();

    public PartialReplication(DataManager local) {
        super(local);
        registry = new AlbumPictureRegistry(local.getServerId(), NR_REPLIC);
    }

    private Queue<Endpoint> active = new ConcurrentLinkedQueue<>();
    private Endpoint getRandomEndpoint(){
        while(true){
            if(active.isEmpty()){
                Collection<Endpoint> list = activeEndpoints.values();
                if(!list.isEmpty())
                    active.addAll(list);
                else
                    try{ Thread.sleep(MILLIS_SLEEP); }
                    catch (InterruptedException e){}
            }

            Endpoint remote = active.poll();
            if(remote != null && remote.getServerId() != null)
                return remote;

        }
    }

    @Override
    protected void onServerAdd(URL url, Endpoint remote) {
        super.onServerAdd(url, remote);
        activeEndpoints.put(url, remote);

        String remoteId = remote.getServerId();
        if(remoteId == null)
            return;

        idMap.put(url, remoteId);

        MetadataBundle meta = remote.getMetadata();
        if(meta.getAlbumList() != null && meta.getPictureList() != null)
            registry.put(remoteId, meta.getAlbumList(), meta.getPictureList());
    }

    @Override
    protected void onServerDel(URL url) {
        super.onServerDel(url);
        activeEndpoints.remove(url);

        AlbumPictureRegistry.Bundle toReplicate = registry.remove(idMap.remove(url));
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
        while(count > 0){
            Endpoint remote = getRandomEndpoint();
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
        while(count > 0){
            Endpoint remote = getRandomEndpoint();
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

    private void filterOnlyLocals(List<SharedAlbum> albumList, List<SharedAlbumPicture> pictureList){
        albumList
                .parallelStream()
                .filter(a -> local.getMetadata(a.getName()) != null);

        pictureList
                .parallelStream()
                .filter(p -> local.getMetadata(p.getAlbum().getName(), p.getPicture().getPictureName()) != null);
    }

    @Override
    protected void handleMetadata(URL url, Endpoint remote, List<SharedAlbum> albumList, List<SharedAlbumPicture> pictureList) {
        filterOnlyLocals(albumList, pictureList);
        AlbumPictureRegistry.Bundle toDispose = registry.put(remote.getServerId(), albumList, pictureList);
        toDispose.album.forEach((a, c) -> local.dispose(a.getName()));
        toDispose.pictures.forEach((p, c) -> local.dispose(p.getAlbum().getName(), p.getPicture().getPictureName()));

        super.handleMetadata(url, remote, albumList, pictureList);
    }
}

class AlbumPictureRegistry {

    private String localId;
    private int nrReplics;

    AlbumPictureRegistry(String localId, int nrReplics){
        this.localId = localId;
        this.nrReplics = nrReplics;
    }

    private Map<String, Set<String>> albumSourcesMap = new ConcurrentHashMap<>();
    private Map<String, Set<String>> pictureSourcesMap = new ConcurrentHashMap<>();

    private Map<String, List<SharedAlbum>> sourceAlbumMap = new ConcurrentHashMap<>();
    private Map<String, List<SharedAlbumPicture>> sourcePictureMap = new ConcurrentHashMap<>();

    private Lock lock = new ReentrantLock(false);

    private String hash(Album album){
        return album.getName();
    }

    private String hash(AlbumPicture picture){
        return String.format("%s/%S", picture.getAlbum().getName(), picture.getPicture().getPictureName());
    }

    public Bundle remove(String remoteId){
        lock.lock();

        try{
            List<SharedAlbum> albumList = sourceAlbumMap.remove(remoteId);
            List<SharedAlbumPicture> pictureList = sourcePictureMap.remove(remoteId);

            Map<SharedAlbum, Integer> albumToReplicate = new HashMap<>();
            Map<SharedAlbumPicture, Integer> pictureToReplicate = new HashMap<>();

            albumList.forEach(a -> {
                Set<String> sources = albumSourcesMap.get(hash(a));
                int needReplicate = 0;
                if(sources != null
                        && (needReplicate = nrReplics - sources.size()) > 0
                        && localId.equals(sources.parallelStream().max(Comparator.naturalOrder()).orElse(null))){

                    albumToReplicate.put(a, needReplicate);
                }
            });

            pictureList.forEach(p -> {
                Set<String> sources = pictureSourcesMap.get(hash(p));
                int needReplicate = 0;
                if(sources != null
                        && (needReplicate = nrReplics - sources.size()) > 0
                        && localId.equals(sources.parallelStream().max(Comparator.naturalOrder()).orElse(null))){

                    pictureToReplicate.put(p, needReplicate);
                }
            });

            Bundle toReplicate = new Bundle();
            toReplicate.album = albumToReplicate;
            toReplicate.pictures = pictureToReplicate;
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
            albumList.forEach(a -> {
                Set<String> sources = albumSourcesMap.get(hash(a));
                if(sources == null){
                    sources = new LinkedHashSet<>();
                    albumSourcesMap.put(hash(a), sources);
                }
                sources.add(remoteId);

                int dispose = 0;
                if((dispose = sources.size() - nrReplics) > 0
                        && localId.equals(sources.parallelStream().min(Comparator.naturalOrder()).orElse(null)))
                    toDispose.album.put(a, dispose);
            });

            pictureList.forEach(p -> {
                Set<String> sources = pictureSourcesMap.get(hash(p));
                if(sources == null){
                    sources = new LinkedHashSet<>();
                    pictureSourcesMap.put(hash(p), sources);
                }

                sources.add(remoteId);


                int dispose = 0;
                if((dispose = sources.size() - nrReplics) > 0
                        && localId.equals(sources.parallelStream().min(Comparator.naturalOrder()).orElse(null)))
                    toDispose.pictures.put(p, dispose);
            });

            sourceAlbumMap.put(remoteId, albumList);
            sourcePictureMap.put(remoteId, pictureList);

            lock.unlock();
            return toDispose;
        }
        catch (Throwable e){
            lock.unlock();
            throw e;
        }
    }

    public class Bundle {
        public Map<SharedAlbum, Integer> album = new HashMap<>();
        public Map<SharedAlbumPicture, Integer> pictures = new HashMap<>();
    }
}
