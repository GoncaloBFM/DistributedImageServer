package sd.tp1.server.replication.backdoor.client;

import sd.tp1.common.SharedAlbum;

/**
 * Created by apontes on 5/16/16.
 */
public class AlbumWrapper extends sd.tp1.server.replication.backdoor.client.stubs.SharedAlbum {
    public AlbumWrapper(SharedAlbum album) {
        super();

        this.name = album.getName();
    }

    public SharedAlbum getSharedAlbum(){
        return new SharedAlbum(this.name);
    }
}
