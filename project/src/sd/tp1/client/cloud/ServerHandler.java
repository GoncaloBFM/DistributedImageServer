package sd.tp1.client.cloud;

/**
 * Created by apontes on 3/21/16.
 */
public interface ServerHandler {
    void serverAdded(Server server);
    void serverLost(Server server);
}
