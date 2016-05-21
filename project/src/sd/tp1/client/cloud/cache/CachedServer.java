package sd.tp1.client.cloud.cache;

import sd.tp1.common.data.Album;
import sd.tp1.common.data.AlbumPicture;
import sd.tp1.common.data.Picture;
import sd.tp1.client.cloud.Server;

import java.util.List;

/**
 * Created by apontes on 4/8/16.
 */
public interface CachedServer extends Server {
    List<Album> getCachedListOfAlbums();
    List<Picture> getCachedListOfPictures(Album album);

    void notifyAlbumChange(Album album);
    void notifyPictureChange(AlbumPicture albumPicture);
}
