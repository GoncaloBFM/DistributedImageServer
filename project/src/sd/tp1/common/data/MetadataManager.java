package sd.tp1.common.data;

import java.util.List;

/**
 * Created by apontes on 5/17/16.
 */
public interface MetadataManager {
    boolean createAlbum(Album album);

    boolean uploadPicture(Album album, Picture picture, byte[] data);

    boolean deleteAlbum(Album album);

    boolean deletePicture(Album album, Picture picture);

    String getServerId();

    MetadataBundle getMetadata();

    Album getAlbum(String albumName);
    Picture getPicture(String albumName, String pictureName);

    void setAlbum(Album album);
    void setPicture(Album album, Picture picture);

    boolean isNewer(Album album);
    boolean isNewer(Album album, Picture picture);
}
