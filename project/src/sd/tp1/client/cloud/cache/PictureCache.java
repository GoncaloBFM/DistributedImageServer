package sd.tp1.client.cloud.cache;

import sd.tp1.Album;
import sd.tp1.Picture;

/**
 * Created by apontes on 4/5/16.
 */
public interface PictureCache {
    byte[] get(Album album, Picture picture);
    void put(Album album, Picture picture, byte[] content);

    void clear(Album album, Picture picture);
    void clear();

    long length();
    int  lengthOnRam();
    long lengthOnDisk();
}
