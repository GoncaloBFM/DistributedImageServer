package sd.tp1.client.cloud.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apontes on 3/27/16.
 */
public class ThreeMap<K1, K2, V> {
    private Map<K1, Map<K2, V>> map = new HashMap<>();

    public void put(K1 k1, K2 k2, V value){
        Map<K2, V> subMap = map.get(k1);
        if(subMap == null){
            subMap = new HashMap<K2, V>();
            map.put(k1, subMap);
        }

        subMap.put(k2, value);
    }

    public V get(K1 k1, K2 k2){
        Map<K2, V> subMap = map.get(k1);
        if(subMap == null)
            return null;

        return subMap.get(k2);
    }
}
