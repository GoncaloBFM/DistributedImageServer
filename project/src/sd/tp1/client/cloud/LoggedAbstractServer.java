package sd.tp1.client.cloud;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedPicture;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by gbfm on 4/7/16.
 */
public abstract class LoggedAbstractServer implements Server {

    private final Logger logger;
    private final String caller;


    public LoggedAbstractServer(String caller) {
        this.logger =  Logger.getLogger(caller);
        this.caller = caller;
    }

    @Override
    public List<Album> loadListOfAlbums() {
        logger.info(caller+".getListOfAlbums");
        return null;
    }

    @Override
    public List<Picture> loadListOfPictures(String album) {
        logger.info(caller+".getListOfPictures(album="+album+")");
        return null;
    }

    @Override
    public byte[] loadPictureData(String album, String picture) {
        logger.info(caller+".getPictureData(album=" + album +", picture=" + picture+")");
        return null;
    }

    @Override
    public boolean createAlbum(Album name) {
        logger.info(caller+".createAlbum(album=" + name.getName()+")");
        return false;
    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        logger.info(caller+".uploadPicture(album=" + album.getName() +", picture=" + picture.getPictureName()+")");
        return false;
    }

    @Override
    public boolean deleteAlbum(Album album) {
        logger.info(caller+".deleteAlbum(album=" + album.getName()+")");
        return false;
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        logger.info(caller+".deletePicture(album=" + album.getName()+", picture=" + picture.getPictureName()+")");
        return false;
    }

    @Override
    public String getServerId() {
        logger.info(caller+".getServerId()");
        return null;
    }
}
