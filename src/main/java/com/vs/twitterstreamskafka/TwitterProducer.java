package com.vs.twitterstreamskafka;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TwitterProducer implements Runnable {
    private final Logger logger = Logger.getLogger(TwitterProducer.class.getName());

//    private final String consumerKey = "5bx40OGe7nK75ACepmsekWJ9B";
//    private final String consumerSecret = "m9haJEy4zQNHt86IGx0zR94YSjnkXsFeSUJzol2cg6QXTxXuit";
//    private final String token = "1427981997302034438-FKIRjhamBO8i36ehbYCvJXXbq34nf9";
//    private final String secret = "YW7oiY9rG0BvEcgtpW1G7gRyWZVflt5alOPPzNyJ6FGRk";

    @Override
    public void run() {
        logger.info("Initialization...");
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(1000);

        Client client = createTwitterClient(msgQueue);
        client.connect();

        KafkaProducer<String, String> producer = createKafkaProducer();

        while (!client.isDone()) {
            String msg = null;
            try {
                msg = msgQueue.poll(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }
            if (msg != null) {
                producer.send(new ProducerRecord<>(AppConfig.topicName, null, msg), new Callback() {
                    @Override //to catch errors
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e != null) {
                            logger.warning("Something bad happened " + e.getMessage());
                        }
                    }
                });
            } else logger.info("The message is empty");
        }
        logger.info("End of application");
    }

    public Client createTwitterClient(BlockingQueue<String> msgQueue) {
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        List<String> terms = Lists.newArrayList("kafka");
        hosebirdEndpoint.trackTerms(terms);

        Authentication hosebirdAuth = new OAuth1(
                SecurityConfig.consumerKey,
                SecurityConfig.consumerSecret,
                SecurityConfig.token,
                SecurityConfig.secret
        );

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        Client hosebirdClient = builder.build();

        return hosebirdClient;
    }

    public KafkaProducer<String, String> createKafkaProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(properties);
    }
}
