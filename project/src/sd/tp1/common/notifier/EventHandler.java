package sd.tp1.common.notifier;

import sd.tp1.common.data.Album;
import sd.tp1.common.data.AlbumPicture;

/**
 * Created by apontes on 5/21/16.
 */
public interface EventHandler {
    void onAlbumUpdate(Album album);
    void onPictureUpdate(AlbumPicture picture);
}
