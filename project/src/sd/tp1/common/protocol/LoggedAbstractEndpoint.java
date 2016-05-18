package sd.tp1.common.protocol;

import sd.tp1.client.cloud.Server;
import sd.tp1.common.data.Album;
import sd.tp1.common.data.MetadataBundle;
import sd.tp1.common.data.Picture;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by gbfm on 4/7/16.
 */
public abstract class LoggedAbstractEndpoint implements Endpoint {

    private static final String LOG_TAG = "Endpoint";

    private final Logger logger;

    public LoggedAbstractEndpoint(){
        this(LOG_TAG);
    }

    public LoggedAbstractEndpoint(String logTag) {
        this.logger =  Logger.getLogger(logTag);
    }

    @Override
    public List<Album> loadListOfAlbums() {
        logger.info("getListOfAlbums");
        return null;
    }

    @Override
    public List<Picture> loadListOfPictures(String album) {
        logger.info("getListOfPictures(album="+album+")");
        return null;
    }

    @Override
    public byte[] loadPictureData(String album, String picture) {
        logger.info("getPictureData(album=" + album +", picture=" + picture+")");
        return null;
    }

    @Override
    public boolean createAlbum(Album name) {
        logger.info("createAlbum(album=" + name.getName()+")");
        return false;
    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        logger.info("uploadPicture(album=" + album.getName() +", picture=" + picture.getPictureName()+")");
        return false;
    }

    @Override
    public boolean deleteAlbum(Album album) {
        logger.info("deleteAlbum(album=" + album.getName()+")");
        return false;
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        logger.info("deletePicture(album=" + album.getName()+", picture=" + picture.getPictureName()+")");
        return false;
    }

    @Override
    public String getServerId() {
        logger.info("getServerId()");
        return null;
    }

    @Override
    public MetadataBundle getMetadata(){
        logger.info("getMetadata()");
        return null;
    }
}
