package sd.tp1.server.replication;

import sd.tp1.common.ClientFactory;
import sd.tp1.common.data.*;
import sd.tp1.common.discovery.ServiceHandler;
import sd.tp1.common.protocol.Endpoint;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by everyone on 5/18/16.
 */
public class TotalReplication implements ReplicationEngine{

    private static final int MILLIS_BETWEEN_SERVERS = 1; //5000;
    private static final int MILLIS_BETWEEN_ROUNDS = 5000; //60000;

    protected Logger logger = Logger.getLogger(TotalReplication.class.getName());

    protected final DataManager local;
    private final Map<URL, Endpoint> endpointMap = new ConcurrentHashMap<>();

    private boolean running = false;

    private ReplicationThread replicationWorker = new ReplicationThread();

    public TotalReplication(DataManager local){
        this.local = local;

        ClientFactory.startDiscovery(new ServiceHandler() {
            @Override
            public void serviceDiscovered(String service, URL url) {
                try {
                    Endpoint remote = ClientFactory.create(service, url);
                    if(remote != null) {
                        String localId = local.getServerId();
                        String remoteId = remote.getServerId();

                        if (remoteId != null && !remoteId.equals(localId)) {
                            endpointMap.put(url, remote);
                            onServerAdd(url, remote);
                        }
                    }

                } catch (ClientFactory.ClientFactoryException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void serviceLost(String service, URL url) {
                endpointMap.remove(url);
                onServerDel(url);
            }
        });
    }

    protected void onServerAdd(URL url, Endpoint remote){
        //point cut
    }

    protected void onServerDel(URL url){
        //point cut
    }

    public void startReplication(){
        if(running)
            return;

        running = true;
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

    protected void handleMetadata(URL url, Endpoint remote, List<SharedAlbum> albumList, List<SharedAlbumPicture> pictureList){
        albumList
                .parallelStream()
                .filter(local::needUpdate)
                .forEach(x -> {
                    //Update album
                    if(!x.isDeleted())
                        local.createAlbum(x);
                    else
                        local.deleteAlbum(x);
                });

        pictureList
                .parallelStream()
                .filter(x -> local.needUpdate(x.getAlbum(), x.getPicture()))
                .forEach(x -> {
                    //Update picture
                    if(!x.getPicture().isDeleted())
                        local.uploadPicture(x.getAlbum(), x.getPicture(),
                                remote.loadPictureData(x.getAlbum().getName(), x.getPicture().getPictureName()));
                    else
                        local.deletePicture(x.getAlbum(), x.getPicture());
                });
    }

    class ReplicationThread extends Thread {
        @Override
        public void run(){
            while(running){
                LinkedList<Endpoint> queue = new LinkedList<>(endpointMap.values());
                Collections.shuffle(queue);

                while(running && !queue.isEmpty()) {
                    Endpoint remote = queue.poll();

                    String serverId = remote.getServerId();
                    if (serverId == null || serverId.equals(local.getServerId()))
                        continue;

                    URL remoteUrl = remote.getUrl();
                    if(remoteUrl == null)
                        continue;

                    logger.info("Start replication with: " + serverId);

                    //MetadataBundle localMeta = local.getMetadata();
                    MetadataBundle remoteMeta = remote.getMetadata();
                    if (remoteMeta == null || remoteMeta.getAlbumList() == null || remoteMeta.getPictureList() == null){
                        logger.severe("Replication error with: " + serverId);
                        continue;
                    }

                    handleMetadata(remoteUrl, remote, remoteMeta.getAlbumList(), remoteMeta.getPictureList());

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
