package sd.tp1.server;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
import sun.security.provider.SHA;

import java.util.List;

/**
 * Created by gbfm on 4/4/16.
 */
public interface DataManager {
    List<SharedAlbum> loadListOfAlbums();

    List<SharedPicture> loadListOfPictures(String album);

    byte[] loadPictureData(Album album, Picture picture);

    void createAlbum(Album album);

    void uploadPicture(Album album, Picture picture, byte[] data);

    void deleteAlbum(Album album);

    boolean deletePicture(Album album, Picture picture);
}
