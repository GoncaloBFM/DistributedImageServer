package sd.tp1.common.data;

import sd.tp1.common.notifier.EventHandler;

import java.util.List;

/**
 * Created by everyone on 4/4/16.
 */
public interface DataManager extends MetadataManager {
    List<SharedAlbum> loadListOfAlbums();

    List<SharedPicture> loadListOfPictures(String album);

    byte[] loadPictureData(String album, String picture);

    void addEventHandler(EventHandler eventHandler);
}
