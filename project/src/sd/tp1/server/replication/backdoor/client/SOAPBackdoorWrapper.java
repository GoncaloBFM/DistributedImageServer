package sd.tp1.server.replication.backdoor.client;

import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
import sd.tp1.server.replication.backdoor.ReblicationServerBackdoor;
import sd.tp1.server.replication.backdoor.client.stubs.SOAPReplicationServerBackdoor;
import sd.tp1.server.replication.backdoor.client.stubs.SOAPReplicationServerBackdoorService;
import sd.tp1.server.replication.metadata.Metadata;
import sd.tp1.server.replication.metadata.ServerMetadata;

import java.net.URL;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by apontes on 5/16/16.
 */
public class SOAPBackdoorWrapper implements ReblicationServerBackdoor{

    SOAPReplicationServerBackdoor server;

    public SOAPBackdoorWrapper(URL url){
        SOAPReplicationServerBackdoorService service = new SOAPReplicationServerBackdoorService(url);
        this.server = service.getSOAPReplicationServerBackdoorPort();
    }

    @Override
    public void sendMetadata(ServerMetadata serverMetadata, Collection<Metadata> metadata) {
        SafeInvoker.invoke(
                () -> { server.sendMetadata(new ServerMetadataWrapper(serverMetadata),
                        metadata.stream().map(x -> new MetadataWrapper(x)).collect(Collectors.toList()));
                        return null;});
    }

    @Override
    public byte[] getPictureData(SharedAlbum album, SharedPicture picture) {
        return SafeInvoker.invoke(
                () -> server.getPictureData(new AlbumWrapper(album), new PictureWrapper(picture)));
    }

    @Override
    public ServerMetadata getServerMetadata() {
        return SafeInvoker.invoke(
                () -> new ServerMetadataWrapper(server.getServerMetadata()).getServerMetada());
    }
}
