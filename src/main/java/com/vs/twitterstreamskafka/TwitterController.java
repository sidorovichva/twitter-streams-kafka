package com.vs.twitterstreamskafka;

import com.vs.twitterstreamskafka.models.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(TwitterController.URI)
@RequiredArgsConstructor
public class TwitterController {
    public static final String URI = "/search";

    private final TwitterService service;

    @PostMapping
    public void addPurchase(@RequestBody UserRequest request) {
        System.err.println("CONTROLLER" + request);
        service.startNewSearch(request);
    }
}
