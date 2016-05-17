package sd.tp1.common.rest_envelops;

import sd.tp1.client.cloud.soap.stubs.DeletePicture;
import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;

/**
 * Created by apontes on 5/17/16.
 */
public class DeletePictureEnvelop {
    public SharedAlbum album;
    public SharedPicture picture;

    public DeletePictureEnvelop(){
    }

    public DeletePictureEnvelop(Album album, Picture picture) {
        this.album = new SharedAlbum(album);
        this.picture = new SharedPicture(picture);
    }

    public DeletePictureEnvelop(SharedAlbum album, SharedPicture picture){
        this.album = album;
        this.picture = picture;
    }
}
