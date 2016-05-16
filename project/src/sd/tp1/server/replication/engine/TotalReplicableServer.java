package sd.tp1.server.replication.engine;

import sd.tp1.common.discovery.*;
import sd.tp1.server.DataManager;

import sd.tp1.server.replication.backdoor.SOAPReplicationServerBackdoor;
import sd.tp1.server.replication.metadata.FileMetadataManager;
import sd.tp1.server.replication.metadata.Metadata;
import sd.tp1.server.replication.metadata.MetadataManager;
import sd.tp1.server.replication.backdoor.ReblicationServerBackdoor;
import sd.tp1.server.replication.metadata.ServerMetadata;
import sd.tp1.server.soap.SoapServer;

import javax.xml.ws.Endpoint;
import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by apontes on 5/13/16.
 */
public class TotalReplicableServer implements ReplicableServer {

    private final static String SERVICE_NAME = "42845_43178_ServerReplication";
    private final static int SERVICE_PORT = 6966;

    private final static String DEFAULT_SERVER_PATH = "replication";
    private final static int DEFAULT_SERVER_PORT = 8090;

    private DataManager dataManager;
    private MetadataManager metadataManager;

    private ServiceAnnouncer serviceAnnouncer;
    private ServiceDiscovery serviceDiscovery;

    private String serverPath;
    private int serverPort;

    private Map<URL, ReblicationServerBackdoor> urlMap = new ConcurrentHashMap<>();
    private Map<ServerMetadata, ReblicationServerBackdoor> metaMap = new ConcurrentHashMap<>();

    private Set<URL> alreadySync = new ConcurrentSkipListSet<>();

    private ReblicationServerBackdoor serverBackdoor;

    private boolean isRunning = false;

    public TotalReplicableServer(DataManager dataManager, File root){
        this(dataManager, root, DEFAULT_SERVER_PATH, DEFAULT_SERVER_PORT);
    }

    public TotalReplicableServer(DataManager dataManager, File root, String serverPath, int serverPort){
        this.dataManager = dataManager;
        this.metadataManager = new FileMetadataManager(dataManager, root);

        this.serverPath = serverPath;
        this.serverPort = serverPort;

        this.serviceAnnouncer = new HeartbeatAnnouncer(SERVICE_NAME, SERVICE_PORT, serverPath, serverPort);
        this.serviceDiscovery = new HeartbeatDiscovery(SERVICE_NAME, SERVICE_PORT);
    }

    @Override
    public void startReplication() {
        if(this.isRunning)
            return;

        this.serviceAnnouncer.startAnnounceService();
        this.serviceDiscovery.discoverService(new ServiceHandler() {
            @Override
            public void serviceDiscovered(String service, URL url) {
                if(! service.equals(SERVICE_NAME))
                    return;

                //urlMap.put(url, null);
            }

            @Override
            public void serviceLost(String service, URL url) {
                if(! service.equals(SERVICE_NAME))
                    return;

                urlMap.remove(url);
            }
        });

        this.serverBackdoor = new SOAPReplicationServerBackdoor(metadataManager, dataManager,
                serverMetadata -> metaMap.get(serverMetadata));

        String endpointUrl = String.format("http://%s:%d/%s", "0.0.0.0", serverPort, serverPath);
        Endpoint.publish(endpointUrl, serverBackdoor);

        System.out.println("Server backdoor started at: " + endpointUrl);
        this.isRunning = true;
    }
}
