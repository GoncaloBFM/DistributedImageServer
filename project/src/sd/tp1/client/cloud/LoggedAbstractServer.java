package sd.tp1.client.cloud;

import org.glassfish.jersey.internal.util.Producer;
import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.Server;

import javax.xml.ws.soap.SOAPFaultException;
import java.net.URL;
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
    public List<Album> getListOfAlbums() {
        logger.info(caller+".getListOfAlbums");
        return null;
    }

    @Override
    public List<Picture> getListOfPictures(Album album) {
        logger.info(caller+".getListOfPictures(album="+album.getName()+")");
        return null;
    }

    @Override
    public byte[] getPictureData(Album album, Picture picture) {
        logger.info(caller+".getPictureData(album=" + album.getName() +", picture=" + picture.getPictureName()+")");
        return null;
    }

    @Override
    public Album createAlbum(String name) {
        logger.info(caller+".createAlbum(album=" + name+")");
        return null;
    }

    @Override
    public Picture uploadPicture(Album album, String name, byte[] data) {
        logger.info(caller+".uploadPicture(album=" + album.getName() +", picture=" + name+")");
        return null;
    }

    @Override
    public void deleteAlbum(Album album) {
        logger.info(caller+".deleteAlbum(album=" + album.getName()+")");

    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        logger.info(caller+".uploadPicture(album=" + album.getName()+", picture=" + picture.getPictureName()+")");
        return false;
    }
}
