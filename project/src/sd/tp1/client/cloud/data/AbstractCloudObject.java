package sd.tp1.client.cloud.data;

import sd.tp1.client.cloud.Server;
import sd.tp1.common.data.LogicClockMetadata;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by apontes on 4/5/16.
 */
public abstract class AbstractCloudObject extends LogicClockMetadata implements CloudObject{

    private Collection<Server> serverCollection = new ConcurrentLinkedQueue<>();

    AbstractCloudObject(String serverId){
        super(serverId);
    }

    @Override
    public void addServer(Server server) {
        if(!this.serverCollection.contains(server))
            this.serverCollection.add(server);
    }

    @Override
    public void remServer(Server server) {
        this.serverCollection.remove(server);
    }

    @Override
    public Collection<Server> getServers() {
        Iterator<Server> iterator = this.serverCollection.iterator();
        Collection<Server> serverCollection = new LinkedList<>();
        while(iterator.hasNext())
            serverCollection.add(iterator.next());

        return Collections.unmodifiableCollection(serverCollection);
    }

    @Override
    public boolean isAvailable() {
        return this.serverCollection.size() > 0;
    }
}
