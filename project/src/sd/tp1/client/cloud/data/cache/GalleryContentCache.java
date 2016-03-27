package sd.tp1.client.cloud.data.cache;

import sd.tp1.GalleryContentProvider;
import sd.tp1.client.cloud.Server;

/**
 * Created by apontes on 3/27/16.
 */
public interface GalleryContentCache extends GalleryContentProvider{

    void notifyServerDown(Server server);
    void notifyServerUp(Server server);

    //TODO assure need
    void notifyServerUpdate(Server server);

    void addContentChangeHandler(ContentChangeHandler handler);

}
