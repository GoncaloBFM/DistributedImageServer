package sd.tp1.client;

/**
 * Created by apontes on 3/15/16.
 */
public interface ServiceDiscovery {
    /**
     * Search for service providers for given service
     * @param handler service handler
     */
    void discoverService(ServiceHandler handler);
}
