package sd.tp1.client.cloud.aux;

import jersey.repackaged.com.google.common.collect.Lists;
import org.glassfish.jersey.internal.util.Producer;
import sd.tp1.client.cloud.Server;

import javax.xml.ws.soap.SOAPFaultException;
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

    public static <T> T invoke(Server server, Producer<T> producer){
        for(int i = 0; i < MAX_TRIES; i++){
            try {
                return producer.call();
            }
            catch(Exception e){
                String stack = Arrays.asList(e.getStackTrace())
                        .stream()
                        .map((x) -> (x).toString())
                        .collect(Collectors.joining("\n"));

                LOGGER.warning("Server call failed! Retrying... :" + server.getUrl() + "\n\n" + stack + "\n\n");

                try {
                    Thread.sleep(WAIT_ON_FAIL);
                } catch (InterruptedException e1) {
                   //NOTHING IS HERE
                }
            }
        }

        LOGGER.severe("Server error: " + server.getUrl());
        return null;
    }

}
