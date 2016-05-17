package sd.tp1.client.cloud.soap;

import sd.tp1.common.LogicClockMetadata;
import sd.tp1.common.Picture;
import sd.tp1.client.cloud.soap.stubs.SharedPicture;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    @Override
    public void updateVersion(String authorId) {
        this.version ++;
        this.authorId = authorId;
    }

    @Override
    public int compareTo(sd.tp1.common.Metadata o) {
        if(o instanceof LogicClockMetadata){
            LogicClockMetadata x = (LogicClockMetadata) o;
            if(this.version != x.getVersion())
                return this.version - x.getVersion();

            return this.authorId.compareTo(x.getAuthorId());
        }

        throw new NotImplementedException();
    }
}
