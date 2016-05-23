package sd.tp1.server.replication;

import sd.tp1.common.protocol.Endpoint;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * Created by everyone on 5/23/16.
 */
public class HashReplicated implements PartialReplicated{

    private int targetReplics;
    private String localId;
    private Set<String> sources = new ConcurrentSkipListSet<>();


    public HashReplicated(String localId, int targetReplics) {
        this.targetReplics = targetReplics;
        this.localId = localId;
    }

    @Override
    public void addSource(String remoteId) {
        this.sources.add(remoteId);
    }

    @Override
    public void remSouce(String remoteId) {
        sources.remove(remoteId);
    }

    private boolean isPrimary(){
        return this.localId.equals(
                this.sources
                        .parallelStream()
                        .max(Comparator.naturalOrder())
                        .orElse(null));
    }

    private boolean isWeakestLink(){
        return this.localId.equals(
                this.sources
                        .parallelStream()
                        .min(Comparator.naturalOrder())
                        .orElse(null));
    }

    @Override
    public int needReplicate() {
        int needs = this.targetReplics - this.sources.size();
        if(needs > 0 && isPrimary())
            return needs;

        return 0;
    }

    @Override
    public int canDispose() {
        int exceeds = this.sources.size() - this.targetReplics;
        if(exceeds > 0 && isWeakestLink())
            return exceeds;

        return 0;
    }

    @Override
    public Collection<String> findReplicationTargets(Collection<String> available) {
        List<String> list = available
                .parallelStream()
                .filter(s -> !sources.contains(s))
                .collect(Collectors.toList());

        Collections.shuffle(list);
        return list;
    }
}
