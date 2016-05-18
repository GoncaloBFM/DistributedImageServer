package sd.tp1.server;

import sd.tp1.common.data.DataManager;
import sd.tp1.common.protocol.EndpointServer;
import sd.tp1.common.protocol.rest.ssl.server.RestSSLServer;
import sd.tp1.server.data.FileDataManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.core.UriBuilder;
import java.io.*;
import java.net.URI;
import java.nio.file.NotDirectoryException;

/**
 * Created by gbfm on 3/29/16.
 */
public class RestSSLServerRun extends ServerRunner {

    private static final int DEFAULT_PORT = generateRandomPort();
    private static final String DEFAULT_ROOT = ".";
    private static final String DEFAULT_SERVICE_PATH = "PictureServer";

    private static final File KEYSTORE = new File("keys/keystore.jks");
    private static final char[] JKS_PASSWORD = "abc123def".toCharArray();
    private static final char[] KEY_PASSWORD = "abc123def".toCharArray();

    public RestSSLServerRun(DataManager dataManager, EndpointServer server) {
        super(dataManager, server);
        throw new NotImplementedException();
    }

    public static void main(String args[]) {
        File root = new File((args.length >= 1) ? args[0] : DEFAULT_ROOT);
        String serverPath = (args.length >= 2) ? args[1] : DEFAULT_SERVICE_PATH;
        int port = (args.length >= 3) ? Integer.parseInt(args[2]) : DEFAULT_PORT;

        URI baseUri = UriBuilder.fromUri("https://0.0.0.0/").port(port).build();

        try {
            DataManager dataManager = new FileDataManager(root);

            try{
                RestSSLServer server = new RestSSLServer(serverPath, baseUri, dataManager, KEYSTORE, JKS_PASSWORD, KEY_PASSWORD);

                ServerRunner serverRunner = new ServerRunner(dataManager, server);
                serverRunner.start();

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }

        } catch (NotDirectoryException e) {
            System.err.println("Directory not available.\n Terminating.");
            System.exit(1);
        }
    }
}