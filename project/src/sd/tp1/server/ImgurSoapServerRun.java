package sd.tp1.server;

import sd.tp1.common.data.DataManager;
import sd.tp1.common.protocol.EndpointServer;
import sd.tp1.common.protocol.soap.server.SoapServer;
import sd.tp1.server.data.FileDataManager;
import sd.tp1.server.data.imgur.ImgurDataManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.NotDirectoryException;

/**
 * Created by everyone on 3/21/16.
 */
public class ImgurSoapServerRun extends ServerRunner {

    private static final int DEFAULT_PORT = generateRandomPort();
    private static final String DEFAULT_ROOT = ".";
    private static final String DEFAULT_SERVICE_PATH = "PictureServer";

    public ImgurSoapServerRun(DataManager dataManager, EndpointServer server) {
        super(dataManager, server);
        throw new NotImplementedException();
    }

    /**
     *
     * @param args 0: Root; 1:ServerPath; 2:Port;
     * @throws Exception
     */
    public static void main(String args[])  {


        File root = new File((args.length >= 1) ? args[0] : DEFAULT_ROOT);
        String serverPath = (args.length >= 2) ? args[1] : DEFAULT_SERVICE_PATH;
        int port = (args.length >= 3) ? Integer.parseInt(args[2]) : DEFAULT_PORT;

        try {
            URL url = new URL(String.format("http://%s:%d/%s", "0.0.0.0", port, serverPath));
            DataManager dataManager = new ImgurDataManager(root);

            SoapServer server = new SoapServer(dataManager, url);

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
