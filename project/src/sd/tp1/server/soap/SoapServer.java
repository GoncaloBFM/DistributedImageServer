package sd.tp1.server.soap;

import sd.tp1.SharedAlbum;
import sd.tp1.SharedPicture;
import sd.tp1.server.DataManager;
import sd.tp1.server.FileDataManager;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by apontes on 3/21/16.
 */
@WebService
public class SoapServer {

    private static final Logger logger = Logger.getLogger(SoapServer.class.getSimpleName());

    //TODO Assure location, and make it make sense
    //public static String SERVICE_TO_ANNOUNCE = "42845_43178_Server";
    private DataManager dataManager;

    public SoapServer() throws NotDirectoryException {
        this.dataManager = new FileDataManager();
    }

    public SoapServer(File root) throws NotDirectoryException {
        this.dataManager = new FileDataManager(root);

    }

    @WebMethod
    public List<SharedAlbum> getListOfAlbums() {
        logger.info("getListOfAlbums");
        throw new RuntimeException();

        //return dataManager.loadListOfAlbums();
    }

    @WebMethod
    public List<SharedPicture> getListOfPictures(SharedAlbum album) {
        logger.info("getListOfPictures" + "(album=" + album.getName()+")");
        return dataManager.loadListOfPictures(album);

    }

    @WebMethod
    public byte[] getPictureData(SharedAlbum album, SharedPicture picture){
        logger.info("getPictureData"+"(album=" + album.getName()+", picture=" + picture.getPictureName()+")");
        return dataManager.loadPictureData(album, picture);
    }

    @WebMethod
    public SharedAlbum createAlbum(String name){
        logger.info("createAlbum" + "(album=" + name + ")");
        return dataManager.createAlbum(name);
    }

    @WebMethod
    public SharedPicture uploadPicture(SharedAlbum album, String name, byte[] data){
        logger.info("uploadPicture" + "(album=" + album.getName()+", picture=" + name+")");
        return dataManager.uploadPicture(album, name, data);
    }

    @WebMethod
    public void deleteAlbum(SharedAlbum album){
        logger.info("deleteAlbum" + "(album=" + album.getName()+")");
        dataManager.deleteAlbum(album);
    }

    @WebMethod
    public boolean deletePicture(SharedAlbum album, SharedPicture picture) {
        logger.info("deletePicture" + "(album=" + album.getName()+", picture=" + picture.getPictureName()+")");
        return dataManager.deletePicture(album, picture);
    }
}
