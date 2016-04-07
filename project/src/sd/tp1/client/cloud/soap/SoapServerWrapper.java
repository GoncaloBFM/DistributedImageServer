package sd.tp1.client.cloud.soap;

import org.glassfish.jersey.internal.util.Producer;
import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.LoggedAbstractServer;
import sd.tp1.client.cloud.Server;
import sd.tp1.client.cloud.soap.stubs.*;

import javax.xml.ws.soap.SOAPFaultException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by apontes on 3/25/16.
 */
public class SoapServerWrapper extends LoggedAbstractServer implements Server {

    private static final Logger LOGGER = Logger.getLogger(SoapServerWrapper.class.getName());
    private static final int MAX_TRIES = 3;
    private static final int WAIT_ON_FAIL = 1000;

    private SoapServer server;
    private final URL url;

    public SoapServerWrapper(URL url){
        super(SoapServerWrapper.class.getSimpleName());
        this.url = url;

        SoapServerService soapServerService = new SoapServerService(url);
        this.server = soapServerService.getSoapServerPort();
    }

    private <T> T safeInvoke(Producer<T> producer){
        for(int i = 0; i < MAX_TRIES; i++){
            try {
                return producer.call();
            }
            catch(SOAPFaultException e){
                LOGGER.info("Server call failed! Retrying... :" + url);

                try {
                    Thread.sleep(WAIT_ON_FAIL);
                } catch (InterruptedException e1) {
                    //Do nothing
                }
            }
        }

        LOGGER.severe("Server error: " + url);
        return null;
    }

    @Override
    public List<Album> getListOfAlbums() {
        super.getListOfAlbums();

        List<SharedAlbum> list = safeInvoke(server::getListOfAlbums);

        if(list == null)
            return null;

        return server.getListOfAlbums()
                .stream()
                .<Album>map(a -> new AlbumWrapper(a))
                .collect(Collectors.toList());
    }

    @Override
    public List<Picture> getListOfPictures(Album album) {
        super.getListOfPictures(album);

        List<SharedPicture> pictures = safeInvoke(() -> server.getListOfPictures(new AlbumWrapper(album)));
        if(pictures == null)
            return null;

        return pictures
                .stream()
                .<Picture>map(p -> new PictureWrapper(p))
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getPictureData(Album album, Picture picture) {
        super.getPictureData(album, picture);
        return safeInvoke(() -> server.getPictureData(new AlbumWrapper(album), new PictureWrapper(picture)));
    }

    @Override
    public Album createAlbum(String name) {
        SharedAlbum album = safeInvoke(() -> server.createAlbum(name));
        if(album == null)
            return null;

        return new AlbumWrapper(album);
    }

    @Override
    public Picture uploadPicture(Album album, String name, byte[] data) {
        super.uploadPicture(album, name, data);
        SharedPicture picture = safeInvoke(() -> server.uploadPicture(
                new AlbumWrapper(album),
                name,
                data
        ));

        if(picture == null)
            return null;

        return new PictureWrapper(picture);
    }

    @Override
    public void deleteAlbum(Album album) {
        super.deleteAlbum(album);
        safeInvoke(() -> {server.deleteAlbum(new AlbumWrapper(album)); return null; });
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        super.deletePicture(album, picture);
        Boolean bool = safeInvoke(() -> server.deletePicture(new AlbumWrapper(album), new PictureWrapper(picture)));
        if(bool == null)
            return false;

        return bool;
    }

    @Override
    public URL getUrl() {
        return this.url;
    }
}
