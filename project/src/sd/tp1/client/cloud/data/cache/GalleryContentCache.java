package sd.tp1.client.cloud.data.cache;

import sd.tp1.GalleryContentProvider;
import sd.tp1.client.cloud.Server;

/**
 * Created by apontes on 3/27/16.
 */
public interface GalleryContentCache extends GalleryContentProvider{

    void serverDown(Server server);
    void serverUp(Server server);

    void addContentChangeHandler(ContentChangeHandler handler);
}
