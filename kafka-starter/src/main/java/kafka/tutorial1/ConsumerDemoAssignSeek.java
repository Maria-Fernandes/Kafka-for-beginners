package kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoAssignSeek {
    public static void main(String[] args) {

        final Logger logger= LoggerFactory.getLogger(ConsumerDemoAssignSeek.class.getName());
        String topic="second-topic";
        // create consumer configs

        Properties properties=new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");


        // create consumer
        KafkaConsumer<String,String> consumer= new KafkaConsumer<String, String>(properties);

        // assign and seek are used to replay data or fetch a particular message

        //assign
        TopicPartition partitionToReadFrom = new TopicPartition(topic,0);
        long offsetToReadFrom=15L;
        consumer.assign(Arrays.asList(partitionToReadFrom));

        //seek
        consumer.seek(partitionToReadFrom,offsetToReadFrom);
        // poll for new data

        int numberOfMessagesToRead=5;
        boolean keepOnReading=true;
        int numberOfMessagesReadSoFar=0;


        while(keepOnReading){
            ConsumerRecords<String,String> records=consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String,String> record:records){
                numberOfMessagesReadSoFar+=1;
                logger.info("Key: "+ record.key()+" , Value: "+record.value()+" ");
                logger.info("Partition: "+ record.partition()+" , Offset: "+record.offset()+" ");
                if(numberOfMessagesReadSoFar>=numberOfMessagesToRead){
                    keepOnReading=false; // to exit while loop
                    break;//exit for loop
                }
            }
        }

        logger.info("exiting the application");

    }
}
