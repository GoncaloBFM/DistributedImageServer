package sd.tp1.common.notifier;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by apontes on 5/22/16.
 */
public class KafkaPublisher implements Publisher {


    private KafkaProducer<String, String> producer;
    private final BlockingQueue<ProducerRecord<String, String>> queue = new LinkedBlockingQueue<>();

    public KafkaPublisher(){
        Properties props = new Properties();

        //props.put("bootstrap.servers", "240.255.255.255:9092");
        props.put("bootstrap.servers", "localhost:9092");

        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        props.put("log.retention.ms", 5000);

        producer = new KafkaProducer<>(props);


        new Thread(){
            @Override
            public void run(){
                for(;;){
                    try {
                        producer.send(queue.take());
                        producer.flush();
                    } catch (InterruptedException e) {
                        //
                    }
                }


            }
        }.start();
    }


    @Override
    public void notifyAlbumUpdate(String album) {
        queue.add(new ProducerRecord<>("album", album));
    }

    @Override
    public void notifyPictureUpdate(String album, String picture) {
        queue.add(new ProducerRecord<>("picture",
                String.format("%s/%s", album, picture)));

    }
}
