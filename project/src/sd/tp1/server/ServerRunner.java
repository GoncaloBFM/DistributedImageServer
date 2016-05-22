package sd.tp1.server;

import sd.tp1.common.data.DataManager;
import sd.tp1.common.discovery.HeartbeatAnnouncer;
import sd.tp1.common.discovery.ServiceAnnouncer;
import sd.tp1.common.notifier.EventHandler;
import sd.tp1.common.notifier.KafkaPublisher;
import sd.tp1.common.notifier.Publisher;
import sd.tp1.common.protocol.EndpointServer;
import sd.tp1.server.replication.ReplicationEngine;

import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by apontes on 5/17/16.
 */
public class ServerRunner implements EndpointServer{
    private static final String SERVICE_TO_ANNOUNCE = "42845_43178_";
    private static final int ANNOUNCE_ON_PORT = 6969;

    private static final Logger LOGGER = Logger.getLogger(ServerRunner.class.getSimpleName());

    private static final int MIN_PORT = 49152; //1024;
    private static final int MAX_PORT = 65535; //65535;

    private EndpointServer server;
    private DataManager dataManager;

    private boolean running;
    private ServiceAnnouncer serviceAnnouncer;

    private ReplicationEngine replicationEngine;

    public ServerRunner(DataManager dataManager, EndpointServer server) {
        this.server = server;
        this.dataManager = dataManager;

        URL url = server.getUrl();
        this.serviceAnnouncer = new HeartbeatAnnouncer(
                SERVICE_TO_ANNOUNCE + server.getType(),
                ANNOUNCE_ON_PORT,
                url.getFile(),
                url.getPort());

        replicationEngine = new ReplicationEngine(dataManager);

        Publisher publisher = new KafkaPublisher();

        dataManager.addEventHandler(new EventHandler() {
            @Override
            public void onAlbumUpdate(String album) {
                publisher.notifyAlbumUpdate(album);
            }

            @Override
            public void onPictureUpdate(String album, String picture) {
                publisher.notifyPictureUpdate(album, picture);
            }
        });
    }

    protected static int generateRandomPort(){
        return (int) (Math.random() * (MAX_PORT - MIN_PORT) + MIN_PORT);
    }

    @Override
    public void start() {
        if(running)
            return;

        this.server.start();

        LOGGER.info("Server data source: " + dataManager);
        LOGGER.info("Server listening at: " + server.getUrl());

        serviceAnnouncer.startAnnounceService();
        LOGGER.info("Service announcer started! ;)");

        replicationEngine.startReplication();
        LOGGER.info("ReplicationEngine started! ;)");

        running = true;
    }

    @Override
    public void stop() {
        if(!running)
            return;

        this.serviceAnnouncer.stopAnnounceService();
        this.server.stop();
        LOGGER.info("Server stopped!");

        replicationEngine.stopReplication();
        LOGGER.info("ReplicationEngine stopped!");

        running = false;
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
