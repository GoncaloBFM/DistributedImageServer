package sd.tp1.client.cloud.cache;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.client.cloud.Server;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by apontes on 4/5/16.
 */
public class HashCachedServer implements CachedServer{

    Server server;

    Cached<List<Album>> listOfAlbums;
    Map<String, Cached<List<Picture>>> picturesMap = new ConcurrentHashMap<>();

    public HashCachedServer(Server server){
        this.server = server;
    }

    @Override
    public URL getUrl() {
        return server.getUrl();
    }

    @Override
    public String getServerId() {
        return server.getServerId();
    }

    @Override
    public List<Album> loadListOfAlbums() {
        if(this.listOfAlbums == null || this.listOfAlbums.isDirty()){
            List<Album> fetchedAlbumList = this.server.loadListOfAlbums();

            if(this.listOfAlbums == null)
                this.listOfAlbums = new CachedObject<>(fetchedAlbumList);
            else
                this.listOfAlbums.recache(fetchedAlbumList);
        }

        return this.listOfAlbums.get();
    }

    @Override
    public List<Picture> loadListOfPictures(String album) {
        Cached<List<Picture>> cachedPictureList = this.picturesMap.get(album);
        if(cachedPictureList == null || cachedPictureList.isDirty()){
            List<Picture> fetchedPictureList = this.server.loadListOfPictures(album);

            if(cachedPictureList == null){
                cachedPictureList = new CachedObject<>(fetchedPictureList);
                this.picturesMap.put(album, cachedPictureList);
            }
            else
                cachedPictureList.recache(fetchedPictureList);

        }

        return cachedPictureList.get();
    }

    @Override
    public byte[] loadPictureData(String album, String picture) {
        return server.loadPictureData(album, picture);
    }

    @Override
    public void createAlbum(Album album) {
        if(album != null) {
            this.server.createAlbum(album);
            this.listOfAlbums.makeDirty();
        }
    }

    @Override
    public void uploadPicture(Album album, Picture picture, byte[] data) {
        if(picture != null) {
            this.server.uploadPicture(album, picture, data);
            this.picturesMap.get(album.getName()).makeDirty();
        }
    }

    @Override
    public void deleteAlbum(Album album) {
        this.server.deleteAlbum(album);
        this.listOfAlbums.makeDirty();
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        boolean deleted = this.server.deletePicture(album, picture);

        if(deleted)
            this.picturesMap.get(album.getName()).makeDirty();

        return deleted;
    }

    @Override
    public List<Album> getCachedListOfAlbums() {
        return this.listOfAlbums.get();
    }

    @Override
    public List<Picture> getCachedListOfPictures(Album album) {
        return this.picturesMap.get(album.getName()).get();
    }
}
