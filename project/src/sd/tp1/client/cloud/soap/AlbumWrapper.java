package sd.tp1.client.cloud.soap;

import sd.tp1.client.cloud.soap.stubs.Metadata;
import sd.tp1.common.Album;
import sd.tp1.client.cloud.soap.stubs.SharedAlbum;
import sd.tp1.common.LogicClockMetadata;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by apontes on 3/25/16.
 */
class AlbumWrapper extends SharedAlbum implements Album {
    AlbumWrapper(SharedAlbum album){
        super();
        super.name = album.getName();
    }

    AlbumWrapper(Album album){
        super();
        super.name = album.getName();
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
