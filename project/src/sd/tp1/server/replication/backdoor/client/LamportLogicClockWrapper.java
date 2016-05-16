package sd.tp1.server.replication.backdoor.client;

import sd.tp1.server.replication.metadata.LamportLogicClock;

/**
 * Created by apontes on 5/16/16.
 */
public class LamportLogicClockWrapper extends sd.tp1.server.replication.backdoor.client.stubs.LamportLogicClock {
    public LamportLogicClockWrapper(LamportLogicClock logicClock) {
        this.sourceId = logicClock.getSourceId();
        this.version = logicClock.getVersion();
    }

    public LamportLogicClock getLamportLogicClock(){
        LamportLogicClock clock = new LamportLogicClock(sourceId);
        clock.setVersion(this.version);
        return clock;
    }
}
