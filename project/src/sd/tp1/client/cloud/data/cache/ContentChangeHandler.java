package sd.tp1.client.cloud.data.cache;

import sd.tp1.Album;

/**
 * Created by apontes on 3/27/16.
 */
public interface ContentChangeHandler {
    void contentChange();
    void contentChange(Album album);
}
