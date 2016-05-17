package sd.tp1.common.rest_envelops;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;

/**
 * Created by apontes on 5/17/16.
 */
public class DeletePictureEnvelop {
    public Album album;
    public Picture picture;

    public DeletePictureEnvelop(Album album, Picture picture) {
        this.album = album;
        this.picture = picture;
    }
}
