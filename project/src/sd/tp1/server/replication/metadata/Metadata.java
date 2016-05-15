package sd.tp1.server.replication.metadata;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by apontes on 5/15/16.
 */
public class Metadata implements Comparable<Metadata> {

    private String identifier;
    private boolean isDeleted = false;
    private LamportLogicClock logicClock;
    private Set<ServerMetadata> sourceSet;
    private ServerMetadata author;

    public Metadata(){

    }

    public Metadata(String identifier, ServerMetadata serverMetadata){
        this.identifier = identifier;
        this.isDeleted = false;
        this.logicClock = new LamportLogicClock(serverMetadata.getServerId());

        this.sourceSet = new HashSet<>();
        this.sourceSet.add(serverMetadata);
    }

    public String getIdentifier(){
        return identifier;
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
        this.logicClock.sourceId = this.author.getServerId();
    }

    @Override
    public int compareTo(Metadata o) {
        return this.logicClock.compareTo(o.logicClock);
    }
}

