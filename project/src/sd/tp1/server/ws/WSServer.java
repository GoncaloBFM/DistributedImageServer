package sd.tp1.server.ws;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.SharedAlbum;
import sd.tp1.SharedPicture;
import sd.tp1.server.Server;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by apontes on 3/21/16.
 */
@WebService
public class WSServer {

    @WebMethod
    public List<Album> getListOfAlbums() {
        return null;
    }

    @WebMethod
    public List<SharedPicture> getListOfPictures(SharedAlbum album) {
        return null;
    }

    @WebMethod
    public byte[] getPictureData(SharedAlbum album, SharedPicture picture) {
        return new byte[0];
    }

    @WebMethod
    public SharedAlbum createAlbum(String name) {
        return null;
    }

    @WebMethod
    public SharedPicture uploadPicture(SharedAlbum album, String name, byte[] data) {
        return null;
    }

    @WebMethod
    public void deleteAlbum(SharedAlbum album) {

    }

    @WebMethod
    public boolean deletePicture(SharedAlbum album, SharedPicture picture) {
        return false;
    }
}
