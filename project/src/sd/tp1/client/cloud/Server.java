package sd.tp1.client.cloud;

import sd.tp1.common.*;

import java.net.URL;
import java.util.List;

/**
 * Created by apontes on 3/25/16.
 */
public interface Server {
    URL getUrl();

    default boolean equals(Server server){
        return server.getUrl().equals(this.getUrl());
    }

    List<Album> loadListOfAlbums();

    List<Picture> loadListOfPictures(String album);

    byte[] loadPictureData(String album, String picture);

    void createAlbum(Album album);

    void uploadPicture(Album album, Picture picture, byte[] data);

    void deleteAlbum(Album album);

    boolean deletePicture(Album album, Picture picture);
}
