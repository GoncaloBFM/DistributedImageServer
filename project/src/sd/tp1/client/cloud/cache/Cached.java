package sd.tp1.client.cloud.cache;

/**
 * Created by everyone on 4/5/16.
 */
public interface Cached<T> {
    long cachedAt();
    long ttl();

    void recache(T object, long ttl);
    void recache(T object);

    boolean isDirty();
    T get();

    void makeDirty();
}
