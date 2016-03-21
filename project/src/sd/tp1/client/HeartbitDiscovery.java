package sd.tp1.client;

import sd.tp1.server.Server;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by apontes on 3/21/16.
 */
public class HeartbitDiscovery implements ServiceDiscovery {


    //224.0.0.0 -- 239.255.255.255
    public static String ADDRESS = "244.0.0.0";


    /**
     * Search for service providers for given service
     *
     * @param service service to look for
     * @param handler service handler
     */
    @Override
    public void discoverService(String service, ServiceHandler handler) {

        new Thread(){

            ServiceHandler serviceHandler = handler;
            Map<String, Server> map = new HashMap<>();

            @Override
            public void run(){
                try {
                    InetAddress address = InetAddress.getByName(ADDRESS);
                    MulticastSocket socket = new MulticastSocket();
                    socket.joinGroup(address);

                    for(;;){
                        byte[] buff = new byte[65636];
                        DatagramPacket p = new DatagramPacket(buff, buff.length);
                        socket.receive(p);
                        handleServiceHeartbit(p.getAddress(), p.getPort(), new String(p.getData(), 0, p.getLength()));
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            private void handleServiceHeartbit(InetAddress address, int port, String url){
                if(map.containsKey(url)){
                    //do nothing
                }
                else{

                }
            }

        }.start();
    }
}
