package sd.tp1.server.soap;

import sd.tp1.SharedAlbum;
import sd.tp1.SharedPicture;
import sd.tp1.server.DataManager;
import sd.tp1.server.FileDataManager;
import sd.tp1.server.HeartbeatAnnouncer;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.List;

/**
 * Created by apontes on 3/21/16.
 */
@WebService
public class SoapServer {

    //TODO Assure location, and make it make sense
    //public static String SERVICE_TO_ANNOUNCE = "42845_43178_Server";
    public static String SERVICE_TO_ANNOUNCE = "http://localhost:8080/PictureServer";
    private DataManager dataManager;

    public SoapServer() throws NotDirectoryException {
        this.dataManager = new FileDataManager();
    }

    public SoapServer(File root) throws NotDirectoryException {
        this.dataManager = new FileDataManager(root);
        HeartbeatAnnouncer announcer = new HeartbeatAnnouncer(SERVICE_TO_ANNOUNCE);
        announcer.announceService();

    }

    @WebMethod
    public List<SharedAlbum> getListOfAlbums() {
        return dataManager.loadListOfAlbums();
    }

    @WebMethod
    public List<SharedPicture> getListOfPictures(SharedAlbum album) {
        return dataManager.loadListOfPictures(album);

    }

    @WebMethod
    public byte[] getPictureData(SharedAlbum album, SharedPicture picture){
        return dataManager.loadPictureData(album, picture);
    }

    @WebMethod
    public SharedAlbum createAlbum(String name){
        return dataManager.createAlbum(name);
    }

    @WebMethod
    public SharedPicture uploadPicture(SharedAlbum album, String name, byte[] data){
        return dataManager.uploadPicture(album, name, data);
    }

    @WebMethod
    public void deleteAlbum(SharedAlbum album){
        dataManager.deleteAlbum(album);
    }

    @WebMethod
    public boolean deletePicture(SharedAlbum album, SharedPicture picture) {
        return dataManager.deletePicture(album, picture);
    }
}
