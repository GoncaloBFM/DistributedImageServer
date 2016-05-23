package sd.tp1.common.discovery;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by everyone on 3/21/16.
 */
public class HeartbeatDiscovery implements ServiceDiscovery {

    private static final Logger LOGGER = Logger.getLogger(HeartbeatDiscovery.class.getName());


    //224.0.0.0 -- 239.255.255.255
    private static String ADDRESS = "239.255.255.255";
    private static int DEFAUTL_PORT = 6969;

    private static int SWIPE_INITIAL_DELAY_SECONDS = 0;
    private static int SWIPE_DELAY_SECONDS = 10;
    private static long ALLOWED_TIME_SINCE_LAST_BEAT = 5000;

    private final Map<URL, HeartbeatServer> serverMap = new HashMap<>();
    private String serviceToDiscover;

    private int port;

    public HeartbeatDiscovery(String serviceToDiscover){
        this(serviceToDiscover, DEFAUTL_PORT);
    }

    public HeartbeatDiscovery(String serviceToDiscover, int port) {
        this.serviceToDiscover = serviceToDiscover;
        this.port = port;
    }


    @Override
    public void discoverService(ServiceHandler handler) {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate((Runnable) () -> {
            synchronized (this.serverMap){
                for (HeartbeatServer server: serverMap.values()) {
                    if (server.getCurrentState() == HeartbeatServer.State.ONLINE && server.getTimeSinceLastBeat() > ALLOWED_TIME_SINCE_LAST_BEAT) {
                        LOGGER.fine(String.format("Server lost %s", server.getURL()));

                        server.setCurrentState(HeartbeatServer.State.OFFLINE);
                        handler.serviceLost(this.serviceToDiscover, server.getURL());
                    }
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
                    socket = new MulticastSocket(port);
                    socket.joinGroup(address);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                for(;;) {
                    try {
                        byte[] buff = new byte[65535];
                        DatagramPacket p = new DatagramPacket(buff, buff.length);
                        socket.receive(p);
                        handleServiceHeartbeat(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            private void handleServiceHeartbeat(DatagramPacket p) throws MalformedURLException {
                String data = new String(p.getData(), 0, p.getLength());
                if(!data.startsWith(serviceToDiscover))
                    return;

                String file; int port;

                try{
                    String[] serviceData = data.split("@");
                    if(!serviceData[0].equals(serviceToDiscover))
                        return;

                    data = serviceData[1];
                    String[] portFile = data.split(":");

                    port = Integer.parseInt(portFile[0]);
                    file = portFile.length > 1 ? portFile[1] : "/";

                }
                catch(Exception e){
                    LOGGER.severe("Invalid packet data: " + data);
                    return;
                }

                URL url = new URL("http",
                        p.getAddress().getHostAddress(),
                        port,
                        file);

                HeartbeatServer server = serverMap.get(url);
                if(server != null){
                    LOGGER.fine(String.format("Server rediscovered %s", url.toString()));

                    server.setNewTimeOfLastBeat();
                    if (server.getCurrentState().equals(HeartbeatServer.State.OFFLINE)) {
                        server.setCurrentState(HeartbeatServer.State.ONLINE);
                        handler.serviceDiscovered(serviceToDiscover, server.getURL());
                    }
                }
                else{
                    synchronized (serverMap){
                        LOGGER.fine(String.format("Server discovered %s", url.toString()));

                        server = new HeartbeatServer(url);
                        server.setCurrentState(HeartbeatServer.State.ONLINE);
                        serverMap.put(url, server);
                        handler.serviceDiscovered(serviceToDiscover, server.getURL());
                    }
                }
            }
        }.start();
    }

    private static class HeartbeatServer {
        private enum State {ONLINE, OFFLINE}
        private State currentState;
        private long timeOfLastBeat;
        private URL url;

        public HeartbeatServer(URL url) {
            this.url = url;
            setNewTimeOfLastBeat();

        }

        public State getCurrentState() {
            return currentState;
        }

        public void setCurrentState(State newState) {
            currentState = newState;
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
