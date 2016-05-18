package sd.tp1.server.replication;

import sd.tp1.common.ClientFactory;
import sd.tp1.common.data.DataManager;
import sd.tp1.common.data.Metadata;
import sd.tp1.common.data.MetadataBundle;
import sd.tp1.common.data.Picture;
import sd.tp1.common.discovery.HeartbeatDiscovery;
import sd.tp1.common.discovery.ServiceDiscovery;
import sd.tp1.common.discovery.ServiceHandler;
import sd.tp1.common.protocol.Endpoint;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by apontes on 5/18/16.
 */
public class ReplicationEngine {

    private static final int MILLIS_BETWEEN_SERVERS = 1000; //5000;
    private static final int MILLIS_BETWEEN_ROUNDS = 5000; //60000;

    private final Logger logger = Logger.getLogger(ReplicationEngine.class.getName());

    private final DataManager local;
    private final Map<URL, Endpoint> endpointMap = new ConcurrentHashMap<>();

    private boolean running;
    private boolean firstRun = true;

    private ReplicationThread replicationWorker = new ReplicationThread();

    public ReplicationEngine(DataManager local){
        this.local = local;
    }

    public void startReplication(){
        if(running)
            return;

        running = true;

        if(firstRun)
            ClientFactory.startDiscovery(new ServiceHandler() {
                @Override
                public void serviceDiscovered(String service, URL url) {
                    try {
                        Endpoint endpoint = ClientFactory.create(service, url);
                        if(endpoint != null)
                            endpointMap.put(url, endpoint);

                    } catch (ClientFactory.ClientFactoryException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void serviceLost(String service, URL url) {
                    endpointMap.remove(url);
                }
            });
        firstRun = false;

        this.replicationWorker.start();
    }

    public void stopReplication(){
        if(!running)
            return;

        running = false;

        try {
            replicationWorker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class ReplicationThread extends Thread {
        @Override
        public void run(){
            while(running){
                LinkedList<Endpoint> queue = new LinkedList(endpointMap.values());
                Collections.shuffle(queue);

                while(running && !queue.isEmpty()){
                    Endpoint remote = queue.poll();

                    URL url = remote.getUrl();
                    if(url == null)
                        continue;

                    logger.info("Start replication with: " + url);

                    //MetadataBundle localMeta = local.getMetadata();
                    MetadataBundle remoteMeta = remote.getMetadata();

                    remoteMeta.getAlbumList()
                            .parallelStream()
                            .filter(local::isNewer)
                            .forEach(x -> {
                                //Update album
                                local.setAlbum(x);
                                //todo fetch picture albums (or not)
                            });

                    remoteMeta.getPictureList()
                            .parallelStream()
                            .filter(x -> local.isNewer(x.getAlbum(), x.getPicture()))
                            .forEach(x -> {
                                //Update picture
                                Picture old = local.getPicture(x.getAlbum().getName(), x.getPicture().getPictureName());
                                if(old == null){
                                    //fetch pictures
                                    byte[] data = remote.loadPictureData(x.getAlbum().getName(), x.getPicture().getPictureName());
                                    local.uploadPicture(x.getAlbum(), x.getPicture(), data);
                                }
                                else{
                                    local.setPicture(x.getAlbum(), x.getPicture());
                                }
                            });


                    try {
                        Thread.sleep(MILLIS_BETWEEN_SERVERS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                logger.info("ReplicationRound ended");

                try {
                    Thread.sleep(MILLIS_BETWEEN_ROUNDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
