package sd.tp1.client.cloud.cache;

/**
 * Created by apontes on 4/5/16.
 */
public class CachedObject<T> implements Cached<T> {
    private static final long DEFAULT_TTL = 10000L;

    private long cachedAt;
    private long expiration;
    private long ttl;
    private boolean dirty;

    protected T content;

    CachedObject(T content){
        this(content, DEFAULT_TTL);
    }

    CachedObject(T content, long ttl){
        this.recache(content, ttl);
    }

    @Override
    public long cachedAt() {
        return this.cachedAt;
    }

    @Override
    public long ttl() {
        if(this.dirty)
            return -1;

        return this.expiration - System.currentTimeMillis();
    }

    @Override
    public void recache(T object, long ttl) {
        this.content = object;

        this.dirty = false;
        this.cachedAt = System.currentTimeMillis();
        this.expiration = this.cachedAt + ttl;
        this.ttl = ttl;
    }

    @Override
    public void recache(T object) {
        this.recache(object, this.ttl);
    }

    @Override
    public boolean isDirty() {
        return this.dirty || this.ttl() < 0;
    }

    @Override
    public T get() {
        return this.content;
    }

    @Override
    public void makeDirty() {
        this.dirty = true;
    }
}
