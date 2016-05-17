package sd.tp1.client.cloud.rest;

import org.glassfish.jersey.client.ClientConfig;
import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
import sd.tp1.client.cloud.LoggedAbstractServer;
import sd.tp1.client.cloud.Server;
import sd.tp1.client.cloud.aux.SafeInvoker;
import sd.tp1.common.rest_envelops.DeletePictureEnvelop;
import sd.tp1.common.rest_envelops.UploadPictureEnvelop;

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
public class RestServerWrapper extends LoggedAbstractServer implements Server {

    private final URL url;
    private WebTarget target;
    private String serverId;

    protected RestServerWrapper(String loggerTag, URL url, WebTarget target){
        super(loggerTag);
        this.url = url;
        this.target = target;
    }

    protected RestServerWrapper(URL url, WebTarget target){
        this(RestServerWrapper.class.getSimpleName(), url, target);
    }

    public RestServerWrapper(URL url){
        super(RestServerWrapper.class.getSimpleName());
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
        super.loadListOfAlbums();
        SharedAlbum[] list = SafeInvoker.invoke(this, () ->
            this.target.path("/getListOfAlbums").request().accept(MediaType.APPLICATION_JSON).get(SharedAlbum[].class));

        return list == null ? null : new LinkedList<>(Arrays.asList(list));
    }

    @Override
    public List<Picture> loadListOfPictures(String album) {
        super.loadListOfPictures(album);
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
        super.loadPictureData(album, picture);
        byte[] bytes = SafeInvoker.invoke(this, () ->
                this.target.path("/getPictureData/" + album + "/" + picture).request().accept(MediaType.APPLICATION_OCTET_STREAM).get(byte[].class));
        return bytes;
    }

    @Override
    public void createAlbum(Album sharedAlbum) {
        super.createAlbum(sharedAlbum);
        SafeInvoker.invoke(this, () ->
                target.path("/createAlbum/" + sharedAlbum.getName()).request().post(Entity.entity(sharedAlbum, MediaType.APPLICATION_JSON)));

    }

    @Override
    public void uploadPicture(Album album, Picture picture, byte[] data) {
        super.uploadPicture(album, picture, data);
        UploadPictureEnvelop message = new UploadPictureEnvelop(album, picture, data);
        SafeInvoker.invoke(this, ()->
                this.target.path("/uploadPicture").request().post(Entity.entity(message, MediaType.APPLICATION_JSON)));

    }

    @Override
    public void deleteAlbum(Album album) {
        super.deleteAlbum(album);

        SafeInvoker.invoke(this, () -> {
            this.target.path("/deleteAlbum").request().post(Entity.entity(album, MediaType.APPLICATION_JSON));
            return null;
        });

    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        super.deletePicture(album, picture);

        DeletePictureEnvelop message = new DeletePictureEnvelop(album, picture);
        Response response = SafeInvoker.invoke(this, () ->
                this.target.path("/deletePicture").request().post(Entity.entity(message, MediaType.APPLICATION_JSON)));

        return response != null && response.getStatus() == Response.Status.OK.getStatusCode();

    }
}

