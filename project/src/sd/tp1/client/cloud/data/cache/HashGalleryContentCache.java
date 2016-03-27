package sd.tp1.client.cloud.data.cache;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.Server;
import sd.tp1.client.cloud.data.CloudAlbum;
import sd.tp1.client.cloud.data.CloudPicture;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by apontes on 3/27/16.
 */
public class HashGalleryContentCache implements GalleryContentCache{

    private Map<String, CloudAlbum> albumMap = new HashMap<>();
    private Map<String, CloudPicture> pictureMap = new HashMap<>();

    private Map<Server, List<CloudAlbum>> serverAlbumMap = new HashMap<>();
    private Map<Server, List<CloudPicture>> serverPictureMap = new HashMap<>();

    private List<ContentChangeHandler> contentChangeHandlers = new LinkedList<>();

    private String hashCode(Album album){
        return album.getName();
    }

    private String hashCode(Album album, Picture picture){
        return hashCode(album) + picture.getName();
    }

    @Override
    public void notifyServerDown(Server server) {
        List<CloudAlbum> cloudAlbums = serverAlbumMap.remove(server);
        List<CloudPicture> cloudPictures = serverPictureMap.remove(server);
        boolean collectionChange = false;

        if(cloudAlbums != null)
            for(CloudAlbum album : cloudAlbums) {
                album.remServer(server);
                if(album.getServers().size() == 0) {
                    albumMap.remove(this.hashCode(album));
                    collectionChange = true;
                }
            }

        if(cloudPictures != null)
            for(CloudPicture picture : cloudPictures) {
                picture.remServer(server);
                if(picture.getServers().size() == 0) {
                    pictureMap.remove(this.hashCode(picture.getAlbum(), picture));
                    collectionChange = true;
                }
            }

        if(collectionChange)
            notifyContentChange();

    }

    private void notifyContentChange(){
        for(ContentChangeHandler handler : this.contentChangeHandlers)
            handler.contentChange();
    }


    //TODO implement
    @Override
    public void notifyServerUp(Server server) {

    }

    @Override
    public void notifyServerUpdate(Server server) {

    }

    @Override
    public void addContentChangeHandler(ContentChangeHandler handler) {
        this.contentChangeHandlers.add(handler);
    }

    @Override
    public List<Album> getListOfAlbums() {
        return null;
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
