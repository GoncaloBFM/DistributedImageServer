package sd.tp1.server.replication.backdoor.client;

import sd.tp1.common.SharedPicture;

/**
 * Created by apontes on 5/16/16.
 */
public class PictureWrapper extends sd.tp1.server.replication.backdoor.client.stubs.SharedPicture {
    public PictureWrapper(SharedPicture picture) {
        super();

        this.pictureName = picture.getPictureName();
    }

    public SharedPicture getSharedPicture(){
        return new SharedPicture(this.pictureName);
    }
}
