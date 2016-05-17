package sd.tp1.common.protocol;

import sd.tp1.common.data.Album;
import sd.tp1.common.data.Picture;

import java.net.URL;
import java.util.List;

/**
 * Created by apontes on 5/17/16.
 */
public interface Endpoint {
    URL getUrl();
    String getServerId();

    default boolean equals(Endpoint endpoint){
        if(endpoint == null)
            return false;

        URL url = getUrl();
        return url != null && url.equals(endpoint.getUrl());
    }

    List<Album> loadListOfAlbums();

    List<Picture> loadListOfPictures(String album);

    byte[] loadPictureData(String album, String picture);

    boolean createAlbum(Album album);

    boolean uploadPicture(Album album, Picture picture, byte[] data);

    boolean deleteAlbum(Album album);

    boolean deletePicture(Album album, Picture picture);
}
