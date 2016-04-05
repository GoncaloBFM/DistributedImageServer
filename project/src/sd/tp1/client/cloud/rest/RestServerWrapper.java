package sd.tp1.client.cloud.rest;

import org.glassfish.jersey.client.ClientConfig;
import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.SharedAlbum;
import sd.tp1.SharedPicture;
import sd.tp1.client.cloud.Server;

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
public class RestServerWrapper implements Server {

    private final URL url;
    private WebTarget target;

    public RestServerWrapper(URL url){
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
        SharedAlbum[] list = this.target.path("/getListOfAlbums").request().accept(MediaType.APPLICATION_JSON).get(SharedAlbum[].class);
        return new LinkedList<>(Arrays.asList(list));
    }

    @Override
    public List<Picture> getListOfPictures(Album album) {
        SharedPicture[] list = this.target.path("/getListOfPictures/" + album.getName()).request().accept(MediaType.APPLICATION_JSON).get(SharedPicture[].class);
        return new LinkedList<>(Arrays.asList(list));
    }

    @Override
    public byte[] getPictureData(Album album, Picture picture) {
        byte[] bytes  = this.target.path("/getPictureData/" + album.getName() + "/" + picture.getPictureName()).request().accept(MediaType.APPLICATION_OCTET_STREAM).get(byte[].class);
        return bytes;
    }

    @Override
    public Album createAlbum(String name) {
        Response response = target.path("/createAlbum/" + name).request().post(Entity.entity(name, MediaType.APPLICATION_JSON));
        return new SharedAlbum(name);
    }

    @Override
    public Picture uploadPicture(Album album, String name, byte[] data) {
        Response response = this.target.path("/uploadPicture/" + album.getName() + "/" + name).request().post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        return new SharedPicture();
    }

    @Override
    public void deleteAlbum(Album album) {
        this.target.path("/deleteAlbum/"+album.getName()).request().delete();
    }

    @Override
    public boolean deletePicture(Album album, Picture picture) {
        Response response = this.target.path("/deletePicture/" + album.getName() + "/" + picture.getPictureName()).request().delete();
        return response.getStatus() == Response.Status.ACCEPTED.getStatusCode();
    }
}

