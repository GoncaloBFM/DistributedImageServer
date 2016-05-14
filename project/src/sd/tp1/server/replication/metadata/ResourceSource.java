package sd.tp1.server.replication.metadata;

/**
 * Created by apontes on 5/13/16.
 */
public class ResourceSource {
    private String serverId;

    public ResourceSource(String serverId){
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }

    @Override
    public int hashCode() {
        return serverId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  ResourceSource)
            return this.serverId.equals(((ResourceSource) obj).serverId);

        return false;
    }
}
