package sd.tp1.server.replication.metadata.old;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;

/**
 * Created by apontes on 5/13/16.
 */
public interface MetadataManager {
    PersistenceMetadata buildMetadata(Album album);
    PersistenceMetadata buildMetadata(Album album, Picture picture);

    PersistenceMetadata buildAndStoreMetadata(Album album);
    PersistenceMetadata buildAndStoreMetadata(Album album, Picture picture);

    PersistenceMetadata getMetadata(Album album);
    PersistenceMetadata getMetadata(Album album, Picture picture);

    void storeMetadata(PersistenceMetadata metadata);

    boolean needUpdate(Metadata metadata);
}
