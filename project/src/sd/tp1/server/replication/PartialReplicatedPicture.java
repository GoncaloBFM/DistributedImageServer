package sd.tp1.server.replication;

import sd.tp1.common.data.SharedAlbum;
import sd.tp1.common.data.SharedAlbumPicture;
import sd.tp1.common.protocol.Endpoint;

import java.util.Collection;

/**
 * Created by everyone on 5/23/16.
 */
public class PartialReplicatedPicture extends SharedAlbumPicture implements PartialReplicated {

    public static final int TARGET_REPLICS = 3;

    private HashReplicated sources;

    public PartialReplicatedPicture(SharedAlbumPicture picture, String localId){
        this(picture, localId, TARGET_REPLICS);
    }

    public PartialReplicatedPicture(SharedAlbumPicture picture, String localId, int targetReplics){
        this.setAlbum(picture.getAlbum());
        this.setPicture(picture.getPicture());

        sources = new HashReplicated(localId, targetReplics);
    }

    @Override
    public void addSource(String remoteId) {
        sources.addSource(remoteId);
    }

    @Override
    public void remSouce(String remoteId) {
        sources.remSouce(remoteId);
    }

    @Override
    public int needReplicate() {
        return sources.needReplicate();
    }

    @Override
    public int canDispose() {
        return sources.canDispose();
    }

    @Override
    public Collection<String> findReplicationTargets(Collection<String> available) {
        return sources.findReplicationTargets(available);
    }
}
