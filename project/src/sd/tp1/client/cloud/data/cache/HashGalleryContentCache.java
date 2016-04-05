package sd.tp1.client.cloud.data.cache;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.Server;
import sd.tp1.client.cloud.data.CloudAlbum;
import sd.tp1.client.cloud.data.CloudPicture;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Created by apontes on 3/27/16.
 */
//TODO concurrent implement
public class HashGalleryContentCache implements GalleryContentCache{

    private static long CACHE_TIMEOUT = 5000L;
    private static int  RETRIES_ON_ERROR = 3;

    private Map<String, CachedAlbumList> albumListMap = new ConcurrentHashMap<>();
    private Map<String, CachedAlbum> albumMap = new ConcurrentHashMap<>();
    private Map<String, CachedPicture> pictureMap = new ConcurrentHashMap<>();

    private Map<String, List<CachedPicture>> serverPictureMap = new ConcurrentHashMap<>();

    private Collection<ContentChangeHandler> contentChangeHandlers = new ConcurrentLinkedQueue<>();
    private Collection<Server> serverCollection = new ConcurrentLinkedQueue<>();

    private String hashCode(Album album){
        return album.getName();
    }

    private String hashCode(Album album, Picture picture){
        return String.format("%s // %s", hashCode(album), picture.getName());
    }

    private String hashCode(Server server){
        return server.getUrl().toString();
    }

    private String hashCode(CloudPicture picture){
        return this.hashCode(picture.getAlbum(), picture);
    }

    @Override
    public void serverDown(Server server) {
        String hash = hashCode(server);
        List<CachedAlbum> cachedAlbumList = this.albumListMap.remove(hash).getAlbumList();
        List<CachedPicture> cachedPictureList = this.serverPictureMap.remove(hash);

        Set<CachedAlbum> modifiedAlbums = new HashSet<>(cachedAlbumList.size());

        //Remove server from Albums and store unavailable albums
        if(cachedAlbumList != null)
            for(CachedAlbum album : cachedAlbumList) {
                album.remServer(server);
                if(album.getServers().size() == 0)
                    modifiedAlbums.add(
                            this.albumMap.remove(this.hashCode(album)));
            }

        //Remove server from Picture and store modified albums
        if(cachedPictureList != null)
            for(CachedPicture picture : cachedPictureList) {
                picture.remServer(server);
                if(picture.getServers().size() == 0) {
                    CachedPicture cachedPicture = this.pictureMap.remove(this.hashCode(picture));
                    modifiedAlbums.add(cachedPicture.getAlbum());
                }
            }

        this.serverCollection.remove(server);
        modifiedAlbums.forEach(x -> {x.makeDirty(); this.notifyContentChange(x); });
    }

    //TODO improve notification system thread queuing
    private void notifyContentChange(){
        this.contentChangeHandlers.forEach(ContentChangeHandler::contentChange);
    }

    //TODO improve notification system thread queuing
    private void notifyContentChange(CloudAlbum album){
        this.contentChangeHandlers.forEach(x -> x.contentChange(album));
    }


    //TODO implement
    @Override
    public void serverUp(Server server) {
        this.serverCollection.add(server);
        this.fetchAlbumList(server).forEach(x -> notifyContentChange(x));
    }

    private List<CachedAlbum> fetchAlbumList(Server server){
        List<Album> fetchedAlbums = null;

        for(int i = 0; fetchedAlbums == null && i < RETRIES_ON_ERROR; i++)
            fetchedAlbums = server.getListOfAlbums();

        if(fetchedAlbums == null) {
            return Collections.EMPTY_LIST;
        }

        List<CachedAlbum> albumList = new LinkedList<>();

        for(Album album : fetchedAlbums){
            CachedAlbum cachedAlbum = this.albumMap.get(hashCode(album));

            if(cachedAlbum == null){
                cachedAlbum = new CachedAlbum(album.getName(), null);
                this.albumMap.put(album.getName(), cachedAlbum);
            }

            albumList.add(cachedAlbum);
        }

        albumList = Collections.unmodifiableList(albumList);
        this.albumListMap.put(hashCode(server), new CachedAlbumList(server, albumList));

        return albumList;
    }

    @Override
    public void addContentChangeHandler(ContentChangeHandler handler) {
        this.contentChangeHandlers.add(handler);
    }

    @Override
    public List<Album> getListOfAlbums() {
        List<Album> albumList = new LinkedList<>();

        this.serverCollection.forEach(server -> {
            CachedAlbumList cachedAlbumList = this.albumListMap.get(hashCode(server));
            List<? extends Album> serverAlbums = cachedAlbumList.isDirty() ?
                    fetchAlbumList(server) : cachedAlbumList.getAlbumList();

            serverAlbums.forEach(albumList::add);
        });

        return albumList;
    }

    private List<CachedPicture> fetchPictureList(Album album){
        CachedAlbum cachedAlbum = this.albumMap.get(hashCode(album));
        if(cachedAlbum == null)
            throw new RuntimeException(String.format("No references for album %s", album));

        for(Server server : cachedAlbum.getServers()){
            List<Picture> fetchedPictures = null;
            for(int i = 0; i < RETRIES_ON_ERROR && fetchedPictures == null; i++)
                fetchedPictures = server.getListOfPictures(album);

            //List<CachedPicture> serverPictures = this.serverPictureMap.get()
            for(Picture picture : fetchedPictures){
                CachedPicture cachedPicture = this.pictureMap.get(hashCode(album, picture));

                if(cachedPicture == null){
                    cachedPicture = new CachedPicture(picture.getName(), cachedAlbum, null);
                    this.pictureMap.put(hashCode(cachedPicture), cachedPicture);
                }

                cachedPicture.addServer(server);
            }
        }

        return null;
    }

    @Override
    public List<Picture> getListOfPictures(Album album) {
        CachedAlbum cachedAlbum = this.albumMap.get(hashCode(album));
        if(cachedAlbum == null || cachedAlbum.isDirty() || cachedAlbum.getPictures() == null){



        }

        return (List<Picture>) (List<?>) cachedAlbum.getPictures();
        //.stream().map(x -> x).collect(Collectors.toList());
    }

    @Override
    public byte[] getPictureData(Album album, Picture picture) {
        return new byte[0];
    }

    @Override
    public Album createAlbum(String name) {
        return null;
    }

    @Override
    public Picture uploadPicture(Album album, String name, byte[] data) {
        return null;
    }

    @Override
    public void deleteAlbum(Album album) {

    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        return false;
    }
}
