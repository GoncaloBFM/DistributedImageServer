package sd.tp1.server.replication.backdoor.client;

import sd.tp1.server.replication.backdoor.client.stubs.Metadata;

import java.util.stream.Collectors;

/**
 * Created by apontes on 5/16/16.
 */
public class MetadataWrapper extends Metadata {

    public MetadataWrapper(sd.tp1.server.replication.metadata.Metadata meta){

        this.deleted = meta.isDeleted();
        this.local = new ServerMetadataWrapper(meta.getLocal());

        this.logicClock = new LamportLogicClockWrapper(meta.getLogicClock());

        this.picture = meta.getPicture() == null ? null : new PictureWrapper(meta.getPicture());
        this.album = new AlbumWrapper(meta.getAlbum());

        this.sourceSet = meta.getSourceSet()
                .stream()
                .map(x -> new ServerMetadataWrapper(x))
                .collect(Collectors.toList());
    }

    public sd.tp1.server.replication.metadata.Metadata getMetadata(){
        sd.tp1.server.replication.metadata.Metadata meta = new sd.tp1.server.replication.metadata.Metadata();

        meta.isDeleted = this.deleted;
        meta.setLocal(((ServerMetadataWrapper) this.local).getServerMetada());
        meta.setLogicClock(((LamportLogicClockWrapper) this.logicClock).getLamportLogicClock());
        meta.setPicture(((PictureWrapper) picture).getSharedPicture());
        meta.setAlbum(((AlbumWrapper) album).getSharedAlbum());
        meta.setSourceSet(this.sourceSet
                .stream()
                .map(x -> ((ServerMetadataWrapper) x).getServerMetada())
                .collect(Collectors.toSet()));

        return meta;
    }
}
