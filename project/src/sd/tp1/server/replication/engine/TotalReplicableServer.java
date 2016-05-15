package sd.tp1.server.replication.engine;

import sd.tp1.common.discovery.*;
import sd.tp1.server.DataManager;

import sd.tp1.server.replication.backdoor.SOAPReplicationServerBackdoor;
import sd.tp1.server.replication.metadata.FileMetadataManager;
import sd.tp1.server.replication.metadata.MetadataManager;
import sd.tp1.server.replication.backdoor.ReblicationServerBackdoor;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by apontes on 5/13/16.
 */
public class TotalReplicableServer {

    private static String SERVICE_NAME = "42845_43178_ServerReplication";
    private static int SERVICE_PORT = 6966;

    private DataManager dataManager;
    private MetadataManager metadataManager;

    private ServiceAnnouncer serviceAnnouncer;
    private ServiceDiscovery serviceDiscovery;

    private Map<URL, ReblicationServerBackdoor> serverMap = new ConcurrentHashMap<>();
    private Set<URL> alreadySync = new ConcurrentSkipListSet<>();

    private ReblicationServerBackdoor serverBackdoor;

    TotalReplicableServer(DataManager dataManager, String serverPath, int serverPort){
        this.dataManager = dataManager;
        this.metadataManager = new FileMetadataManager(dataManager);

        this.serviceAnnouncer = new HeartbeatAnnouncer(SERVICE_NAME, SERVICE_PORT, serverPath, serverPort);
        this.serviceDiscovery = new HeartbeatDiscovery(SERVICE_NAME, SERVICE_PORT);

        this.serviceAnnouncer.startAnnounceService();
        this.serviceDiscovery.discoverService(new ServiceHandler() {
            @Override
            public void serviceDiscovered(String service, URL url) {
                if(! service.equals(SERVICE_NAME))
                    return;

                serverMap.put(url, null);
            }

            @Override
            public void serviceLost(String service, URL url) {
                if(! service.equals(SERVICE_NAME))
                    return;

                serverMap.remove(url);
            }
        });

        //this.serverBackdoor = new SOAPReplicationServerBackdoor();
    }
}
