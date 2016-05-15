package sd.tp1.server;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;

/**
 * Created by apontes on 5/14/16.
 */
public interface DataOperationHandler {
    void onPictureUpload(SharedAlbum album, SharedPicture picture);
    void onPictureDelete(SharedAlbum album, SharedPicture picture);

    void onAlbumCreate(SharedAlbum album);
    void onAlbumDelete(SharedAlbum album);
}
