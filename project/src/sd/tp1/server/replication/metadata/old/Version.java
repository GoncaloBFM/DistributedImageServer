package sd.tp1.server.replication.metadata.old;

/**
 * Created by apontes on 5/10/16.
 */
public interface Version extends Comparable<Version>{
    Version next();
}
