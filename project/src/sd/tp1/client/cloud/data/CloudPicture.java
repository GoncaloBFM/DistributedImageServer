package sd.tp1.client.cloud.data;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.Server;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by apontes on 3/27/16.
 */
public class CloudPicture implements Picture {

    private String name;
    private List<Server> servers = new LinkedList<>();

    private CloudAlbum album;

    public CloudPicture(String name, CloudAlbum album){
        this.name = name;this.album = album;
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
    public void remServer(Server server) { this.servers.remove(server); }

    public CloudAlbum getAlbum(){
        return this.album;
    }
}
