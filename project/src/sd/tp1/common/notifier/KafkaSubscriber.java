package sd.tp1.common.notifier;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by apontes on 5/22/16.
 */
public class KafkaSubscriber implements Subscriber {

    KafkaConsumer<String, String> consumer;
    Queue<EventHandler> handlerList = new ConcurrentLinkedQueue<>();

    public KafkaSubscriber(){
        Properties props = new Properties();

        props.put("bootstrap.servers", "240.255.255.255:9092");

        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        props.put("group.id", UUID.randomUUID().toString());

        consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList("album", "picture"));

        new Thread() {
            @Override
            public void run(){
                for(;;)
                    consumer.poll(1000).forEach(x -> handleEvent(x.topic(), x.value()));
            }
        }.start();
    }

    private void handleEvent(String topic, String data){
        try {
            switch (topic) {
                case "album":
                    handlerList.forEach(x -> x.onAlbumUpdate(data));
                    break;

                case "picture":
                    String[] split = data.split("/");
                    handlerList.forEach(x -> x.onPictureUpdate(split[0], split[1]));
                    break;
            }
        }
        catch(Exception e){
            //
        }
    }

    @Override
    public void addEventHandler(EventHandler handler) {
        handlerList.add(handler);
    }
}
