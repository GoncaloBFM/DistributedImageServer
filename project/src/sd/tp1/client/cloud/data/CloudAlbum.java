package sd.tp1.client.cloud.data;

import sd.tp1.Album;
import sd.tp1.client.cloud.Server;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by apontes on 3/27/16.
 */
public class CloudAlbum implements Album {
    private String name;
    private List<Server> servers = new LinkedList<>();

    public CloudAlbum(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }


    public List<Server> getServers(){
        return Collections.unmodifiableList(this.servers);
    }

    public void addServer(Server server){
        this.servers.add(server);
    }
}
