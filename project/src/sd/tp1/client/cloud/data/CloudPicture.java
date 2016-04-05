package sd.tp1.client.cloud.data;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.Server;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by apontes on 3/27/16.
 */
public class CloudPicture extends AbstractCloudObject implements Picture {

    private String name;
    private CloudAlbum album;

    public CloudPicture(String name, CloudAlbum album){
        this.name = name;this.album = album;
    }

    public String getPictureName() {
        return name;
    }

    public CloudAlbum getAlbum(){
        return this.album;
    }
}
