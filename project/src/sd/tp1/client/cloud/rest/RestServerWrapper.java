package sd.tp1.client.cloud.rest;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.internal.util.Producer;
import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.SharedAlbum;
import sd.tp1.SharedPicture;
import sd.tp1.client.cloud.LoggedAbstractServer;
import sd.tp1.client.cloud.Server;
import sd.tp1.client.cloud.aux.SafeInvoker;
import sd.tp1.server.rest.RestServer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.soap.SOAPFaultException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by gbfm on 4/4/16.
 */
public class RestServerWrapper extends LoggedAbstractServer implements Server {

    private final URL url;
    private WebTarget target;

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
    public List<Album> getListOfAlbums() {
        super.getListOfAlbums();
        SharedAlbum[] list = SafeInvoker.invoke(this, () ->
            this.target.path("/getListOfAlbums").request().accept(MediaType.APPLICATION_JSON).get(SharedAlbum[].class));

        return list == null ? null : new LinkedList<>(Arrays.asList(list));
    }

    @Override
    public List<Picture> getListOfPictures(Album album) {
        super.getListOfPictures(album);
        Response response = SafeInvoker.invoke(this, () ->
                this.target.path("/getListOfPictures/" + album.getName()).request().accept(MediaType.APPLICATION_JSON).buildGet().invoke());

        if (response != null && response.getStatus() == Response.Status.OK.getStatusCode()) {
            return new LinkedList<>(Arrays.asList(response.readEntity(SharedPicture[].class)));
        } else {
            return null;
        }
    }

    @Override
    public byte[] getPictureData(Album album, Picture picture) {
        super.getPictureData(album, picture);
        byte[] bytes = SafeInvoker.invoke(this, () ->
                this.target.path("/getPictureData/" + album.getName() + "/" + picture.getPictureName()).request().accept(MediaType.APPLICATION_OCTET_STREAM).get(byte[].class));
        return bytes;
    }

    @Override
    public Album createAlbum(String name) {
        super.createAlbum(name);
        Response response = SafeInvoker.invoke(this, () ->
                target.path("/createAlbum/" + name).request().post(Entity.entity(name, MediaType.APPLICATION_JSON)));

        if (response != null && response.getStatus() == Response.Status.OK.getStatusCode()) {
            return new SharedAlbum(name);
        }
        return null;
    }

    @Override
    public Picture uploadPicture(Album album, String name, byte[] data) {
        super.uploadPicture(album, name, data);
        Response response = SafeInvoker.invoke(this, ()->
                this.target.path("/uploadPicture/" + album.getName() + "/" + name).request().post(Entity.entity(data, MediaType.APPLICATION_JSON)));

        if (response != null && response.getStatus() == Response.Status.OK.getStatusCode()) {
            return new SharedPicture(name);
        }
        return null;
    }

    @Override
    public void deleteAlbum(Album album) {
        super.deleteAlbum(album);

        SafeInvoker.invoke(this, () -> {
            this.target.path("/deleteAlbum/"+album.getName()).request().delete();
            return null;
        });

    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        super.deletePicture(album, picture);

        Response response = SafeInvoker.invoke(this, () ->
                this.target.path("/deletePicture/" + album.getName() + "/" + picture.getPictureName()).request().delete());

        return response != null && response.getStatus() == Response.Status.OK.getStatusCode();

    }
}

