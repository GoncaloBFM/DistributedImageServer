package sd.tp1.server.replication.metadata;

import java.io.Serializable;

/**
 * Created by apontes on 5/13/16.
 */
public class ServerMetadata implements Serializable {
    private String serverId;

    public ServerMetadata(){}

    public ServerMetadata(String serverId){
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) { this.serverId = serverId; }

    @Override
    public int hashCode() {
        return serverId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ServerMetadata)
            return this.serverId.equals(((ServerMetadata) obj).serverId);

        return false;
    }
}
