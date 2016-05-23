package sd.tp1.common.data;

/**
 * Created by apontes on 5/18/16.
 */
public class SharedAlbumPicture implements AlbumPicture {
    private SharedAlbum album;
    private SharedPicture picture;

    public SharedAlbumPicture(){}

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

    @Override
    public int hashCode() {
        return String.format("%s/%s", album.getName(), picture.getPictureName()).hashCode();
    }

    @Override
    public boolean equals(AlbumPicture albumPicture) {
        return album.equals(albumPicture.getAlbum()) && picture.equals(albumPicture.getPicture());
    }
}
