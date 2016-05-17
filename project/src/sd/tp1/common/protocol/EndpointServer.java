package sd.tp1.common.protocol;

import java.net.URL;

/**
 * Created by apontes on 5/17/16.
 */
public interface EndpointServer {
    void start();
    void stop();
    boolean isRunning();

    String getType();
    URL getUrl();
}
