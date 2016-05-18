package sd.tp1.common.protocol.soap.client;

import sd.tp1.common.data.*;
import sd.tp1.common.data.MetadataBundle;
import sd.tp1.common.data.SharedAlbumPicture;
import sd.tp1.common.protocol.LoggedAbstractEndpoint;
import sd.tp1.common.protocol.SafeInvoker;
import sd.tp1.common.protocol.Endpoint;
import sd.tp1.common.protocol.soap.client.stubs.*;
import sd.tp1.common.protocol.soap.client.stubs.SharedAlbum;
import sd.tp1.common.protocol.soap.client.stubs.SharedPicture;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by apontes on 3/25/16.
 */
public class SoapClient implements Endpoint {

    private LoggedAbstractEndpoint logger = new LoggedAbstractEndpoint(SoapClient.class.getSimpleName()) {
        @Override
        public URL getUrl() {
            return null;
        }
    };

    private SoapServer server;
    private final URL url;
    private String serverId;

    public SoapClient(URL url){
        this.url = url;

        SoapServerService soapServerService = new SoapServerService(url);
        this.server = soapServerService.getSoapServerPort();
    }

    @Override
    public List<Album> loadListOfAlbums() {
        logger.loadListOfAlbums();

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
        logger.loadListOfPictures(album);

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
        logger.loadPictureData(album, picture);
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
        logger.uploadPicture(album, picture, data);
        Boolean bool = SafeInvoker.invoke(this, () -> server.uploadPicture(
                    new SoapAlbumWrapper(album),
                    new SoapPictureWrapper(picture),
                    data));

        return bool != null && bool;
    }

    @Override
    public boolean deleteAlbum(Album album) {
        logger.deleteAlbum(album);
        Boolean bool = SafeInvoker.invoke(this, () ->
            server.deleteAlbum(new SoapAlbumWrapper(album)));

        return bool != null && bool;
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        logger.deletePicture(album, picture);
        Boolean bool = SafeInvoker.invoke(this, () ->
                server.deletePicture(new SoapAlbumWrapper(album), new SoapPictureWrapper(picture)));

        return bool != null && bool;
    }

    @Override
    public MetadataBundle getMetadata() {
        logger.getMetadata();

        sd.tp1.common.protocol.soap.client.stubs.MetadataBundle stubMeta = SafeInvoker.invoke(this, () ->
                server.getMetadata());

        List<Album> albumList = stubMeta.getAlbumList()
                .parallelStream()
                .map(x -> new SoapAlbumWrapper(x))
                .collect(Collectors.toList());

        List<AlbumPicture> pictureList = new LinkedList();
        stubMeta.getPictureList().forEach(x -> new SharedAlbumPicture(new SoapAlbumWrapper(x.getAlbum()), new SoapPictureWrapper(x.getPicture())));

        MetadataBundle metadata = new MetadataBundle(albumList, pictureList);

        return metadata;
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public String getServerId() {
        logger.getServerId();

        if(serverId != null)
            return serverId;

        serverId = SafeInvoker.invoke(this, () -> this.server.getServerId());
        return serverId;
    }
}
