package sd.tp1.server.replication.version;

/**
 * Created by apontes on 5/10/16.
 */
public interface Version extends Comparable<Version>{
    Version next();
}
