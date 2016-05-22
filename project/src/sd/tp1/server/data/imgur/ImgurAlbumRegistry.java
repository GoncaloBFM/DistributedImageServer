package sd.tp1.server.data.imgur;

import java.rmi.registry.Registry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gbfm on 5/19/16.
 */
public class ImgurAlbumRegistry extends ImgurRegistry{

    private ConcurrentHashMap<String,ImgurPictureRegistry> idRegistry = new ConcurrentHashMap<>();

    public boolean updateEntry(String id, String name) {
        if (super.updateEntry(id, name)) {
            idRegistry.put(id, new ImgurPictureRegistry());
            return true;
        }

        return false;
    }

    public String removeById(String id) {
        String name = super.removeById(id);
        if(null != name) {
            idRegistry.remove(id);
        }

        return name;
    }

    public void clear() {
        super.clear();
        idRegistry.clear();
    }

    public ImgurPictureRegistry getPictureRegistry(String albumId) {
        return idRegistry.get(albumId);
    }

    public void setPictureRegistry(String id, ImgurPictureRegistry registry) {
        idRegistry.put(id, registry);
    }

}
