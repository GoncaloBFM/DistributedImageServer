package sd.tp1.client.cloud;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.cache.HashPictureCache;
import sd.tp1.client.cloud.cache.PictureCache;

/**
 * Created by apontes on 4/5/16.
 */
public class CacheCloudClient extends CloudClient{

    private PictureCache pictureCache = new HashPictureCache();

    /**
     * Returns the contents of picture in album.
     * On error this method should return null.
     *
     * @param album
     * @param picture
     */
    @Override
    public byte[] getPictureData(Album album, Picture picture) {
        byte[] data = this.pictureCache.get(album, picture);
        if(data != null)
            return data;

        return super.getPictureData(album, picture);
    }

    /**
     * Add a new picture to an album.
     * On error this method should return null.
     *
     * @param album
     * @param name
     * @param data
     */
    @Override
    public Picture uploadPicture(Album album, String name, byte[] data) {
        Picture picture = super.uploadPicture(album, name, data);
        if(picture != null)
            this.pictureCache.put(album, picture, data);

        return picture;
    }

    /**
     * Delete a picture from an album.
     * On error this method should return false.
     *
     * @param album
     * @param picture
     */
    @Override
    public boolean deletePicture(Album album, Picture picture) {
        if(super.deletePicture(album, picture)){
            this.pictureCache.clear(album, picture);
            return true;
        }

        return false;
    }
}
