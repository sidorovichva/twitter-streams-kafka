package com.vs.twitterstreamskafka;

import com.vs.twitterstreamskafka.consumers.TwitterConsumer;
import com.vs.twitterstreamskafka.consumers.TwitterCounter;
import com.vs.twitterstreamskafka.models.UserRequest;
import com.vs.twitterstreamskafka.producers.TwitterProducer;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class TwitterService {
    private TwitterProducer producer;

    public TwitterService() {
        this.producer = null;
    }

    public void startNewSearch(UserRequest request) {
        System.err.println("Starting search by string: " + request.getTextToSearch());

        if (producer != null) {
            producer.stopThread();
        }
        producer = new TwitterProducer(request.getTextToSearch());
        producer.start();

        new TwitterConsumer().start();
    }

    public void startNewCount(UserRequest request) {
        System.err.println("Starting counting by string: " + request.getTextToSearch());

        if (producer != null) {
            producer.stopThread();
        }
        producer = new TwitterProducer(request.getTextToSearch());
        producer.start();

        new TwitterCounter().start();
    }
}