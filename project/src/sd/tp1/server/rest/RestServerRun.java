package sd.tp1.server.rest;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import sd.tp1.common.discovery.HeartbeatAnnouncer;
import sd.tp1.common.discovery.ServiceAnnouncer;
import sd.tp1.server.DataManager;
import sd.tp1.server.FileDataManager;
import sd.tp1.server.replication.engine.ReplicableServer;
import sd.tp1.server.replication.engine.TotalReplicableServer;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.net.URI;
import java.nio.file.NotDirectoryException;

/**
 * Created by gbfm on 3/29/16.
 */
public class RestServerRun {
    private static final String SERVICE_TO_ANNOUNCE = "42845_43178_REST";
    private static final int ANNOUNCE_ON_PORT = 6968;

    private static final int MIN_PORT = 1024; //1024;
    private static final int MAX_PORT = 65535; //65535;

    private static final int DEFAULT_PORT = generateRandomPort();
    private static final String DEFAULT_ROOT = ".";
    private static final String DEFAULT_SERVICE_PATH = "PictureServer";


    public static void main(String args[]) {
        File root = new File((args.length >= 1) ? args[0] : DEFAULT_ROOT);
        String serverPath = (args.length >= 2) ? args[1] : DEFAULT_SERVICE_PATH;
        int port = (args.length >= 3) ? Integer.parseInt(args[2]) : DEFAULT_PORT;

        URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(port).build();
        ResourceConfig config = new ResourceConfig();

        try {
            DataManager dataManager = new FileDataManager(root);

            RestServer server = new RestServer(serverPath, dataManager);

            config.register(server);
            JdkHttpServerFactory.createHttpServer(baseUri, config);
            System.out.println(String.format("Server started at port %s, root:%s, path:%s", port, root.getAbsoluteFile(), serverPath));

            //Start announcer
            ServiceAnnouncer serviceAnnouncer = new HeartbeatAnnouncer(SERVICE_TO_ANNOUNCE,ANNOUNCE_ON_PORT, serverPath, port);
            serviceAnnouncer.startAnnounceService();
            System.out.println("Service announcer started! ;)");


            //Start replication system
            ReplicableServer replicableServer = new TotalReplicableServer(dataManager, root, "replication", port+1);
            replicableServer.startReplication();

        } catch (NotDirectoryException e) {
            System.err.println("Directory not available.\n Terminating.");
            System.exit(1);
        }

    }

    static int generateRandomPort(){
        return (int) (Math.random() * (MAX_PORT - MIN_PORT) + MIN_PORT);
    }
}
