package sd.tp1.client.cloud.rest_ssl;

import org.glassfish.jersey.client.ClientConfig;
import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.SharedAlbum;
import sd.tp1.SharedPicture;
import sd.tp1.client.cloud.LoggedAbstractServer;
import sd.tp1.client.cloud.Server;
import sd.tp1.client.cloud.aux.SafeInvoker;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gbfm on 4/4/16.
 */
public class RestSSLServerWrapper extends LoggedAbstractServer implements Server {

    private final URL url;
    private WebTarget target;

    public RestSSLServerWrapper(URL url) throws NoSuchAlgorithmException, KeyManagementException {
        super(RestSSLServerWrapper.class.getSimpleName());
        this.url = url;

        SSLContext sc = SSLContext.getInstance("TLSv1");

        TrustManager[] trustAllCerts = { new InsecureTrustManager() };
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        Client client = ClientBuilder.newBuilder()
                .hostnameVerifier(new InsecureHostnameVerifier())
                .sslContext(sc)
                .build();

        target = null;
        try {
            target = client.target(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    static public class InsecureHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    static public class InsecureTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
            Arrays.asList( chain ).forEach( i -> {
                System.err.println( "type: " + i.getType() + "from: " + i.getNotBefore() + " to: " + i.getNotAfter() );
            });
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
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

