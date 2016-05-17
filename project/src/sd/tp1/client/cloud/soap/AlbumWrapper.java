package sd.tp1.client.cloud.soap;

import sd.tp1.client.cloud.soap.stubs.Metadata;
import sd.tp1.common.Album;
import sd.tp1.client.cloud.soap.stubs.SharedAlbum;

/**
 * Created by apontes on 3/25/16.
 */
class AlbumWrapper extends SharedAlbum implements Album {
    AlbumWrapper(SharedAlbum album){
        super();
        super.name = album.getName();
    }

    AlbumWrapper(Album album){
        super();
        super.name = album.getName();
    }
}
