package sd.tp1.server.data.imgur;

import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gbfm on 5/19/16.
 */
public class ImgurRegistry {

    private ConcurrentHashMap<String,String> idName = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> nameId = new ConcurrentHashMap<>();

    public boolean updateEntry(String id, String name) {
        if (idName.containsKey(id)) {
            return false;
        }

        nameId.put(name, id);
        idName.put(id, name);

        return true;
    }

    public String removeById(String id) {
        if (!idName.containsKey(id)) {
            return null;
        }

        String name = idName.remove(id);
        nameId.remove(name);
        return name;

    }

    public boolean hasId(String id) {
        return idName.containsKey(id);
    }

    public boolean hasname(String name) {
        return nameId.containsKey(name);
    }

    public String getName(String id) {
        return idName.get(id);
    }

    public String getId(String name) {
        return nameId.get(name);
    }

    public void clear() {
        idName.clear();
        nameId.clear();
    }

    public Map<String,String> getIdNameMap() {
        return idName;
    }

}
