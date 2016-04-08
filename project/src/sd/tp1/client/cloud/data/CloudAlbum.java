package sd.tp1.client.cloud.data;

import sd.tp1.Album;
import sd.tp1.Picture;
import sd.tp1.client.cloud.Server;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by apontes on 3/27/16.
 */
public class CloudAlbum extends AbstractCloudObject implements Album {
    private String name;
    private List<CloudPicture> pictures = new LinkedList<>();

    public CloudAlbum(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<CloudPicture> getPictures(){
        return this.pictures;
    }

    public boolean containsPicture(String picture){
        for(Picture p : this.pictures)
            if(p.equals(picture))
                return true;

        return false;
    }
}
