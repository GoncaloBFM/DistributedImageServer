package sd.tp1.server.replication.metadata;

import java.io.File;

/**
 * Created by apontes on 5/11/16.
 */
public interface PersistenceMetadata extends Metadata {
    void store();
    void load();
}
