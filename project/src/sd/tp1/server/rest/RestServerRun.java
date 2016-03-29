package sd.tp1.server.rest;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import sd.tp1.server.soap.SoapServer;

import javax.ws.rs.core.UriBuilder;
import javax.xml.ws.Endpoint;
import java.io.File;
import java.net.URI;
import java.nio.file.NotDirectoryException;

/**
 * Created by gbfm on 3/29/16.
 */
public class RestServerRun {
    private static final int MIN_PORT = 8080; //1024;
    private static final int MAX_PORT = 8080; //65535;

    private static final int DEFAULT_PORT = generateRandomPort();
    private static final String DEFAULT_ROOT = ".";
    private static final String DEFAULT_SERVICE_PATH = "PictureServer";


    public static void main(String args[]) {
        File root = new File((args.length >= 1) ? args[0] : DEFAULT_ROOT);
        String serverPath = (args.length >= 2) ? args[1] : DEFAULT_SERVICE_PATH;
        int port = (args.length >= 3) ? Integer.parseInt(args[2]) : DEFAULT_PORT;

        System.err.println(String.format("Server started at port %s, root:%s, path:%s", port, root.getAbsoluteFile(), serverPath));

        URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(port).build();
        ResourceConfig config = new ResourceConfig();
        config.register(RestServer.class);
        JdkHttpServerFactory.createHttpServer(baseUri, config);
    }

    static int generateRandomPort(){
        return (int) (Math.random() * (MAX_PORT - MIN_PORT) + MIN_PORT);
    }
}