package sd.tp1.client.cloud;

import sd.tp1.client.cloud.cache.HashCachedServer;
import sd.tp1.client.cloud.data.CloudAlbum;
import sd.tp1.client.cloud.discovery.HeartbeatDiscovery;
import sd.tp1.client.cloud.discovery.ServiceDiscovery;
import sd.tp1.client.cloud.discovery.ServiceHandler;
import sd.tp1.client.cloud.rest.RestServerWrapper;
import sd.tp1.client.cloud.soap.SoapServerWrapper;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Created by apontes on 3/25/16.
 */
public class HashServerManager implements ServerManager {

    //TODO assure location
    public static final String SOAP_SERVICE = "42845_43178_SOAP";
    public static final int SOAP_SERVICE_PORT = 6969;

    public static final String REST_SERVICE = "42845_43178_REST";
    public static final int REST_SERVICE_PORT = 6968;

    private static HashServerManager serverManager;

    private Collection<Server> serverCollection = new ConcurrentLinkedQueue<>();
    private Map<URL, Server> serverMap = new ConcurrentHashMap<>();

    private ServiceDiscovery soapServiceDiscovery;
    private ServiceDiscovery restServiceDiscovery;

    private static final Logger logger = Logger.getLogger(HashServerManager.class.getSimpleName());


    private Collection<ServerHandler> serverHandlersCollection = new ConcurrentLinkedQueue<>();

    private HashServerManager(){
        this.soapServiceDiscovery = new HeartbeatDiscovery(SOAP_SERVICE, SOAP_SERVICE_PORT);
        this.restServiceDiscovery = new HeartbeatDiscovery(REST_SERVICE, REST_SERVICE_PORT);

        this.soapServiceDiscovery.discoverService(new SrvHandler());
        this.restServiceDiscovery.discoverService(new SrvHandler());
    }

    private class SrvHandler implements ServiceHandler {
        @Override
        public void serviceDiscovered(String service, URL url) {
            addServer(service, url);
        }

        @Override
        public void serviceLost(String service, URL url) {
            remServer(service, url);
        }
    }

    private Server create(String service, URL url){
        switch (service){
            case SOAP_SERVICE: return new HashCachedServer(new SoapServerWrapper(url));
            case REST_SERVICE: return new HashCachedServer(new RestServerWrapper(url));
        }

        return null;
    }

    private synchronized void addServer(String service, URL url){
        Server server = this.serverMap.get(url);

        if(server == null){
            logger.info("Server added: " + url.toString());
            server = create(service, url);
            if(server == null)
                return;

            this.serverMap.put(url, server);
            this.serverCollection.add(server);


            for(ServerHandler handler : this.serverHandlersCollection)
                handler.serverAdded(server);
        }
    }

    private synchronized void remServer(String service, URL url){
        Server server = this.serverMap.remove(url);
        if(server == null) {
            return;
        }
        logger.info("Server removed: " + url.toString());

        this.serverCollection.remove(server);

        for(ServerHandler handler : this.serverHandlersCollection)
            handler.serverLost(server);
    }

    public static ServerManager getServerManager(){
        if(serverManager == null)
            serverManager = new HashServerManager();

        return serverManager;
    }

    @Override
    public Collection<Server> getServers() {
        return Collections.unmodifiableCollection(this.serverCollection);
    }


    @Override
    public Collection<Server> getServerToCreateAlbum() {
        LinkedList<Server> list = new LinkedList<>(serverCollection);
        Collections.shuffle(list);
        return list;
    }

    @Override
    public Collection<Server> getServerToUploadPicture(CloudAlbum album){
        LinkedList<Server> list = new LinkedList<>(serverCollection);
        Collections.shuffle(list);
        return list;
    }

    @Override
    public void addServerHandler(ServerHandler handler) {
        this.serverHandlersCollection.add(handler);
    }
}
