package sd.tp1.client;

import sd.tp1.server.Server;

/**
 * Created by apontes on 3/21/16.
 */
public interface ServiceHandler {
    void serviceDiscovered(Server server);
    void serviceNotAnymore(Server server);
}
