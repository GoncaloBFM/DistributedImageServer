package sd.tp1.server.replication.metadata;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;

/**
 * Created by apontes on 5/15/16.
 */
public interface MetadataManager {
    Metadata getMetadata(Album album);
    Metadata getMetadata(Album album, Picture picture);

    void setMetadata(Album album, Metadata metadata);
    void setMetadata(Album album, Picture picture, Metadata metadata);

    ServerMetadata getServerMetadata();
}
