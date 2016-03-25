package sd.tp1.client.cloud;

import java.net.URL;
import java.util.Collection;

/**
 * Created by apontes on 3/25/16.
 */
public interface ServerManager {
    Collection<Server> getServers();
    Server getServer(URL url);

    void setServerDownListner();
}
