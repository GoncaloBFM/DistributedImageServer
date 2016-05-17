package sd.tp1.server.replication.backdoor;

import sd.tp1.common.SharedAlbum;
import sd.tp1.common.SharedPicture;
import sd.tp1.server.DataManager;
import sd.tp1.server.replication.metadata.Metadata;
import sd.tp1.server.replication.metadata.MetadataManager;
import sd.tp1.server.replication.metadata.ServerMetadata;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Created by apontes on 5/15/16.
 */
@WebService
public class SOAPReplicationServerBackdoor implements ReplicationServerBackdoor {

    private Logger logger = Logger.getLogger(SOAPReplicationServerBackdoor.class.getSimpleName());

    private MetadataManager metadataManager;
    private DataManager dataManager;
    private BackdoorFactory backdoorFactory;

    public SOAPReplicationServerBackdoor(){
        throw new NotImplementedException();
    }

    public SOAPReplicationServerBackdoor(MetadataManager metadataManager, DataManager dataManager, BackdoorFactory backdoorFactory){
        this.metadataManager = metadataManager;
        this.dataManager = dataManager;
        this.backdoorFactory = backdoorFactory;
    }

    @Override
    @WebMethod
    public void sendMetadata(ServerMetadata serverMetadata, Collection<Metadata> metadata) {
        this.logger.info(String.format("Metadata received [serverId: %s]", serverMetadata.getServerId()));
        ReplicationServerBackdoor backdoor = this.backdoorFactory.getBackdoor(serverMetadata);
        //No backdoor available
        if(backdoor == null)
            return;



        for(Metadata iMeta : metadata){
            if(iMeta.getAlbum() != null && iMeta.getPicture() != null){ //iMeta.isPicture()){
                SharedAlbum album = iMeta.getAlbum();
                SharedPicture picture = iMeta.getPicture();

                //updated or newer
                if(iMeta.compareTo(this.metadataManager.getMetadata(album, picture)) <= 0)
                    continue;

                //need update
                this.logger.info(String.format("Picture need update (%s\\%s)", album.getName(), picture.getPictureName()));
                if(iMeta.getDeleted()){
                    this.dataManager.deletePicture(album, picture);
                }
                else{
                    byte[] data = backdoor.getPictureData(album, picture);
                    if(data == null)
                        continue;

                    this.dataManager.uploadPicture(album, picture.getPictureName(), data);
                }

                this.logger.info(String.format("Picture updated (%s\\%s)", album.getName(), picture.getPictureName()));
                this.metadataManager.setMetadata(album, picture, iMeta);
            }
            else if(iMeta.getAlbum() != null){ //iMeta.isAlbum()){
                SharedAlbum album = iMeta.getAlbum();

                //updated or newer
                if(iMeta.compareTo(this.metadataManager.getMetadata(album)) <= 0)
                    continue;

                //need update
                this.logger.info(String.format("Album need update (%s)", album.getName()));
                if(iMeta.getDeleted()){
                    this.dataManager.deleteAlbum(album);
                }
                else{
                    this.dataManager.createAlbum(album.getName());
                }

                this.logger.info(String.format("Album updated (%s)", album.getName()));
                this.metadataManager.setMetadata(album, iMeta);
            }
            else{
                throw new RuntimeException("Metadata not a picture nor album");
            }
        }

        //Analise and query server
    }

    @Override
    @WebMethod
    public byte[] getPictureData(SharedAlbum album, SharedPicture picture) {
        this.logger.info(String.format("Sending picture (%s\\%s)", album.getName(), picture.getPictureName()));
        return dataManager.loadPictureData(album, picture);
    }

    @Override
    @WebMethod
    public ServerMetadata getServerMetadata() {
        this.logger.info("Sending metadata");
        return metadataManager.getServerMetadata();
    }
}
