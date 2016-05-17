package sd.tp1.common.rest_envelops;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;


/**
 * Created by apontes on 5/17/16.
 */
public class UploadPictureEnvelop {
    public Album album;
    public Picture picture;
    public byte[] data;

    public UploadPictureEnvelop(Album album, Picture picture, byte[] data) {
        this.album = album;
        this.picture = picture;
        this.data = data;
    }
}
