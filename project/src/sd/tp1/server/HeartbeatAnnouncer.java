package sd.tp1.server;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    public void announceService() {

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
        exec.scheduleAtFixedRate((Runnable) () -> {
            try {
                multicastSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, HEART_BEAT_INITIAL_DELAY_SECONDS, HEART_BEAT_DELAY_SECONDS, TimeUnit.SECONDS);
    }
}
