package sd.tp1.common.protocol.soap.client;

import sd.tp1.common.data.Album;
import sd.tp1.common.data.Picture;
import sd.tp1.common.protocol.LoggedAbstractEndpoint;
import sd.tp1.common.protocol.SafeInvoker;
import sd.tp1.common.protocol.Endpoint;
import sd.tp1.common.protocol.soap.client.stubs.*;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by apontes on 3/25/16.
 */
public class SoapClient extends LoggedAbstractEndpoint implements Endpoint {

    private SoapServer server;
    private final URL url;
    private String serverId;

    public SoapClient(URL url){
        super(SoapClient.class.getSimpleName());
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
                .<Album>map(SoapAlbumWrapper::new)
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
                .<Picture>map(SoapPictureWrapper::new)
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
        Boolean bool = SafeInvoker.invoke(this, () -> server.createAlbum(new SoapAlbumWrapper(album)));
        return bool != null && bool;
    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        super.uploadPicture(album, picture, data);
        Boolean bool = SafeInvoker.invoke(this, () -> server.uploadPicture(
                    new SoapAlbumWrapper(album),
                    new SoapPictureWrapper(picture),
                    data));

        return bool != null && bool;
    }

    @Override
    public boolean deleteAlbum(Album album) {
        super.deleteAlbum(album);
        Boolean bool = SafeInvoker.invoke(this, () ->
            server.deleteAlbum(new SoapAlbumWrapper(album)));

        return bool != null && bool;
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        super.deletePicture(album, picture);
        Boolean bool = SafeInvoker.invoke(this, () ->
                server.deletePicture(new SoapAlbumWrapper(album), new SoapPictureWrapper(picture)));

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
