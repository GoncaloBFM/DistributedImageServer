package sd.tp1.client;

import sd.tp1.server.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by apontes on 3/15/16.
 */
public interface ServiceDiscovery {
    /**
     * Search for service providers for given service
     * @param service service to look for
     * @param handler service handler
     */
    void discoverService(String service, ServiceHandler handler);
}
