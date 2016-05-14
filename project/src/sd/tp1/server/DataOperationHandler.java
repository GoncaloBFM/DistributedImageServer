package sd.tp1.server;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;

/**
 * Created by apontes on 5/14/16.
 */
public interface DataOperationHandler {
    void onPictureUpload(Album album, Picture picture);
    void onPictureDelete(Album album, Picture picture);

    void onAlbumCreate(Album album);
    void onAlbumDelete(Album album);
}
