package sd.tp1.client;

import sd.tp1.client.ws.WSServer;

/**
 * Created by apontes on 3/21/16.
 */
public interface ServiceHandler {
    void serviceDiscovered(WSServer server);
    void serviceNotAnymore(WSServer server);
}
