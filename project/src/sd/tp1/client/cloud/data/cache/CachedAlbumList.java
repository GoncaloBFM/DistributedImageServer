package sd.tp1.client.cloud.data.cache;

import sd.tp1.Album;
import sd.tp1.client.cloud.Server;

import java.util.List;

/**
 * Created by apontes on 4/3/16.
 */
public class CachedAlbumList implements Cached {

    private Server server;

    private Cached cached = new CachedItem();
    private List<CachedAlbum> albumList;

    public CachedAlbumList(Server server, List<CachedAlbum> albumList){
        this.server = server;
        this.albumList = albumList;
    }

    public Server getServer(){
        return this.server;
    }

    public List<CachedAlbum> getAlbumList(){
        return this.albumList;
    }

    public void recache(List<CachedAlbum> albumList){
        this.albumList = albumList;
        this.cached = new CachedItem();
    }

    @Override
    public boolean isDirty() {
        return cached.isDirty();
    }

    @Override
    public long ttl() {
        return cached.ttl();
    }

    @Override
    public long creation() {
        return cached.creation();
    }

    @Override
    public void makeDirty() {
        cached.makeDirty();
    }
}
