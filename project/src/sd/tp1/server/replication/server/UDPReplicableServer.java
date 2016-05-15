package sd.tp1.server.replication.server;

import sd.tp1.common.discovery.HeartbeatAnnouncer;
import sd.tp1.common.discovery.ServiceAnnouncer;
import sd.tp1.common.discovery.ServiceDiscovery;
import sd.tp1.server.DataManager;
import sd.tp1.server.replication.metadata.old.FileMetadataManager;
import sd.tp1.server.replication.metadata.old.MetadataManager;
import sd.tp1.server.replication.metadata.ServerMetadata;

import java.io.*;
import java.util.UUID;

/**
 * Created by apontes on 5/13/16.
 */
public class UDPReplicableServer {

    private static String SERVICE_DISCOVERY = "42845_43178_ServerReplication";
    private static int SERVICE_DISCOVERY_PORT = 6966;

    private static File ROOT = new File(".metadata");
    private static ServerMetadata getResourceSource() throws IOException {
        try {
            DataInput input = new DataInputStream(new FileInputStream(new File(ROOT, ".server-id")));

            return new ServerMetadata(input.readLine());
        } catch (FileNotFoundException e) {
            PrintStream output = new PrintStream(new FileOutputStream(new File(ROOT, ".server-id")));
            String serverId = UUID.randomUUID().toString();
            output.println(serverId);
            output.flush();
            output.close();

            return new ServerMetadata(serverId);
        }
    }

    private ServerMetadata source;
    private File root;

    private DataManager dataManager;
    private MetadataManager metadataManager;

    private ServiceAnnouncer serviceAnnouncer;
    private ServiceDiscovery serviceDiscovery;

    UDPReplicableServer(DataManager dataManager, String serverPath, int serverPort) throws IOException {
        this(ROOT, getResourceSource(), dataManager, serverPath, serverPort);
    }

    UDPReplicableServer(File root, ServerMetadata source, DataManager dataManager, String serverPath, int serverPort){
        this.source = source;
        this.dataManager = dataManager;
        this.metadataManager = new FileMetadataManager(source, dataManager, root);

        this.serviceAnnouncer = new HeartbeatAnnouncer(SERVICE_DISCOVERY, SERVICE_DISCOVERY_PORT, serverPath, serverPort);
    }
}
