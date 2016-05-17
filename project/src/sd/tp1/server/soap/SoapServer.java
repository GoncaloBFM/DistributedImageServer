package sd.tp1.server.soap;

import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
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

    private DataManager dataManager;

    public SoapServer() throws NotDirectoryException {
        this.dataManager = new FileDataManager();
    }

    public SoapServer(File root) throws NotDirectoryException {
        this.dataManager = new FileDataManager(root);
    }

    public SoapServer(DataManager dataManager){
        this.dataManager = dataManager;
    }



    @WebMethod
    public List<SharedPicture> loadListOfPictures(String album) {
        logger.info("getListOfPictures" + "(album=" + album+")");
        return dataManager.loadListOfPictures(album);

    }

    @WebMethod
    public List<SharedAlbum> loadListOfAlbums() {
        logger.info("getListOfAlbums");
        return dataManager.loadListOfAlbums();
    }

    @WebMethod
    public byte[] loadPictureData(String album, String picture){
        logger.info("getPictureData"+"(album=" + album+", picture=" + picture + ")");
        return dataManager.loadPictureData(album, picture);
    }

    @WebMethod
    public void createAlbum(SharedAlbum album){
        logger.info("createAlbum" + "(album=" + album.getName() + ")");
        dataManager.createAlbum(album);
    }

    @WebMethod
    public void uploadPicture(SharedAlbum album, SharedPicture picture, byte[] data){
        logger.info("uploadPicture" + "(album=" + album.getName()+", picture=" + picture.getPictureName()+")");
        dataManager.uploadPicture(album, picture, data);
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

    @WebMethod
    public String getServerId(){
        return dataManager.getServerId();
    }
}
