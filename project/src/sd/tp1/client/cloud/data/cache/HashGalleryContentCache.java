package sd.tp1.client.cloud.data.cache;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.HashServerManager;
import sd.tp1.client.cloud.Server;
import sd.tp1.client.cloud.data.CloudAlbum;
import sd.tp1.client.cloud.data.CloudPicture;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by apontes on 3/27/16.
 */
//TODO concurrent implement
public class HashGalleryContentCache implements GalleryContentCache{

    private static long CACHE_TIMEOUT = 5000L;

    private Map<String, CloudAlbum> albumMap = new ConcurrentHashMap<>();
    private Map<String, CloudPicture> pictureMap = new ConcurrentHashMap<>();

    private Map<Server, List<CloudAlbum>> serverAlbumMap = new ConcurrentHashMap<>();
    private Map<Server, List<CloudPicture>> serverPictureMap = new ConcurrentHashMap<>();

    private Collection<ContentChangeHandler> contentChangeHandlers = new ConcurrentLinkedQueue<>();

    private Map<URL, Long> lastAlbumListFetch = new ConcurrentHashMap<>();
    private Map<String, Long> lastAlbumContentFecth = new ConcurrentHashMap<>();
    private Map<String, Long> lastPictureFetch = new ConcurrentHashMap<>();

    private Collection<Server> serverCollection = new ConcurrentLinkedQueue<>();

    private String hashCode(Album album){
        return album.getName();
    }

    private String hashCode(Album album, Picture picture){
        return String.format("%s // %s", hashCode(album), picture.getName());
    }

    @Override
    public void serverDown(Server server) {
        List<CloudAlbum> cloudAlbums = serverAlbumMap.remove(server);
        List<CloudPicture> cloudPictures = serverPictureMap.remove(server);


        Set<CloudAlbum> modifiedAlbums = new HashSet<>(cloudAlbums.size());

        //Remove server from Albums and store unavailable albums
        if(cloudAlbums != null)
            for(CloudAlbum album : cloudAlbums) {
                album.remServer(server);
                if(album.getServers().size() == 0)
                    modifiedAlbums.add(
                            albumMap.remove(this.hashCode(album)));
            }

        //Remove server from Picture and store modified albums
        if(cloudPictures != null)
            for(CloudPicture picture : cloudPictures) {
                picture.remServer(server);
                if(picture.getServers().size() == 0)
                    modifiedAlbums.add(
                            pictureMap.remove(this.hashCode(picture.getAlbum(), picture)).getAlbum());
            }

        modifiedAlbums.forEach(x -> this.notifyContentChange(x));
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
        fetchAlbumList(server).forEach(x -> notifyContentChange(x));
    }

    private List<CloudAlbum> fetchAlbumList(Server server){
        List<Album> fetchedAlbums = server.getListOfAlbums();
        List<CloudAlbum> cloudAlbumList = new LinkedList<>();

        if(fetchedAlbums == null)
            return Collections.EMPTY_LIST;

        for(Album album : fetchedAlbums){
            CloudAlbum cloudAlbum = this.albumMap.get(hashCode(album));

            if(cloudAlbum == null){
                cloudAlbum = new CloudAlbum(album.getName());
                this.albumMap.put(album.getName(), cloudAlbum);
            }

            cloudAlbumList.add(cloudAlbum);
        }

        this.serverAlbumMap.put(server, Collections.unmodifiableList(cloudAlbumList));
        this.lastAlbumListFetch.put(server.getUrl(), System.currentTimeMillis());

        return cloudAlbumList;
    }

    private boolean isAlbumListUpdated(Server server){
        //TODO verify
        return this.lastAlbumListFetch.get(server.getUrl()) - System.currentTimeMillis() >= -CACHE_TIMEOUT;
    }

    @Override
    public void addContentChangeHandler(ContentChangeHandler handler) {
        this.contentChangeHandlers.add(handler);
    }

    @Override
    public List<Album> getListOfAlbums() {
        List<Album> albumList = new LinkedList<>();

        this.serverCollection.forEach(server -> {
            List<CloudAlbum> serverAlbums = isAlbumListUpdated(server) ?
                    this.serverAlbumMap.get(server) : fetchAlbumList(server);

            serverAlbums.forEach(albumList::add);
        });

        return albumList;
    }

    @Override
    public List<Picture> getListOfPictures(Album album) {
        return null;
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
