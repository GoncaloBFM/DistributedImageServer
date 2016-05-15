package sd.tp1.server.replication.backdoor;

import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
import sd.tp1.server.DataManager;
import sd.tp1.server.replication.metadata.Metadata;
import sd.tp1.server.replication.metadata.MetadataManager;
import sd.tp1.server.replication.metadata.ServerMetadata;
import sun.security.provider.SHA;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Collection;

/**
 * Created by apontes on 5/15/16.
 */
public class SOAPReplicationServerBackdoor implements ReblicationServerBackdoor{

    private MetadataManager metadataManager;
    private DataManager dataManager;
    private BackdoorFactory backdoorFactory;

    public SOAPReplicationServerBackdoor(MetadataManager metadataManager, DataManager dataManager, BackdoorFactory backdoorFactory){
        this.metadataManager = metadataManager;
        this.dataManager = dataManager;
    }

    @Override
    public void sendMetadata(ServerMetadata serverMetadata, Collection<Metadata> metadata) {
        ReblicationServerBackdoor backdoor = this.backdoorFactory.getBackdoor(serverMetadata);
        //No backdoor available
        if(backdoor == null)
            return;

        for(Metadata iMeta : metadata){
            if(iMeta.isPicture()){
                SharedAlbum album = iMeta.getAlbum();
                SharedPicture picture = iMeta.getPicture();

                //updated or newer
                if(iMeta.compareTo(this.metadataManager.getMetadata(album, picture)) <= 0)
                    continue;

                //need update
                if(iMeta.isDeleted()){
                    this.dataManager.deletePicture(album, picture);
                }
                else{
                    byte[] data = backdoor.getPictureData(album, picture);
                    this.dataManager.uploadPicture(album, picture.getPictureName(), data);
                }

                this.metadataManager.setMetadata(album, picture, iMeta);
            }
            else if(iMeta.isAlbum()){
                SharedAlbum album = iMeta.getAlbum();

                //updated or newer
                if(iMeta.compareTo(this.metadataManager.getMetadata(album)) <= 0)
                    continue;

                //need update
                if(iMeta.isDeleted()){
                    this.dataManager.deleteAlbum(album);
                }
                else{
                    this.dataManager.createAlbum(album.name);
                }

                this.metadataManager.setMetadata(album, iMeta);
            }
            else{
                throw new RuntimeException("Metadata not a picture nor album");
            }
        }

        //Analise and query server
    }

    @Override
    public byte[] getPictureData(SharedAlbum album, SharedPicture picture) {
        return dataManager.loadPictureData(album, picture);
    }

    @Override
    public ServerMetadata getServerMetadata() {
        return metadataManager.getServerMetadata();
    }
}
