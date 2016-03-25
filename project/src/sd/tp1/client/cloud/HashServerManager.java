package sd.tp1.client.cloud;

import sd.tp1.client.cloud.soap.SoapServerWrapper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by apontes on 3/25/16.
 */
public class HashServerManager implements ServerManager {

    private static HashServerManager serverManager;

    private LinkedList<Server> serverList = new LinkedList<>();
    private HashMap<URL, Server> serverMap = new HashMap<>();

    private HashServerManager(){

    }

    public static ServerManager getServerManager(){
        if(serverManager == null)
            serverManager = new HashServerManager();

        return serverManager;
    }

    @Override
    public Collection<Server> getServers() {
        return Collections.unmodifiableCollection(this.getServers());
    }

    @Override
    public Server getServer(URL url) {
        //TODO implement REST/SOAP decision logic
        Server server =
                new SoapServerWrapper(url);

        this.serverList.add(server);
        this.serverMap.put(url, server);

        return server;
    }

    @Override
    public void setServerDownListner() {
        //TODO implement
        throw new NotImplementedException();
    }
}
