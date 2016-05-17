package sd.tp1.common.protocol.rest.envelops;

import sd.tp1.common.data.Album;
import sd.tp1.common.data.Picture;
import sd.tp1.common.data.SharedAlbum;
import sd.tp1.common.data.SharedPicture;

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
