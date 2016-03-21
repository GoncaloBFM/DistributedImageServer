package sd.tp1.server;

/**
 * Created by apontes on 3/15/16.
 */
public interface ServiceAnnouncer {
    /**
     * Start announcing service to the cloud
     * Blocking current thread
     * @param service service to announce
     */
    void announceService(String service);
}
