package sd.tp1.common.protocol.soap.client;

import sd.tp1.common.data.Album;
import sd.tp1.common.data.Metadata;
import sd.tp1.common.protocol.soap.client.stubs.SharedAlbum;
import sd.tp1.common.data.LogicClockMetadata;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by apontes on 3/25/16.
 */
class SoapAlbumWrapper extends SharedAlbum implements Album {
    SoapAlbumWrapper(SharedAlbum album){
        super();
        super.name = album.getName();
        super.authorId = album.getAuthorId();
        super.deleted = album.isDeleted();
        super.version = album.getVersion();
    }

    SoapAlbumWrapper(Album album){
        super();
        super.name = album.getName();
        super.authorId = album.getAuthorId();
        super.deleted = album.isDeleted();
        super.version = album.getVersion();
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
