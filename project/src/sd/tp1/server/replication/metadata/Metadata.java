package sd.tp1.server.replication.metadata;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by apontes on 5/15/16.
 */
public class Metadata implements Comparable<Metadata>, Serializable {

    private boolean deleted = false;
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
        this.deleted = false;
        this.logicClock = new LamportLogicClock(serverMetadata.getServerId());

        this.sourceSet = new HashSet<>();
        this.sourceSet.add(serverMetadata);

        this.album = album;
        this.picture = picture;

        this.local = serverMetadata;
    }

    /*public boolean isPicture(){
        return this.picture != null;
    }

    public boolean isAlbum(){
        return this.picture == null;
    }*/

    public boolean getDeleted(){
        return this.deleted;
    }

    public SharedAlbum getAlbum() {
        return album;
    }

    public void setAlbum(SharedAlbum album) {
        this.album = album;
    }

    public void setDeleted(boolean isDeleted){
        this.deleted = isDeleted;
    }

    public Set<ServerMetadata> getSourceSet() {
        return sourceSet;
    }

    public void setSourceSet(Set<ServerMetadata> sourceSet) {
        this.sourceSet = sourceSet;
    }

    public ServerMetadata getLocal() {
        return local;
    }

    public void setLocal(ServerMetadata local) {
        this.local = local;
    }

    public void setPicture(SharedPicture picture) {
        this.picture = picture;
    }

    public SharedPicture getPicture(){
        return this.picture;
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
}

