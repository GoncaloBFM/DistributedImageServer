package sd.tp1.server.replication.metadata;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by apontes on 5/15/16.
 */
public class Metadata implements Comparable<Metadata> {

    private boolean isDeleted = false;
    private LamportLogicClock logicClock;
    private Set<ServerMetadata> sourceSet;

    private ServerMetadata local;

    private SharedAlbum album;
    private SharedPicture picture;

    public Metadata(){

    }

    public Metadata(SharedAlbum album, ServerMetadata serverMetadata){
        this(album, null, serverMetadata);
    }

    public Metadata(SharedAlbum album, SharedPicture picture, ServerMetadata serverMetadata){
        this.isDeleted = false;
        this.logicClock = new LamportLogicClock(serverMetadata.getServerId());

        this.sourceSet = new HashSet<>();
        this.sourceSet.add(serverMetadata);

        this.album = album;
        this.picture = picture;

        this.local = serverMetadata;
    }

    public boolean isPicture(){
        return this.picture != null;
    }

    public boolean isAlbum(){
        return this.picture == null;
    }

    public boolean isDeleted(){
        return this.isDeleted;
    }

    public void setDeleted(boolean isDeleted){
        this.isDeleted = isDeleted;
        this.setNextVersion();
    }

    public LamportLogicClock getLogicClock(){
        return this.logicClock;
    }

    public void setLogicClock(LamportLogicClock logicClock){
        this.logicClock = logicClock;
    }

    public void setNextVersion(){
        this.logicClock = this.logicClock.getNextVersion();
        this.logicClock.sourceId = this.local.getServerId();
    }

    @Override
    public int compareTo(Metadata o) {
        return this.logicClock.compareTo(o.logicClock);
    }

    public SharedAlbum getAlbum(){
        return this.album;
    }

    public SharedPicture getPicture(){
        return this.picture;
    }
}

