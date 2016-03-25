package sd.tp1.client;

import sd.tp1.client.ws.WSServer;
import sd.tp1.client.ws.WSServerService;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by apontes on 3/21/16.
 */
public class HeartbeatDiscovery implements ServiceDiscovery {


    //224.0.0.0 -- 239.255.255.255
    private static String ADDRESS = "239.255.255.255";
    private static int PORT = 6969;

    private static int SWIPE_INITIAL_DELAY_SECONDS = 0;
    private static int SWIPE_DELAY_SECONDS = 10;
    private static long ALLOWED_TIME_SINCE_LAST_BEAT = 100;

    private Map<String, HeartbeatServer> serverMap = new HashMap<>();
    private String serviceToDiscover;


    public HeartbeatDiscovery(String serviceToDiscover) {
        this.serviceToDiscover = serviceToDiscover;
    }


    @Override
    public void discoverService(ServiceHandler handler) {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate((Runnable) () -> {
            for (String key: serverMap.keySet()) {
                HeartbeatServer server = serverMap.get(key);
                if (server.getCurrentState() != HeartbeatServer.State.OFFLINE && server.getTimeSinceLastBeat() > ALLOWED_TIME_SINCE_LAST_BEAT) {
                    server.setCurrentState(HeartbeatServer.State.OFFLINE);
                    handler.serviceNotAnymore(server.getEntity());
                }
            }
        }, SWIPE_INITIAL_DELAY_SECONDS, SWIPE_DELAY_SECONDS, TimeUnit.SECONDS);

        new Thread(){
            @Override
            public void run(){
                InetAddress address = null;
                MulticastSocket socket = null;
                try {
                    address = InetAddress.getByName(ADDRESS);
                    socket = new MulticastSocket(PORT);
                    socket.joinGroup(address);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                for(;;) {
                    try {
                        byte[] buff = new byte[65536];
                        DatagramPacket p = new DatagramPacket(buff, buff.length);
                        socket.receive(p);
                        handleServiceHeartbeat(new String(p.getData(), 0, p.getLength()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            private void handleServiceHeartbeat(String url) throws MalformedURLException {
                HeartbeatServer server = serverMap.get(url);
                if(server != null){
                    server.setNewTimeOfLastBeat();
                    server.setCurrentState(HeartbeatServer.State.ONLINE);
                }
                else{
                    URL wsURL = new URL(url);
                    WSServerService service = new WSServerService(wsURL);
                    WSServer entity = service.getWSServerPort();
                    serverMap.put(url,new HeartbeatServer(wsURL, entity));
                    handler.serviceDiscovered(entity);
                }
            }
        }.start();
    }
    private static class HeartbeatServer {
        private enum State {ONLINE, OFFLINE}
        private State currentState;
        private WSServer entity;
        private long timeOfLastBeat;
        private URL url;

        public HeartbeatServer(URL url, WSServer entity) {
            this.url = url;
            this.entity = entity;
            setNewTimeOfLastBeat();

        }

        public State getCurrentState() {
            return currentState;
        }

        public void setCurrentState(State newState) {
            currentState = newState;
        }

        public WSServer getEntity() {
            return entity;
        }
        public long getTimeSinceLastBeat() {
            return System.currentTimeMillis() - timeOfLastBeat;
        }

        public void setNewTimeOfLastBeat() {
            timeOfLastBeat = System.currentTimeMillis();
        }

        public long getTimeOfLastBeat() {
            return timeOfLastBeat;
        }

        public URL getURL(){
            return url;
        }
    }
}
