package sd.tp1.common.protocol.soap.server;

import sd.tp1.common.data.SharedAlbum;
import sd.tp1.common.data.SharedPicture;
import sd.tp1.common.data.DataManager;
import sd.tp1.common.protocol.EndpointServer;
import sd.tp1.common.protocol.LoggedAbstractEndpoint;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.net.URL;
import java.util.List;

/**
 * Created by apontes on 3/21/16.
 */
@WebService
public class SoapServer implements EndpointServer{

    public static final String SERVER_TYPE = "SOAP";

    private LoggedAbstractEndpoint logger = new LoggedAbstractEndpoint() {
        @Override
        public URL getUrl() {
            return url;
        }
    };

    private DataManager dataManager;
    private Endpoint server;
    private URL url;

    public SoapServer(){
        throw new NotImplementedException();
    }

    public SoapServer(DataManager dataManager, URL url){
        this.dataManager = dataManager;
        this.url = url;
    }

    @WebMethod
    public List<SharedPicture> loadListOfPictures(String album) {
        logger.loadListOfPictures(album);
        return dataManager.loadListOfPictures(album);
    }

    @WebMethod
    public List<SharedAlbum> loadListOfAlbums() {
        logger.loadListOfAlbums();
        return dataManager.loadListOfAlbums();
    }

    @WebMethod
    public byte[] loadPictureData(String album, String picture){
        logger.loadPictureData(album, picture);
        return dataManager.loadPictureData(album, picture);
    }

    @WebMethod
    public boolean createAlbum(SharedAlbum album){
        logger.createAlbum(album);
        return dataManager.createAlbum(album);
    }

    @WebMethod
    public boolean uploadPicture(SharedAlbum album, SharedPicture picture, byte[] data){
        logger.uploadPicture(album, picture, data);
        return dataManager.uploadPicture(album, picture, data);
    }

    @WebMethod
    public boolean deleteAlbum(SharedAlbum album){
        logger.deleteAlbum(album);
        return dataManager.deleteAlbum(album);
    }

    @WebMethod
    public boolean deletePicture(SharedAlbum album, SharedPicture picture) {
        logger.deletePicture(album, picture);
        return dataManager.deletePicture(album, picture);
    }

    @WebMethod
    public String getServerId(){
        logger.getServerId();
        return dataManager.getServerId();
    }

    @Override
    public void start() {
        if(isRunning())
            return;

        this.server = Endpoint.publish(this.url.toString(), this);
    }

    @Override
    public void stop() {
        if(!isRunning())
            return;

        this.server.stop();
        this.server = null;
    }

    @Override
    public boolean isRunning() {
        return this.server != null;
    }

    @Override
    public String getType() {
        return SERVER_TYPE;
    }

    @Override
    public URL getUrl() {
        return url;
    }
}
