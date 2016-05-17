package sd.tp1.common.data;

import java.util.List;

/**
 * Created by gbfm on 4/4/16.
 */
public interface DataManager extends MetadataManager {
    List<SharedAlbum> loadListOfAlbums();

    List<SharedPicture> loadListOfPictures(String album);

    byte[] loadPictureData(String album, String picture);
}
