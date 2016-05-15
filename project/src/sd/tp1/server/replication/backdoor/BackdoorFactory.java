package sd.tp1.server.replication.backdoor;

import sd.tp1.server.replication.metadata.ServerMetadata;

/**
 * Created by apontes on 5/15/16.
 */
public interface BackdoorFactory {
    ReblicationServerBackdoor getBackdoor(ServerMetadata serverMetadata);
}
