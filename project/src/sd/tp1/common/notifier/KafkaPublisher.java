package sd.tp1.common.notifier;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import sd.tp1.common.data.Album;
import sd.tp1.common.data.AlbumPicture;

import java.util.Properties;

/**
 * Created by apontes on 5/22/16.
 */
public class KafkaPublisher implements Publisher {


    KafkaProducer<String, String> producer;

    public KafkaPublisher(){
        Properties props = new Properties();

        props.put("bootstrap.servers", "240.255.255.255:9092");

        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        props.put("log.retention.ms", 5000);

        producer = new KafkaProducer(props);
    }


    @Override
    public void notifyAlbumUpdate(Album album) {
        ProducerRecord<String, String> data = new ProducerRecord<String, String>("album", album.getName());
        producer.send(data);
        producer.flush();
    }

    @Override
    public void notifyPictureUpdate(AlbumPicture picture) {
        ProducerRecord<String, String> data = new ProducerRecord<String, String>("picture",
                String.format("%s/%s", picture.getAlbum().getName(), picture.getPicture().getPictureName()));
        producer.send(data);
        producer.flush();

    }
}
