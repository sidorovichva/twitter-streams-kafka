package com.vs.twitterstreamskafka.producers;

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
import com.vs.twitterstreamskafka.configs.AppConfig;
import com.vs.twitterstreamskafka.configs.KafkaProducerConfig;
import com.vs.twitterstreamskafka.configs.SecurityConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.*;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TwitterProducer implements Runnable {
    private final Logger logger = Logger.getLogger(TwitterProducer.class.getName());

    private String stringToSearch;

    @Override
    public void run() {
        logger.info("Initialization...");
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(1000);

        Client client = createTwitterClient(msgQueue);
        client.connect();

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(KafkaProducerConfig.getProperties());

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

        //List<String> terms = Lists.newArrayList("kafka");
        List<String> terms = Lists.newArrayList(stringToSearch == null ? "kafka" : stringToSearch);
        System.err.println(terms.get(0) + " = " + stringToSearch);
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
}
