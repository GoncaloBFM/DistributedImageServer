package sd.tp1.client.cloud.data;

import sd.tp1.Album;
import sd.tp1.client.cloud.Server;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by apontes on 3/27/16.
 */
public class CloudAlbum extends AbstractCloudObject implements Album {
    private String name;

    public CloudAlbum(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
