package sd.tp1.common.discovery;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

/**
 * Created by apontes on 3/21/16.
 */
public class HeartbeatAnnouncer implements ServiceAnnouncer {

    private static int HEART_BEAT_INITIAL_DELAY_SECONDS = 0;
    private static int HEART_BEAT_DELAY_SECONDS = 1;

    private static String ADDRESS = "239.255.255.255";
    private static int DEFAULT_PORT = 6969;

    private String serviceToAnnounce;
    private int announceOnPort;

    private int serverPort;
    private String serverPath;

    private ScheduledFuture scheduled;

    public HeartbeatAnnouncer(String serviceToAnnounce, String serverPath, int serverPort){
        this(serviceToAnnounce, DEFAULT_PORT, serverPath, serverPort);
    }

    public HeartbeatAnnouncer(String serviceToAnnounce, int announceOnPort, String serverPath, int serverPort) {
        this.serviceToAnnounce = serviceToAnnounce;
        this.serverPath = serverPath;
        this.serverPort = serverPort;
        this.announceOnPort = announceOnPort;
    }

    @Override
    public void startAnnounceService() {
        if(this.scheduled != null)
            return;

        final MulticastSocket multicastSocket;
        final DatagramPacket packet;
        try {
            multicastSocket = new MulticastSocket();
            byte[] input = String.format("%s@%d:/%s", serviceToAnnounce, serverPort, serverPath).getBytes();
            packet = new DatagramPacket(input, input.length);
            packet.setPort(announceOnPort);
            packet.setAddress(InetAddress.getByName(ADDRESS));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        this.scheduled = exec.scheduleAtFixedRate((Runnable) () -> {
            try {
                multicastSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, HEART_BEAT_INITIAL_DELAY_SECONDS, HEART_BEAT_DELAY_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public void stopAnnounceService() {
        if(this.scheduled != null && this.scheduled.cancel(false))
            this.scheduled = null;
    }
}
