package sd.tp1.server.replication.backdoor.client;

import sd.tp1.server.replication.backdoor.client.stubs.ServerMetadata;

import java.io.Serializable;

/**
 * Created by apontes on 5/16/16.
 */
public class ServerMetadataWrapper extends ServerMetadata implements Serializable {

    public ServerMetadataWrapper(sd.tp1.server.replication.metadata.ServerMetadata meta){
        this.serverId = meta.getServerId();
    }

    public ServerMetadataWrapper(ServerMetadata meta){
        this.serverId = meta.getServerId();
    }

    sd.tp1.server.replication.metadata.ServerMetadata getServerMetada(){
        return new sd.tp1.server.replication.metadata.ServerMetadata(this.serverId);
    }
}
