package com.vs.twitterstreamskafka;

import com.vs.twitterstreamskafka.models.UserRequest;
import com.vs.twitterstreamskafka.producers.TwitterProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class TwitterService {
    //private final TwitterProducer producer;

    public void startNewSearch(UserRequest request) {
        //producer.run();
        System.err.println("Starting search by string: " + request.getTextToSearch());
        new TwitterProducer(request.getTextToSearch()).run();
    }
}
