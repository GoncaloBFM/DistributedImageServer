package sd.tp1.server;

import sd.tp1.SharedAlbum;
import sd.tp1.SharedPicture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
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
}
