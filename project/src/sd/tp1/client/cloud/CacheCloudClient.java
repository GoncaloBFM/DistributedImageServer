package sd.tp1.client.cloud;

import sd.tp1.common.data.Album;
import sd.tp1.common.data.Picture;
import sd.tp1.client.cloud.cache.CachedServer;
import sd.tp1.client.cloud.cache.HashPictureCache;
import sd.tp1.client.cloud.cache.PictureCache;

/**
 * Created by apontes on 4/5/16.
 */
public class CacheCloudClient extends CloudClient{

    private PictureCache pictureCache = new HashPictureCache();

    CacheCloudClient(){
        super(false);

        HashServerManager.getServerManager().addServerHandler(new ServerHandler() {
            @Override
            public void serverAdded(Server server) {
                gui.updateAlbums();
            }

            @Override
            public void serverLost(Server server) {
                try{
                    for(Album album: ((CachedServer) server).getCachedListOfAlbums())
                        gui.updateAlbum(album);
                }
                catch (ClassCastException e){
                    gui.updateAlbums();
                }
            }
        });
    }

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

        data = super.getPictureData(album, picture);
        this.pictureCache.put(album, picture, data);

        return data;
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
