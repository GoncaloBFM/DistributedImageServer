package sd.tp1.client.cloud.soap;

import sd.tp1.Picture;
import sd.tp1.client.cloud.soap.stubs.SharedPicture;

/**
 * Created by apontes on 3/25/16.
 */
class PictureWrapper extends SharedPicture implements Picture {
    PictureWrapper(SharedPicture picture){
        super.pictureName = picture.getPictureName();
    }

    public PictureWrapper(Picture picture) {
        super.pictureName = picture.getPictureName();
    }
}
