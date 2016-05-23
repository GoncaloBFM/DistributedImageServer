package sd.tp1.client.cloud.cache;

import java.io.File;

/**
 * Created by everyone on 4/5/16.
 */
public interface CachedBlob extends Cached<byte[]> {
    int length();

    void swap();
    boolean isOnRam();
    boolean isOnDisk();
}
