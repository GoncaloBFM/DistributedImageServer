package sd.tp1.server.replication.server.backdoor;

import sd.tp1.common.Album;
import sd.tp1.common.Picture;
import sd.tp1.common.SharedAlbum;
import sd.tp1.server.replication.metadata.Metadata;
import sd.tp1.server.replication.metadata.ServerMetadata;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by apontes on 5/15/16.
 */
@WebService
public class SOAPReplicationServerBackdoor implements ReblicationServerBackdoor{


    @WebMethod
    @Override
    public void sendMetadata(Metadata[] metadata) {

    }

    @WebMethod
    @Override
    public byte[] getPictureData(SharedAlbum album, SharedAlbum picture) {
        return new byte[0];
    }

    @Override
    public ServerMetadata getServerMetadata() {
        return null;
    }
}
