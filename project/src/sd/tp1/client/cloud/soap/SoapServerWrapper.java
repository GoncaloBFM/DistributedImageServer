package sd.tp1.client.cloud.soap;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.LoggedAbstractServer;
import sd.tp1.client.cloud.Server;
import sd.tp1.client.cloud.soap.stubs.*;

import java.net.URL;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by apontes on 3/25/16.
 */
public class SoapServerWrapper extends LoggedAbstractServer implements Server {

    private SoapServer server;
    private final URL url;

    public SoapServerWrapper(URL url){
        super(SoapServerWrapper.class.getSimpleName());
        this.url = url;

        SoapServerService soapServerService = new SoapServerService(url);
        this.server = soapServerService.getSoapServerPort();
    }

    @Override
    public List<Album> getListOfAlbums() {
        super.getListOfAlbums();
        return server.getListOfAlbums()
                .stream()
                .<Album>map(a -> new AlbumWrapper(a))
                .collect(Collectors.toList());
    }

    @Override
    public List<Picture> getListOfPictures(Album album) {
        super.getListOfPictures(album);
        List<SharedPicture> pictures = server.getListOfPictures(new AlbumWrapper(album));
        if(pictures == null)
            return null;

        return pictures
                .stream()
                .<Picture>map(p -> new PictureWrapper(p))
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getPictureData(Album album, Picture picture) {
        super.getPictureData(album,picture);
        return server.getPictureData(new AlbumWrapper(album), new PictureWrapper(picture));
    }

    @Override
    public Album createAlbum(String name) {
        super.createAlbum(name);
        SharedAlbum album = server.createAlbum(name);
        return new AlbumWrapper(album);
    }

    @Override
    public Picture uploadPicture(Album album, String name, byte[] data) {
        super.uploadPicture(album,name,data);
        SharedPicture picture = server.uploadPicture(
                new AlbumWrapper(album),
                name,
                data
        );

        if(picture == null)
            return null;

        return new PictureWrapper(picture);
    }

    @Override
    public void deleteAlbum(Album album) {
        super.deleteAlbum(album);
        server.deleteAlbum(new AlbumWrapper(album));
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        super.deletePicture(album, picture);
        return server.deletePicture(new AlbumWrapper(album), new PictureWrapper(picture));
    }

    @Override
    public URL getUrl() {
        return this.url;
    }
}
