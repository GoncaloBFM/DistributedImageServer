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

    private static HashServerManager serverManager;

    private Collection<Server> serverCollection = new ConcurrentLinkedQueue<>();
    private Map<URL, Server> serverMap = new ConcurrentHashMap<>();

    private static final Logger logger = Logger.getLogger(HashServerManager.class.getSimpleName());


    private Collection<ServerHandler> serverHandlersCollection = new ConcurrentLinkedQueue<>();

    private HashServerManager(){
        ClientFactory.startDiscovery(new SrvHandler());
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

    private synchronized void addServer(String service, URL url){
        Server server = this.serverMap.get(url);

        if(server == null){
            try{
                server = ClientFactory.create(service, url);
                if(server == null)
                    return;

                this.serverMap.put(url, server);
                this.serverCollection.add(server);

                logger.info("Server added: " + url.toString());
                for(ServerHandler handler : this.serverHandlersCollection)
                    handler.serverAdded(server);
            }
            catch (ClientFactory.ClientFactoryException e){
                e.printStackTrace();
                logger.severe(e.getMessage());
            }
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
        LinkedList<Server> list = new LinkedList<>(album.getServers());
        Collections.shuffle(list);
        return list;
    }

    @Override
    public void addServerHandler(ServerHandler handler) {
        this.serverHandlersCollection.add(handler);
    }
}
