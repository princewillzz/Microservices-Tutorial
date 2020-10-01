package com.example.moviecatalogservice.service;

import java.util.Arrays;

import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.Userrating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserRatingInfo {

    @Autowired
    RestTemplate restTemplate;

    // Intelligent Circuit breaker way to handle fault tolerance and resilience
    // @HystrixCommand(fallbackMethod = "getFallbackUserRating", commandProperties =
    // {
    // // There are many more properties just look that up
    // // setting timeout
    // @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
    // value = "2000"),
    // // Setting how many request to consider for circuit breaking
    // @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value =
    // "5"),
    // // Setting the percentage of error which lead the circuit to break
    // @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value =
    // "50"),
    // // Setting the time for which the service should sleep when the circuit
    // breaks
    // @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value =
    // "5000") })

    //
    // Bulkhead Way of handing Fault tolerance and resilence
    @HystrixCommand(fallbackMethod = "getFallbackUserRating",
            // Creating bulkhead
            // Assigning the key means creating a separate thread pool
            threadPoolKey = "movieInfoPool",
            // One's you create the bulkhead then configure that bulkhead
            threadPoolProperties = {
                    // How many threads maxs can be waiting in the thread pool
                    @HystrixProperty(name = "coreSize", value = "20"),
                    // how many requests are waiting in the queue before they gets access to the
                    // thread. This can also be configured
                    // Any thing out of that will go to the fallback
                    @HystrixProperty(name = "maxQueueSize", value = "10") })
    public Userrating getUserRating(String userId) {
        return restTemplate.getForObject("http://ratingsdata-service/ratingsdata/user/" + userId, Userrating.class);
    }

    public Userrating getFallbackUserRating(String userId) {
        Userrating userRating = new Userrating();
        userRating.setUserRating(Arrays.asList(new Rating("0", 0)));
        return userRating;
    }
}
