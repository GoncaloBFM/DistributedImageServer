package sd.tp1.client.cloud;

import sd.tp1.client.cloud.data.CloudAlbum;
import sd.tp1.client.cloud.discovery.HeartbeatDiscovery;
import sd.tp1.client.cloud.discovery.ServiceDiscovery;
import sd.tp1.client.cloud.soap.SoapServerWrapper;

import java.net.URL;
import java.util.*;

/**
 * Created by apontes on 3/25/16.
 */
public class HashServerManager implements ServerManager {

    //TODO assure location
    public static String SERVICE_TO_DISCOVERY = "42845_43178_Server";

    private static HashServerManager serverManager;

    private LinkedList<Server> serverList = new LinkedList<>();
    private HashMap<URL, Server> serverMap = new HashMap<>();

    private ServiceDiscovery serviceDiscovery;
    private List<ServerHandler> serverHandlers = new LinkedList<>();

    private HashServerManager(){
        serviceDiscovery = new HeartbeatDiscovery(SERVICE_TO_DISCOVERY);
        serviceDiscovery.discoverService(new ServerHandler() {
            @Override
            public void serverAdded(Server server) {
                for(ServerHandler handler : serverHandlers)
                    handler.serverAdded(server);
            }

            @Override
            public void serverLost(Server server) {
                //TODO something
                for(ServerHandler handler : serverHandlers)
                    ;//handler.serverLost(server);
            }
        });
    }

    public static ServerManager getServerManager(){
        if(serverManager == null)
            serverManager = new HashServerManager();

        return serverManager;
    }

    @Override
    public Collection<Server> getServers() {
        return Collections.unmodifiableCollection(this.serverList);
    }

    @Override
    public Server getServer(URL url) {
        Server server = this.serverMap.get(url);

        if(server == null){
            //TODO implement REST/SOAP decision logic
            server = new SoapServerWrapper(url);
            this.serverList.add(server);
            this.serverMap.put(url, server);
        }

        return server;
    }

    @Override
    public Server getServerToCreateAlbum() {
        //TODO improve
        return serverList.get(0);
    }

    public Collection<Server> getServerToUploadPicture(CloudAlbum album){
        //TODO improve
        return album.getServers();
    }

    @Override
    public void addServerHandler(ServerHandler handler) {
        this.serverHandlers.add(handler);
    }
}
