package sd.tp1.server.replication.server;

import sd.tp1.server.DataManager;
import sd.tp1.server.replication.metadata.Metadata;
import sd.tp1.server.replication.metadata.ServerMetadata;

import java.util.Collection;

/**
 * Created by apontes on 5/13/16.
 */
public interface ReplicableServer {
    void startReplication();

    ServerMetadata getServerMetadata();
    Collection<Metadata> getMetadataCollection();
}
