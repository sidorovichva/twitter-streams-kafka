package com.vs.twitterstreamskafka;

import com.vs.twitterstreamskafka.models.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TwitterController.URI)
@RequiredArgsConstructor
public class TwitterController {
    public static final String URI = "/search";

    private final TwitterService service;

    @PostMapping
    public void tweetSearch(@RequestBody UserRequest request) {
        service.startNewSearch(request);
    }

    @PostMapping("/count")
    public void tweetCount(@RequestBody UserRequest request) {
        service.startNewCount(request);
    }
}
