package sd.tp1.client.cloud.cache;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.Server;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by apontes on 4/5/16.
 */
public class CachedServer implements Server{

    Server server;

    Cached<List<Album>> listOfAlbums;
    Map<String, Cached<List<Picture>>> picturesMap = new ConcurrentHashMap<>();

    public CachedServer(Server server){
        this.server = server;
    }

    @Override
    public URL getUrl() {
        return server.getUrl();
    }

    @Override
    public List<Album> getListOfAlbums() {
        if(this.listOfAlbums == null || this.listOfAlbums.isDirty()){
            List<Album> fetchedAlbumList = this.server.getListOfAlbums();

            if(this.listOfAlbums == null)
                this.listOfAlbums = new CachedObject<>(fetchedAlbumList);
            else
                this.listOfAlbums.recache(fetchedAlbumList);
        }
        return this.listOfAlbums.get();
    }

    @Override
    public List<Picture> getListOfPictures(Album album) {
        Cached<List<Picture>> cachedPictureList = this.picturesMap.get(album);
        if(cachedPictureList == null || cachedPictureList.isDirty()){
            List<Picture> fetchedPictureList = this.server.getListOfPictures(album);

            if(cachedPictureList == null){
                cachedPictureList = new CachedObject<List<Picture>>(fetchedPictureList);
                this.picturesMap.put(album.getName(), cachedPictureList);
            }
            else
                cachedPictureList.recache(fetchedPictureList);

        }

        return cachedPictureList.get();
    }

    @Override
    public byte[] getPictureData(Album album, Picture picture) {
        return server.getPictureData(album, picture);
    }

    @Override
    public Album createAlbum(String name) {
        Album album = this.server.createAlbum(name);
        if(album != null)
            this.listOfAlbums.makeDirty();

        return album;
    }

    @Override
    public Picture uploadPicture(Album album, String name, byte[] data) {
        Picture picture = this.server.uploadPicture(album, name, data);

        if(picture != null)
            this.picturesMap.get(album.getName()).makeDirty();

        return picture;
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
}
