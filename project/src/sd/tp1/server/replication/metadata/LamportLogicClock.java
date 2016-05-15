package sd.tp1.server.replication.metadata;

/**
 * Created by apontes on 5/15/16.
 */
public class LamportLogicClock implements Comparable<LamportLogicClock> {
    int version;
    String sourceId;

    public LamportLogicClock(){

    }

    public LamportLogicClock(String sourceId){
        this.version = 0;
        this.sourceId = sourceId;
    }

    @Override
    public int compareTo(LamportLogicClock o) {
        if(this.version - o.version != 0)
            return this.version - o.version;

        return this.sourceId.compareTo(o.sourceId);
    }

    public LamportLogicClock getNextVersion(){
        LamportLogicClock lamportLogicClock = new LamportLogicClock(this.sourceId);
        lamportLogicClock.version = this.version +1;

        return lamportLogicClock;
    }
}
