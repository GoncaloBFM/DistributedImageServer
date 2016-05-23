package sd.tp1.client.cloud.data;

import sd.tp1.client.cloud.Server;

import java.util.Collection;

/**
 * Created by everyone on 4/5/16.
 */
public interface CloudObject {
    void addServer(Server server);
    void remServer(Server server);

    Collection<Server> getServers();

    boolean isAvailable();

}
