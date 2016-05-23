package sd.tp1.server.replication;

import sd.tp1.common.protocol.Endpoint;

import java.util.Collection;

/**
 * Created by everyone on 5/23/16.
 */
public interface PartialReplicated {

    void addSource(String remoteId);
    void remSouce(String remoteId);

    int needReplicate();
    int canDispose();

    Collection<String> findReplicationTargets(Collection<String> available);
}
