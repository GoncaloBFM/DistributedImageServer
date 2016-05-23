package sd.tp1.client.cloud.cache;

import sd.tp1.common.data.Album;
import sd.tp1.common.data.AlbumPicture;
import sd.tp1.common.data.MetadataBundle;
import sd.tp1.common.data.Picture;
import sd.tp1.client.cloud.Server;
import sd.tp1.common.protocol.Endpoint;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by everyone on 4/5/16.
 */
public class HashCachedServer implements CachedServer{

    Endpoint server;

    Cached<List<Album>> listOfAlbums;
    Map<String, Cached<List<Picture>>> picturesMap = new ConcurrentHashMap<>();

    public HashCachedServer(Endpoint server){
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
    public boolean createAlbum(Album album) {
        if(album != null && this.server.createAlbum(album)) {
            this.listOfAlbums.makeDirty();
            return true;
        }

        return false;
    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        if(picture != null && this.server.uploadPicture(album, picture, data)) {
            this.picturesMap.get(album.getName()).makeDirty();
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteAlbum(Album album) {
        if(album != null && this.server.deleteAlbum(album)) {
            this.listOfAlbums.makeDirty();
            return true;
        }

        return false;
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        if(album != null && picture != null && this.server.deletePicture(album, picture)) {
            this.picturesMap.get(album.getName()).makeDirty();
            return true;
        }

        return false;
    }

    @Override
    public MetadataBundle getMetadata() {
        return server.getMetadata();
    }

    @Override
    public List<Album> getCachedListOfAlbums() {
        return this.listOfAlbums.get();
    }

    @Override
    public List<Picture> getCachedListOfPictures(Album album) {
        return this.picturesMap.get(album.getName()).get();
    }

    @Override
    public void notifyAlbumChange(Album album) {
        this.listOfAlbums.makeDirty();
        Cached cachedAlbum = this.picturesMap.get(album.getName());
        if(cachedAlbum != null)
            cachedAlbum.makeDirty();
    }

    @Override
    public void notifyPictureChange(AlbumPicture albumPicture) {
        Cached album = this.picturesMap.get(albumPicture.getAlbum());
        if(album == null) {
            listOfAlbums.makeDirty();
            return;
        }

        album.makeDirty();
    }
}
