package sd.tp1.server.replication.server.backdoor;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.server.replication.metadata.ServerMetadata;
import sd.tp1.server.replication.metadata.Metadata;

/**
 * Created by apontes on 5/15/16.
 */
public interface ReblicationServerBackdoor {
    void sendMetadata(Metadata[] metadata);
    byte[] getPictureData(SharedAlbum album, SharedAlbum picture);

    ServerMetadata getServerMetadata();
}
