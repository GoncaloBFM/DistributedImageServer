package sd.tp1.common.protocol.rest.client;

import org.glassfish.jersey.client.ClientConfig;
import sd.tp1.common.data.*;
import sd.tp1.common.protocol.LoggedAbstractEndpoint;
import sd.tp1.common.protocol.SafeInvoker;
import sd.tp1.common.protocol.Endpoint;
import sd.tp1.common.protocol.rest.envelops.DeletePictureEnvelop;
import sd.tp1.common.protocol.rest.envelops.UploadPictureEnvelop;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gbfm on 4/4/16.
 */
public class RestClient implements Endpoint {

    private final URL url;
    private WebTarget target;
    private String serverId;

    private LoggedAbstractEndpoint logger = new LoggedAbstractEndpoint() {
        @Override
        public URL getUrl() {
            return url;
        }
    };

    protected RestClient(URL url, WebTarget target){
        this.url = url;
        this.target = target;
    }

    public RestClient(URL url){
        this.url = url;

        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);

        target = null;
        try {
            target = client.target(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public URL getUrl() {
        return this.url;
    }

    @Override
    public String getServerId() {
        if(serverId != null)
            return serverId;

        serverId = SafeInvoker.invoke(this, () ->
            this.target.path("/getServerId").request().accept(MediaType.APPLICATION_JSON).get(String.class));

        return serverId;
    }

    @Override
    public List<Album> loadListOfAlbums() {
        logger.loadListOfAlbums();
        SharedAlbum[] list = SafeInvoker.invoke(this, () ->
            this.target.path("/getListOfAlbums").request().accept(MediaType.APPLICATION_JSON).get(SharedAlbum[].class));

        return list == null ? null : new LinkedList<>(Arrays.asList(list));
    }

    @Override
    public List<Picture> loadListOfPictures(String album) {
        logger.loadListOfPictures(album);
        Response response = SafeInvoker.invoke(this, () ->
                this.target.path("/getListOfPictures/" + album).request().accept(MediaType.APPLICATION_JSON).buildGet().invoke());

        if (response != null && response.getStatus() == Response.Status.OK.getStatusCode()) {
            return new LinkedList<>(Arrays.asList(response.readEntity(SharedPicture[].class)));
        } else {
            return null;
        }
    }

    @Override
    public byte[] loadPictureData(String album, String picture) {
        logger.loadPictureData(album, picture);
        byte[] bytes = SafeInvoker.invoke(this, () ->
                this.target.path("/getPictureData/" + album + "/" + picture).request().accept(MediaType.APPLICATION_OCTET_STREAM).get(byte[].class));
        return bytes;
    }

    @Override
    public boolean createAlbum(Album album) {
        logger.createAlbum(album);

        SharedAlbum sharedAlbum = new SharedAlbum(album);
        Response response = SafeInvoker.invoke(this, () ->
                target.path("/createAlbum").request().post(Entity.entity(sharedAlbum, MediaType.APPLICATION_JSON)));

        return response != null && response.getStatus() == Response.Status.OK.getStatusCode();

    }

    @Override
    public boolean uploadPicture(Album album, Picture picture, byte[] data) {
        logger.uploadPicture(album, picture, data);
        UploadPictureEnvelop message = new UploadPictureEnvelop(album, picture, data);

        Response response = SafeInvoker.invoke(this, ()->
                this.target.path("/uploadPicture").request().post(Entity.entity(message, MediaType.APPLICATION_JSON)));

        return response != null && response.getStatus() == Response.Status.OK.getStatusCode();
    }

    @Override
    public boolean deleteAlbum(Album album) {
        logger.deleteAlbum(album);

        SharedAlbum sharedAlbum = new SharedAlbum(album);
        Response response = SafeInvoker.invoke(this, () ->
            this.target.path("/deleteAlbum").request().post(Entity.entity(sharedAlbum, MediaType.APPLICATION_JSON)));

        return response != null && response.getStatus() == Response.Status.OK.getStatusCode();
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        logger.deletePicture(album, picture);

        DeletePictureEnvelop message = new DeletePictureEnvelop(album, picture);
        Response response = SafeInvoker.invoke(this, () ->
                this.target.path("/deletePicture").request().post(Entity.entity(message, MediaType.APPLICATION_JSON)));

        return response != null && response.getStatus() == Response.Status.OK.getStatusCode();
    }

    @Override
    public MetadataBundle getMetadata() {
        logger.getMetadata();

        Response response = SafeInvoker.invoke(this, () ->
                this.target.path("/getMetadata").request().accept(MediaType.APPLICATION_JSON).buildGet().invoke());

        if(response != null && response.getStatus() == Response.Status.OK.getStatusCode())
            return response.readEntity(MetadataBundle.class);

        return null;
    }
}
