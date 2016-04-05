import sd.tp1.SharedAlbum;
import sd.tp1.client.cloud.rest.RestServerWrapper;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gbfm on 4/4/16.
 */
public class TestMain {
    public static void main(String args[]) {
        RestServerWrapper server = null;
        try {
            server = new RestServerWrapper(new URL("http://localhost:8080/PictureServer"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        server.deleteAlbum(new SharedAlbum("aaaa"));
    }
}
