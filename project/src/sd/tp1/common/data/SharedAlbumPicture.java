package sd.tp1.common.data;

/**
 * Created by apontes on 5/18/16.
 */
public class SharedAlbumPicture implements AlbumPicture {
    private SharedAlbum album;
    private SharedPicture picture;

    public SharedAlbumPicture(Album album, Picture picture){
        this.album = new SharedAlbum(album);
        this.picture = new SharedPicture(picture);
    }

    public SharedAlbumPicture(SharedAlbum album, SharedPicture picture){
        this.album = album;
        this.picture = picture;
    }

    public SharedAlbum getAlbum() {
        return album;
    }

    public SharedPicture getPicture() {
        return picture;
    }

    public void setAlbum(SharedAlbum album) {
        this.album = album;
    }

    public void setPicture(SharedPicture picture) {
        this.picture = picture;
    }
}