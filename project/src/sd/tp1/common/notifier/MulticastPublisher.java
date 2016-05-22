package sd.tp1.common.notifier;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by apontes on 5/22/16.
 */
public class MulticastPublisher implements Publisher{

    private static final int PORT = 6666;
    private static final String ADDRESS = "localhost";

    private MulticastSocket socket;

    public MulticastPublisher(){

        final MulticastSocket multicastSocket;
        final DatagramPacket packet;
        try {
            socket = new MulticastSocket();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        throw new NotImplementedException();
    }

    @Override
    public void notifyAlbumUpdate(String album) {

        try {
            byte[] data = toBytes(new MulticastEnvelop(album));
            DatagramPacket packet = new DatagramPacket(data, data.length);


        } catch (IOException e) {
            //
        }

        //packet.setPort(announceOnPort);
        //packet.setAddress(InetAddress.getByName(ADDRESS));
    }

    @Override
    public void notifyPictureUpdate(String album, String picture) {

    }

    private byte[] toBytes(Serializable o) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteArray);

        out.writeObject(o);
        return byteArray.toByteArray();
    }
}
