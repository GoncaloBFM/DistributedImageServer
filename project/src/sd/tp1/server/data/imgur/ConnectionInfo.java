package sd.tp1.server.data.imgur;

/**
 * Created by everyone on 5/19/16.
 */
public class ConnectionInfo {

    public final String apiKey;
    public final String apiSecret;
    public final String pin;

    public ConnectionInfo(String apiKey, String apiSecret, String pin) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.pin = pin;
    }

    //imgur.con
    //{"apiKey":"01c07ff99455d21","apiSecret":"82dbdf420a192a4f7c65ba2b116a473f3b19b518","pin":"5d31cf9c886b5740fbca83387ca103a6131c3940"}

}
