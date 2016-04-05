package sd.tp1.client.cloud.data.cache;

/**
 * Created by apontes on 4/3/16.
 */
public class CachedItem implements Cached {
    private static final long CACHE_TTL = 10000L;

    long creation;
    long expiration;

    boolean dirty;

    CachedItem(){
        this(CACHE_TTL);
    }

    CachedItem(long ttl){
        this.creation = System.currentTimeMillis();
        this.expiration = this.creation + ttl;
        this.dirty = false;
    }

    @Override
    public boolean isDirty() {
        return this.dirty || this.ttl() < 0;
    }

    @Override
    public long ttl() {
        if(dirty)
            return -1;

        return this.expiration - System.currentTimeMillis();
    }

    @Override
    public long creation() {
        return this.creation;
    }

    @Override
    public void makeDirty() {
        this.dirty = true;
    }
}
