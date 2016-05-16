package sd.tp1.server.replication.backdoor;

import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
import sd.tp1.server.replication.metadata.ServerMetadata;
import sd.tp1.server.replication.metadata.Metadata;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Collection;

/**
 * Created by apontes on 5/15/16.
 */
@WebService
public interface ReplicationServerBackdoor {

    @WebMethod
    void sendMetadata(ServerMetadata serverMetadata, Collection<Metadata> metadata);

    @WebMethod
    byte[] getPictureData(SharedAlbum album, SharedPicture picture);

    @WebMethod
    ServerMetadata getServerMetadata();
}
