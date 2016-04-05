package sd.tp1.client.cloud.data.cache;

import sd.tp1.Picture;
import sd.tp1.client.cloud.data.CloudAlbum;
import sd.tp1.client.cloud.data.CloudPicture;

import java.util.List;

/**
 * Created by apontes on 4/3/16.
 */
public class CachedAlbum extends CloudAlbum implements Cached {

    private Cached cached = new CachedItem();

    private List<CachedPicture> pictures;

    public CachedAlbum(String name, List<CachedPicture> pictures) {
        super(name);

        this.pictures = pictures;
    }

    public List<CachedPicture> getPictures(){
        return this.pictures;
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

    public void recache(List<CachedPicture> pictures){
        this.pictures = pictures;
        this.cached = new CachedItem();
    }
}
