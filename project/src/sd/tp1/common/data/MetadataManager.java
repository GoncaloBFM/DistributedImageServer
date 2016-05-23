package sd.tp1.common.data;

import java.util.List;

/**
 * Created by everyone on 5/17/16.
 */
public interface MetadataManager {
    boolean createAlbum(Album album);

    boolean uploadPicture(Album album, Picture picture, byte[] data);

    boolean deleteAlbum(Album album);

    boolean deletePicture(Album album, Picture picture);

    String getServerId();

    MetadataBundle getMetadata();

    boolean needUpdate(Album album);
    boolean needUpdate(Album album, Picture picture);

    void dispose(String album);
    void dispose(String album, String picture);

    Album getMetadata(String album);
    Picture getMetadata(String album, String picture);
}
