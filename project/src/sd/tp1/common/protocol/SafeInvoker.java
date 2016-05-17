package sd.tp1.common.protocol;

import org.glassfish.jersey.internal.util.Producer;
import sd.tp1.client.cloud.Server;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by apontes on 4/7/16.
 */
public class SafeInvoker {
    private static final Logger LOGGER = Logger.getLogger(SafeInvoker.class.getName());
    private static final int MAX_TRIES = 3;
    private static final int WAIT_ON_FAIL = 1000;

    private static final boolean VERBOSE = false;

    public static <T> T invoke(Endpoint endpoint, Producer<T> producer){
        for(int i = 0; i < MAX_TRIES; i++){
            try {
                return producer.call();
            }
            catch(Exception e){
                if(VERBOSE){
                    String stack = Arrays.asList(e.getStackTrace())
                            .stream()
                            .map((x) -> (x).toString())
                            .collect(Collectors.joining("\n"));

                    LOGGER.warning("Server call failed! Retrying... :" + endpoint.getUrl() + "\n\n" + stack + "\n\n");
                }
                else{
                    LOGGER.warning("Server call failed! Retrying... :" + endpoint.getUrl());
                }


                try {
                    Thread.sleep(WAIT_ON_FAIL);
                } catch (InterruptedException e1) {
                   //NOTHING IS HERE
                }
            }
        }

        LOGGER.severe("Server error: " + endpoint.getUrl());
        return null;
    }

}
