package sd.tp1.client.cloud.soap;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.Server;
import sd.tp1.client.cloud.soap.stubs.IOException_Exception;
import sd.tp1.client.cloud.soap.stubs.SoapServer;
import sd.tp1.client.cloud.soap.stubs.SoapServerService;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by apontes on 3/25/16.
 */
public class SoapServerWrapper implements Server {

    SoapServer server;

    public SoapServerWrapper(URL url){
        SoapServerService soapServerService = new SoapServerService(url);
        this.server = soapServerService.getSoapServerPort();
    }

    @Override
    public List<Album> getListOfAlbums() {
        return server.getListOfAlbums()
                .stream()
                .<Album>map(a -> new AlbumWrapper(a))
                .collect(Collectors.toList());
    }

    @Override
    public List<Picture> getListOfPictures(Album album) {
        return server.getListOfPictures(new AlbumWrapper(album))
                .stream()
                .<Picture>map(p -> new PictureWrapper(p))
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getPictureData(Album album, Picture picture) {
        try {
            return server.getPictureData(new AlbumWrapper(album), new PictureWrapper(picture));
        } catch (IOException_Exception e) {
            //TODO something
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Album createAlbum(String name) {
        try {
            return new AlbumWrapper(server.createAlbum(name));
        } catch (IOException_Exception e) {
            //TODO something
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Picture uploadPicture(Album album, String name, byte[] data) {
        try {
            return new PictureWrapper(server.uploadPicture(
                    new AlbumWrapper(album),
                    name,
                    data
            ));

        } catch (IOException_Exception e) {
            //TODO something
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteAlbum(Album album) {
        try {
            server.deleteAlbum(new AlbumWrapper(album));
        } catch (IOException_Exception e) {
            //TODO something
            e.printStackTrace();
        }
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        try {
            return server.deletePicture(new AlbumWrapper(album), new PictureWrapper(picture));
        } catch (IOException_Exception e) {
            //TODO something
            e.printStackTrace();
        }

        return false;
    }
}
