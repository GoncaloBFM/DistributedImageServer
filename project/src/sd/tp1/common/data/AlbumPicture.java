package sd.tp1.common.data;

/**
 * Created by apontes on 5/18/16.
 */
public interface AlbumPicture {
    Album getAlbum();
    Picture getPicture();

    default boolean equals(AlbumPicture albumPicture){
        return this.getAlbum().getName().equals(albumPicture.getAlbum().getName())
                && this.getPicture().getPictureName().equals(albumPicture.getPicture().getPictureName());
    }
}
