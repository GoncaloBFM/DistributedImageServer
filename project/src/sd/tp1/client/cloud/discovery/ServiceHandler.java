package sd.tp1.client.cloud.discovery;

import java.net.URL;

/**
 * Created by apontes on 4/5/16.
 */
public interface ServiceHandler {
    void serviceDiscovered(String service, URL url);
    void serviceLost(String service, URL url);
}
