package sd.tp1.client.cloud.cache;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.Server;

import java.util.List;

/**
 * Created by apontes on 4/8/16.
 */
public interface CachedServer extends Server {
    List<Album> getCachedListOfAlbums();

    List<Picture> getCachedListOfPictures(Album album);
}
