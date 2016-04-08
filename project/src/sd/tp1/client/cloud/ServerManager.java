package sd.tp1.client.cloud;

import sd.tp1.client.cloud.data.CloudAlbum;

import java.util.Collection;

/**
 * Created by apontes on 3/25/16.
 */
public interface ServerManager {
    Collection<Server> getServers();

    Collection<Server> getServerToCreateAlbum();
    Collection<Server> getServerToUploadPicture(CloudAlbum album);

    void addServerHandler(ServerHandler handler);
}
