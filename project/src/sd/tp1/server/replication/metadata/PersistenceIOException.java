package sd.tp1.server.replication.metadata;

/**
 * Created by apontes on 5/12/16.
 */
public class PersistenceIOException extends RuntimeException {
    PersistenceIOException(String mesg){
        super(mesg);
    }

    PersistenceIOException(Exception e){
        super(e);
    }
}
