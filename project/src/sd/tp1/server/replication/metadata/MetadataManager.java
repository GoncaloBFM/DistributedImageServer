package sd.tp1.server.replication.metadata;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;

/**
 * Created by apontes on 5/15/16.
 */
public interface MetadataManager {
    Metadata getMetadata(SharedAlbum album);
    Metadata getMetadata(SharedAlbum album, SharedPicture picture);

    void setMetadata(SharedAlbum album, Metadata metadata);
    void setMetadata(SharedAlbum album, SharedPicture picture, Metadata metadata);

    ServerMetadata getServerMetadata();
}
