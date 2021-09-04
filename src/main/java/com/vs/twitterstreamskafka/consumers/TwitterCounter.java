package com.vs.twitterstreamskafka.consumers;

import com.vs.twitterstreamskafka.configs.AppConfig;
import com.vs.twitterstreamskafka.configs.TwitterCounterConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class TwitterCounter extends Thread {
    private final Logger logger = Logger.getLogger(TwitterConsumer.class.getName());

    private AtomicBoolean isRunning;
    private Long counter;

    public TwitterCounter() {
        this.counter = 0L;
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
        logger.warning("Consumer COUNTER starts...");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(TwitterCounterConfig.getProperties());
        consumer.subscribe(Arrays.asList(AppConfig.topicName));

        while(isRunning.get()) {
            logger.warning("RECEIVING...");
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(AppConfig.frequencyInSec));
            for (ConsumerRecord<String, String> record : records) {
                counter += 1;
            }
            System.out.println("Overall tweets found: " + counter);
        }
    }

    public void stopThread() {
        isRunning.set(false);
    }
}
