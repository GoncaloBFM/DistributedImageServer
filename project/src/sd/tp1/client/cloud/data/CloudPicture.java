package sd.tp1.client.cloud.data;

import sd.tp1.common.Metadata;
import sd.tp1.common.Picture;

/**
 * Created by apontes on 3/27/16.
 */
public class CloudPicture extends AbstractCloudObject implements Picture {

    private String name;
    private CloudAlbum album;

    public CloudPicture(String name, CloudAlbum album, String serverId){
        super(serverId);

        this.name = name;this.album = album;
    }

    public String getPictureName() {
        return name;
    }

    public CloudAlbum getAlbum(){
        return this.album;
    }

}
