package sd.tp1.server;

import sd.tp1.common.data.DataManager;
import sd.tp1.common.discovery.HeartbeatAnnouncer;
import sd.tp1.common.discovery.ServiceAnnouncer;
import sd.tp1.common.protocol.EndpointServer;

import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by apontes on 5/17/16.
 */
public class ServerRunner implements EndpointServer{
    private static final String SERVICE_TO_ANNOUNCE = "42845_43178_";
    private static final int ANNOUNCE_ON_PORT = 6968;

    private static final Logger LOGGER = Logger.getLogger(ServerRunner.class.getSimpleName());

    private static final int MIN_PORT = 49152; //1024;
    private static final int MAX_PORT = 65535; //65535;

    private static final int DEFAULT_PORT = generateRandomPort();
    private static final String DEFAULT_ROOT = ".";
    private static final String DEFAULT_SERVICE_PATH = "PictureServer";

    private EndpointServer server;
    private DataManager dataManager;

    private boolean running;
    private ServiceAnnouncer serviceAnnouncer;

    public ServerRunner(DataManager dataManager, EndpointServer server){
        this.server = server;
        this.dataManager = dataManager;

        URL url = server.getUrl();
        this.serviceAnnouncer = new HeartbeatAnnouncer(
                SERVICE_TO_ANNOUNCE + server.getType(),
                ANNOUNCE_ON_PORT,
                url.getFile(),
                url.getPort());

    }

    protected static int generateRandomPort(){
        return (int) (Math.random() * (MAX_PORT - MIN_PORT) + MIN_PORT);
    }

    @Override
    public void start() {
        if(isRunning())
            return;

        this.server.start();

        LOGGER.info("Server data source: " + dataManager);
        LOGGER.info("Server listening at: " + server.getUrl());

        serviceAnnouncer.startAnnounceService();
        LOGGER.info("Service announcer started! ;)");
    }

    @Override
    public void stop() {
        if(!isRunning())
            return;

        this.serviceAnnouncer.stopAnnounceService();
        this.server.stop();
        LOGGER.info("Server stopped!");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public String getType() {
        return server.getType();
    }

    @Override
    public URL getUrl() {
        return server.getUrl();
    }
}
