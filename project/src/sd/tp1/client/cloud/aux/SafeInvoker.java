package sd.tp1.client.cloud.aux;

import org.glassfish.jersey.internal.util.Producer;
import sd.tp1.client.cloud.Server;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.logging.Logger;

/**
 * Created by apontes on 4/7/16.
 */
public class SafeInvoker {
    private static final Logger LOGGER = Logger.getLogger(SafeInvoker.class.getName());
    private static final int MAX_TRIES = 3;
    private static final int WAIT_ON_FAIL = 1000;

    public static <T> T invoke(Server server, Producer<T> producer){
        for(int i = 0; i < MAX_TRIES; i++){
            try {
                return producer.call();
            }
            catch(SOAPFaultException e){
                LOGGER.warning("Server call failed! Retrying... :" + server.getUrl());

                try {
                    Thread.sleep(WAIT_ON_FAIL);
                } catch (InterruptedException e1) {
                    //Do nothing
                }
            }
        }

        LOGGER.severe("Server error: " + server.getUrl());
        return null;
    }

}
