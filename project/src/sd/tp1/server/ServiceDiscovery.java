package sd.tp1.server;

import java.util.List;

/**
 * Created by apontes on 3/15/16.
 */
public interface ServiceDiscovery {
    /**
     * Search for service providers for given service
     * @param service service to look for
     * @return List containing hosts
     */
    List<String> discoverService(String service);
}
