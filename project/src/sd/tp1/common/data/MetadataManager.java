package sd.tp1.common.data;

/**
 * Created by apontes on 5/17/16.
 */
public interface MetadataManager {
    boolean createAlbum(Album album);

    boolean uploadPicture(Album album, Picture picture, byte[] data);

    boolean deleteAlbum(Album album);

    boolean deletePicture(Album album, Picture picture);

    String getServerId();
}
