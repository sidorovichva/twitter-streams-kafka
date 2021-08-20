package com.vs.twitterstreamskafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwitterStreamsKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitterStreamsKafkaApplication.class, args);
        TwitterProducer producer = new TwitterProducer();
        producer.run();
    }

}
