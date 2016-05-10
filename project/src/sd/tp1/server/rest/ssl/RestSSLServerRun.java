package sd.tp1.server.rest.ssl;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import sd.tp1.server.HeartbeatAnnouncer;
import sd.tp1.server.ServiceAnnouncer;
import sd.tp1.server.rest.RestServer;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.io.*;
import java.net.URI;
import java.nio.file.NotDirectoryException;
import java.security.*;

/**
 * Created by gbfm on 3/29/16.
 */
public class RestSSLServerRun {
    private static final String SERVICE_TO_ANNOUNCE = "42845_43178_REST_SSL";
    private static final int ANNOUNCE_ON_PORT = 6967;

    private static final int MIN_PORT = 1024; //1024;
    private static final int MAX_PORT = 65535; //65535;

    private static final int DEFAULT_PORT = generateRandomPort();
    private static final String DEFAULT_ROOT = ".";
    private static final String DEFAULT_SERVICE_PATH = "PictureServer";

    private static final File KEYSTORE = new File("keys/keystore.jks");
    private static final char[] JKS_PASSWORD = "abc123def".toCharArray();
    private static final char[] KEY_PASSWORD = "abc123def".toCharArray();


    public static void main(String args[]) {
        File root = new File((args.length >= 1) ? args[0] : DEFAULT_ROOT);
        String serverPath = (args.length >= 2) ? args[1] : DEFAULT_SERVICE_PATH;
        int port = (args.length >= 3) ? Integer.parseInt(args[2]) : DEFAULT_PORT;

        System.out.println(String.format("Server started at port %s, root:%s, path:%s", port, root.getAbsoluteFile(), serverPath));

        URI baseUri = UriBuilder.fromUri("https://0.0.0.0/").port(port).build();
        ResourceConfig config = new ResourceConfig();
        RestServer server = null;
        try {
            server = new RestServer(serverPath, root);
        } catch (NotDirectoryException e) {
            System.err.println("Directory not available.\n Terminating.");
            System.exit(1);
        }
        config.register(server);

        try{
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            KeyStore keyStore = KeyStore.getInstance("JKS");

            InputStream is = new FileInputStream(KEYSTORE);
            keyStore.load(is, JKS_PASSWORD);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, KEY_PASSWORD);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        JdkHttpServerFactory.createHttpServer(baseUri, config);

        ServiceAnnouncer serviceAnnouncer = new HeartbeatAnnouncer(SERVICE_TO_ANNOUNCE,ANNOUNCE_ON_PORT, serverPath, port);
        serviceAnnouncer.startAnnounceService();

        System.out.println("Service announcer started! ;)");
    }

    static int generateRandomPort(){
        return (int) (Math.random() * (MAX_PORT - MIN_PORT) + MIN_PORT);
    }
}
