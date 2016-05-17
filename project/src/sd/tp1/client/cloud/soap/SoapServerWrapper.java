package sd.tp1.client.cloud.soap;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.client.cloud.LoggedAbstractServer;
import sd.tp1.client.cloud.Server;
import sd.tp1.client.cloud.aux.SafeInvoker;
import sd.tp1.client.cloud.soap.stubs.*;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by apontes on 3/25/16.
 */
public class SoapServerWrapper extends LoggedAbstractServer implements Server {

    private SoapServer server;
    private final URL url;
    private String serverId;

    public SoapServerWrapper(URL url){
        super(SoapServerWrapper.class.getSimpleName());
        this.url = url;

        SoapServerService soapServerService = new SoapServerService(url);
        this.server = soapServerService.getSoapServerPort();
    }

    @Override
    public List<Album> loadListOfAlbums() {
        super.loadListOfAlbums();

        List<SharedAlbum> list = SafeInvoker.invoke(this, server::loadListOfAlbums);

        if(list == null)
            return null;

        return server.loadListOfAlbums()
                .stream()
                .<Album>map(AlbumWrapper::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Picture> loadListOfPictures(String album) {
        super.loadListOfPictures(album);

        List<SharedPicture> pictures = SafeInvoker.invoke(this, () ->
                server.loadListOfPictures(album));
        if(pictures == null)
            return null;

        return pictures
                .stream()
                .<Picture>map(PictureWrapper::new)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] loadPictureData(String album, String picture) {
        super.loadPictureData(album, picture);
        return SafeInvoker.invoke(this, () ->
                server.loadPictureData(album, picture));
    }

    @Override
    public boolean createAlbum(Album album) {
        Boolean bool = SafeInvoker.invoke(this, () -> server.createAlbum(new AlbumWrapper(album)));
        return bool != null && bool;
    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        super.uploadPicture(album, picture, data);
        Boolean bool = SafeInvoker.invoke(this, () -> server.uploadPicture(
                    new AlbumWrapper(album),
                    new PictureWrapper(picture),
                    data));

        return bool != null && bool;
    }

    @Override
    public boolean deleteAlbum(Album album) {
        super.deleteAlbum(album);
        Boolean bool = SafeInvoker.invoke(this, () ->
            server.deleteAlbum(new AlbumWrapper(album)));

        return bool != null && bool;
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        super.deletePicture(album, picture);
        Boolean bool = SafeInvoker.invoke(this, () ->
                server.deletePicture(new AlbumWrapper(album), new PictureWrapper(picture)));

        return bool != null && bool;
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public String getServerId() {
        super.getServerId();

        if(serverId != null)
            return serverId;

        serverId = SafeInvoker.invoke(this, () -> this.server.getServerId());
        return serverId;
    }
}
