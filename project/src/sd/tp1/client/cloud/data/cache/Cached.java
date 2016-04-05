package sd.tp1.client.cloud.data.cache;

/**
 * Created by apontes on 4/3/16.
 */
public interface Cached {
    boolean isDirty();
    long ttl();

    long creation();

    void makeDirty();
}
