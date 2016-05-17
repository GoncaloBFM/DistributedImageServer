package sd.tp1.common.rest_envelops;


import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;


/**
 * Created by apontes on 5/17/16.
 */
public class UploadPictureEnvelop {
    public SharedAlbum album;
    public SharedPicture picture;
    public byte[] data;

    public UploadPictureEnvelop(Album album, Picture picture, byte[] data) {
        this.album = new SharedAlbum(album);
        this.picture = new SharedPicture(picture);
        this.data = data;
    }

    public UploadPictureEnvelop(SharedAlbum album, SharedPicture picture, byte[] data){
        this.album = album;
        this.picture = picture;
        this.data = data;
    }
}
