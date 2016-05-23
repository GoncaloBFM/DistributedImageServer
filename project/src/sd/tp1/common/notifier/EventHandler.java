package sd.tp1.common.notifier;

import sd.tp1.common.data.Album;
import sd.tp1.common.data.AlbumPicture;

/**
 * Created by everyone on 5/21/16.
 */
public interface EventHandler {
    void onAlbumUpdate(String album);
    void onPictureUpdate(String album, String picture);
}
