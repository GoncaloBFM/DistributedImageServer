package sd.tp1.server.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gbfm on 5/19/16.
 */
public class ImgurRegistry {

    private HashMap<String,String> idName = new HashMap<>();
    private HashMap<String,String> nameId = new HashMap<>();

    public boolean updateEntry(String id, String name) {
        if (idName.containsKey(id)) {
            return false;
        }

        if(nameId.containsKey(name)) {
            name += id;
        }

        nameId.put(name, id);
        idName.put(id, name);

        return true;
    }

    public boolean removeById(String id) {
        if (!idName.containsKey(id)) {
            return false;
        }

        String name = idName.remove(id);
        nameId.remove(name);

        return true;
    }

    public boolean hasId(String id) {
        return idName.containsKey(id);
    }

    public boolean hasName(String name) {
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

}
