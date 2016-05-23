package sd.tp1.common.discovery;

import java.net.URL;

/**
 * Created by everyone on 4/5/16.
 */
public interface ServiceHandler {
    void serviceDiscovered(String service, URL url);
    void serviceLost(String service, URL url);
}
