package sd.tp1.server.replication.engine;

import sd.tp1.common.discovery.*;
import sd.tp1.server.DataManager;

import sd.tp1.server.replication.backdoor.SOAPReplicationServerBackdoor;
import sd.tp1.server.replication.backdoor.client.SOAPBackdoorWrapper;
import sd.tp1.server.replication.metadata.FileMetadataManager;
import sd.tp1.server.replication.metadata.Metadata;
import sd.tp1.server.replication.metadata.MetadataManager;
import sd.tp1.server.replication.backdoor.ReplicationServerBackdoor;
import sd.tp1.server.replication.metadata.ServerMetadata;

import javax.xml.ws.Endpoint;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by apontes on 5/13/16.
 */
public class TotalReplicableServer implements ReplicableServer {

    private final static long MILLIS_BETWEEN_SYNC = 5000;

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

    private Map<URL, ReplicationServerBackdoor> urlMap = new ConcurrentHashMap<>();
    private Map<ServerMetadata, ReplicationServerBackdoor> metaMap = new ConcurrentHashMap<>();

    private ReplicationServerBackdoor serverBackdoor;

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

                SOAPBackdoorWrapper server = new SOAPBackdoorWrapper(url);
                ServerMetadata meta = server.getServerMetadata();

                if(meta != null){
                    urlMap.put(url, server);
                    metaMap.put(meta, server);
                }
                else {
                    System.err.println("Cant get meta from server");
                }
            }

            @Override
            public void serviceLost(String service, URL url) {
                if(! service.equals(SERVICE_NAME))
                    return;

                urlMap.remove(url);
                //todo remove from meta
            }
        });

        this.serverBackdoor = new SOAPReplicationServerBackdoor(metadataManager, dataManager,
                serverMetadata -> metaMap.get(serverMetadata));

        String endpointUrl = String.format("http://%s:%d/%s", "0.0.0.0", serverPort, serverPath);
        Endpoint.publish(endpointUrl, serverBackdoor);

        new Thread(){
            @Override
            public void run(){
               for(;;){
                   LinkedList<ReplicationServerBackdoor> toSync = new LinkedList<>();
                   urlMap.values().forEach(toSync::add);
                   Collections.shuffle(toSync);

                   while(!toSync.isEmpty()){
                       ReplicationServerBackdoor server = toSync.poll();

                       ServerMetadata localMeta = metadataManager.getServerMetadata();
                       ServerMetadata remoteMeta = server.getServerMetadata();
                       if(localMeta.getServerId().equals(remoteMeta.getServerId()))
                           continue;

                       server.sendMetadata(localMeta, getAllMetadata());

                       try {
                           Thread.sleep(MILLIS_BETWEEN_SYNC);
                       } catch (InterruptedException e) {
                           //do nothing
                           //todo remove
                           e.printStackTrace();
                       }
                   }

                   try {
                       Thread.sleep(MILLIS_BETWEEN_SYNC *5);
                   } catch (InterruptedException e) {
                       //do nothing
                       //todo remove
                       e.printStackTrace();
                   }
               }
            }
        }.start();

        System.out.println("Server backdoor started at: " + endpointUrl);
        this.isRunning = true;
    }

    private List<Metadata> getAllMetadata(){
        List<Metadata> meta = new LinkedList<>();

        dataManager.loadListOfAlbums().forEach(a -> {
            meta.add(metadataManager.getMetadata(a));
            dataManager.loadListOfPictures(a).forEach(p -> meta.add(metadataManager.getMetadata(a, p)));
        });

        return meta;
    }
}
