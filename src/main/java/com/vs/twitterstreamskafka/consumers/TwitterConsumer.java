package com.vs.twitterstreamskafka.consumers;

import com.vs.twitterstreamskafka.configs.AppConfig;
import com.vs.twitterstreamskafka.configs.TwitterCounterConfig;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Data
public class TwitterConsumer extends Thread {
    private final Logger logger = Logger.getLogger(TwitterConsumer.class.getName());

    private AtomicBoolean isRunning;

    public TwitterConsumer() {
        this.isRunning = new AtomicBoolean(true);
    }

    /*@Scheduled(fixedDelayString = "PT3S")
    public void runner() {
        logger.warning("Consumer start...");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(TwitterCounterConfig.getProperties());
        consumer.subscribe(Arrays.asList(AppConfig.topicName));

        while(isRunning.get()) {
            logger.warning("RECEIVING...");
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(AppConfig.frequencyInSec));
            for (ConsumerRecord<String, String> record : records) {
                logger.warning("in for loop");
                System.out.println(record.value());
            }
        }
    }*/

    @Override
    public void run() {
        logger.warning("Consumer SEARCH starts...");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(TwitterCounterConfig.getProperties());
        consumer.subscribe(Arrays.asList(AppConfig.topicName));

        while(isRunning.get()) {
            logger.warning("RECEIVING...");
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(AppConfig.frequencyInSec));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }
    }

    public void stopThread() {
        isRunning.set(false);
    }
}
