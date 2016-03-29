package sd.tp1.client.cloud;

import sd.tp1.GalleryContentProvider;

import java.net.URL;

/**
 * Created by apontes on 3/25/16.
 */
public interface Server extends GalleryContentProvider {
    URL getUrl();
}
