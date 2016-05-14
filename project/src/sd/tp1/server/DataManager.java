package sd.tp1.server;

import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;

import java.util.List;

/**
 * Created by gbfm on 4/4/16.
 */
public interface DataManager {
    List<SharedAlbum> loadListOfAlbums();

    List<SharedPicture> loadListOfPictures(SharedAlbum album);

    byte[] loadPictureData(SharedAlbum album, SharedPicture picture);

    SharedAlbum createAlbum(String name);

    SharedPicture uploadPicture(SharedAlbum album, String name, byte[] data);

    void deleteAlbum(SharedAlbum album);

    boolean deletePicture(SharedAlbum album, SharedPicture picture);

    void addDataOperationHandler(DataOperationHandler handler);
}
