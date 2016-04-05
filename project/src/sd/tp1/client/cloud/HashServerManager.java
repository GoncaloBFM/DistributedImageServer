package sd.tp1.client.cloud;

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

/**
 * Created by apontes on 3/25/16.
 */
public class HashServerManager implements ServerManager {

    //TODO assure location
    public static String SOAP_SERVICE = "42845_43178_SOAP";
    public static String REST_SERVICE = "42845_43178_REST";

    private static HashServerManager serverManager;

    private Collection<Server> serverCollection = new ConcurrentLinkedQueue<>();
    private Map<URL, Server> serverMap = new ConcurrentHashMap<>();

    private ServiceDiscovery soapServiceDiscovery;
    private ServiceDiscovery restServiceDiscovery;

    private Collection<ServerHandler> serverHandlersCollection = new ConcurrentLinkedQueue<>();

    private HashServerManager(){
        this.soapServiceDiscovery = new HeartbeatDiscovery(SOAP_SERVICE);
        this.restServiceDiscovery = new HeartbeatDiscovery(REST_SERVICE);

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
            case "SOAP": return new SoapServerWrapper(url);
            case "REST": return new RestServerWrapper(url);
        }

        return null;
    }

    private Server addServer(String service, URL url){
        Server server = this.serverMap.get(url);

        if(server == null){
            server = create(service, url);
            this.serverMap.put(url, server);
            this.serverCollection.add(server);
        }

        return server;
    }

    private void remServer(String service, URL url){
        this.serverCollection.remove(
                this.serverMap.remove(url));
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
    public Server getServerToCreateAlbum() {
        //TODO improve
        return serverCollection.iterator().next();
    }

    @Override
    public Server getServerToUploadPicture(CloudAlbum album){
        //TODO improve
        return album.getServers().get(0);
    }

    @Override
    public void addServerHandler(ServerHandler handler) {
        this.serverHandlersCollection.add(handler);
    }
}
