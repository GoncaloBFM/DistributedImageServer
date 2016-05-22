package sd.tp1.common.notifier;

import java.io.Serializable;

/**
 * Created by apontes on 5/22/16.
 */
public class MulticastEnvelop implements Serializable {
    public String album;
    public String picture;

    public MulticastEnvelop(String album) {
        this.album = album;
    }

    public MulticastEnvelop(String album, String picture) {
        this.album = album;
        this.picture = picture;
    }
}
