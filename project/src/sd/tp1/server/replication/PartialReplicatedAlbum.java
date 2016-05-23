package sd.tp1.server.replication;

import sd.tp1.client.cloud.HashServerManager;
import sd.tp1.common.data.SharedAlbum;
import sd.tp1.common.protocol.Endpoint;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by apontes on 5/23/16.
 */
public class PartialReplicatedAlbum extends SharedAlbum implements PartialReplicated{

    public static final int TARGET_REPLICS = 3;

    private HashReplicated sources;

    public PartialReplicatedAlbum(SharedAlbum sharedAlbum, String localId) {
        this(sharedAlbum, localId, TARGET_REPLICS);
    }


    public PartialReplicatedAlbum (SharedAlbum sharedAlbum, String localId, int targetReplics){
        this.setName(sharedAlbum.getName());

        this.setAuthorId(sharedAlbum.getAuthorId());
        this.setVersion(sharedAlbum.getVersion());
        this.setDeleted(sharedAlbum.isDeleted());

        this.sources = new HashReplicated(localId, targetReplics);
    }

    @Override
    public int canDispose() {
        return sources.canDispose();
    }

    @Override
    public Collection<String> findReplicationTargets(Collection<String> available) {
        return sources.findReplicationTargets(available);
    }

    @Override
    public int needReplicate() {
        return sources.needReplicate();
    }

    @Override
    public void remSouce(String remoteId) {
        sources.remSouce(remoteId);
    }

    @Override
    public void addSource(String remoteId, Endpoint remote) {
        sources.addSource(remoteId, remote);
    }
}
