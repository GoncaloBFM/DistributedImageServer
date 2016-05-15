package sd.tp1.server;

import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
import sun.security.provider.SHA;

import java.util.List;

/**
 * Created by gbfm on 4/4/16.
 */
public interface DataManager {
    List<SharedAlbum> loadListOfAlbums();

    List<SharedPicture> loadListOfPictures(SharedAlbum album);

    byte[] loadPictureData(SharedAlbum album, SharedPicture picture);

    SharedAlbum createAlbum(String name);
    SharedAlbum createAlbumNoNotify(String name);

    SharedPicture uploadPicture(SharedAlbum album, String name, byte[] data);
    SharedPicture uploadPictureNoNotify(SharedAlbum album, String name, byte[] data);

    void deleteAlbum(SharedAlbum album);
    void deleteAlbumNoNotify(SharedAlbum album);

    boolean deletePicture(SharedAlbum album, SharedPicture picture);
    boolean deletePictureNoNotify(SharedAlbum album, SharedPicture picture);

    void addDataOperationHandler(DataOperationHandler handler);
}
