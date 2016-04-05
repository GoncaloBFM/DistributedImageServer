package sd.tp1.server;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by apontes on 3/21/16.
 */
public class HeartbeatAnnouncer implements ServiceAnnouncer {

    private static int HEART_BEAT_INITIAL_DELAY_SECONDS = 0;
    private static int HEART_BEAT_DELAY_SECONDS = 1;

    private static String ADDRESS = "239.255.255.255";

    private String serviceToAnnounce;
    private int port;
    private String file;

    public HeartbeatAnnouncer(String serviceToAnnounce, String file, int port) {
        this.serviceToAnnounce = serviceToAnnounce;
        this.file = file;
        this.port = port;
    }

    @Override
    public void announceService() {

        final MulticastSocket multicastSocket;
        final DatagramPacket packet;
        try {
            multicastSocket = new MulticastSocket();
            byte[] input = String.format("%s@%s", serviceToAnnounce, file).getBytes();
            packet = new DatagramPacket(input, input.length);
            packet.setPort(port);
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
