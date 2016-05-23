package sd.tp1.server;

import sd.tp1.common.data.DataManager;
import sd.tp1.common.protocol.EndpointServer;
import sd.tp1.common.protocol.rest.server.RestServer;
import sd.tp1.server.data.FileDataManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.NotDirectoryException;

/**
 * Created by everyone on 3/29/16.
 */
public class RestServerRun extends ServerRunner{
    private static final int DEFAULT_PORT = generateRandomPort();
    private static final String DEFAULT_ROOT = ".";
    private static final String DEFAULT_SERVICE_PATH = "PictureServer";

    public RestServerRun(DataManager dataManager, EndpointServer server) {
        super(dataManager, server);
        throw new NotImplementedException();
    }

    public static void main(String args[]) {
        File root = new File((args.length >= 1) ? args[0] : DEFAULT_ROOT);
        String serverPath = (args.length >= 2) ? args[1] : DEFAULT_SERVICE_PATH;
        int port = (args.length >= 3) ? Integer.parseInt(args[2]) : DEFAULT_PORT;

        URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(port).build();

        try {
            DataManager dataManager = new FileDataManager(root);
            RestServer server = new RestServer(serverPath, baseUri, dataManager);

            ServerRunner serverRunner = new ServerRunner(dataManager, server);
            serverRunner.start();

        } catch (NotDirectoryException e) {
            System.err.println("Directory not available.\n Terminating.");
            System.exit(1);
        } catch (MalformedURLException e) {
            System.err.println("Invalid serverPath or serverPort");
            System.exit(2);
        }

    }
}
