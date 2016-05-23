package sd.tp1.common.protocol.soap.client;

import sd.tp1.common.data.LogicClockMetadata;
import sd.tp1.common.data.Metadata;
import sd.tp1.common.data.Picture;
import sd.tp1.common.protocol.soap.client.stubs.SharedPicture;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by everyone on 3/25/16.
 */
class SoapPictureWrapper extends SharedPicture implements Picture {
    SoapPictureWrapper(SharedPicture picture){
        super.pictureName = picture.getPictureName();
        super.authorId = picture.getAuthorId();
        super.deleted = picture.isDeleted();
        super.version = picture.getVersion();
    }

    public SoapPictureWrapper(Picture picture) {
        super.pictureName = picture.getPictureName();
        super.authorId = picture.getAuthorId();
        super.deleted = picture.isDeleted();
        super.version = picture.getVersion();
    }

    @Override
    public void updateVersion(String authorId) {
        this.version ++;
        this.authorId = authorId;
    }

    @Override
    public int compareTo(Metadata o) {
        if(o instanceof LogicClockMetadata){
            LogicClockMetadata x = (LogicClockMetadata) o;
            if(this.version != x.getVersion())
                return this.version - x.getVersion();

            return this.authorId.compareTo(x.getAuthorId());
        }

        throw new NotImplementedException();
    }
}
