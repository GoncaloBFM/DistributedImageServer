package sd.tp1.server.ws;

import sd.tp1.server.HeartbeatAnnouncer;
import sd.tp1.server.ServiceAnnouncer;

import javax.xml.ws.Endpoint;
import java.io.File;
import java.net.URL;

/**
 * Created by apontes on 3/21/16.
 */
public class WSServerRun {

    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 65535;

    private static final int DEFAULT_PORT = generateRandomPort();
    private static final String DEFAULT_ROOT = ".";
    private static final String DEFAULT_SERVICE_PATH = "FileServer";

    /**
     *
     * @param args 0: Root; 1:ServerPath; 2:Port;
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {

        File root = new File((args.length >= 1) ? args[0] : DEFAULT_ROOT);
        String serverPath = (args.length >= 2) ? args[1] : DEFAULT_SERVICE_PATH;
        int port = (args.length >= 3) ? Integer.parseInt(args[2]) : DEFAULT_PORT;

        String url = String.format("http://%s:%d/%s", "0.0.0.0", port, serverPath);
        Endpoint.publish(String.format("http://%s:%d/%s", "0.0.0.0", port, serverPath), new WSServer(root));
        System.err.println(String.format("Server started at port %s, root:%s", port, root.getAbsoluteFile()));

        ServiceAnnouncer announcer = (new HeartbeatAnnouncer(url));
        announcer.announceService();
    }

    static int generateRandomPort(){
        return (int) (Math.random() * (MAX_PORT - MIN_PORT) + MIN_PORT);
    }
}
