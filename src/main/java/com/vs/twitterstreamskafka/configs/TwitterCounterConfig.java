package com.vs.twitterstreamskafka.configs;

import com.vs.twitterstreamskafka.configs.JsonDeserializer;
import com.vs.twitterstreamskafka.models.Tweet;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class TwitterCounterConfig {

    public static Properties getProperties() {
        Properties properties = new Properties();
        /*properties.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfig.applicationID);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");*/

        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, AppConfig.applicationID);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //properties.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, Tweet.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, AppConfig.groupID);
        //properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

}
