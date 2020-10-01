package com.example.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.Userrating;
import com.example.moviecatalogservice.service.MovieInfo;
import com.example.moviecatalogservice.service.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

// Fault tolerant and resilance in microservice architecture
@RestController
@RequestMapping("/catalog")
public class MovieCatalogResources {

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{userId}")
    // Tell this is where we need to break circuit if something goes wrong
    // fallbackmethod will be called if circuit breaks
    // As this funtion calls to APIs therefore it's better to create fallback for
    // each method call than on the main method
    // @HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalogItems(@PathVariable("userId") String userId) {

        Userrating userrating = userRatingInfo.getUserRating(userId);

        return userrating.getUserRating().stream().map(rating -> {
            return movieInfo.getCatalogItem(rating);
        }).collect(Collectors.toList());

    }
    // Setting circuit breaker for each method
    // It won't work because of how hysterix is designed. Hystric can intercept only
    // the instance of a bean which springframework calls and not the method which
    // is internally called by a method
    // if methods and fallbacks were defined as a normal member method

    // // This is the fallBackMethod which needs to have the same method signature
    // public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String
    // userId) {

    // return Arrays.asList(new CatalogItem("No movie", "", 0));
    // }

}

// Service discovery and communication of microservices
// @RestController
// @RequestMapping("/catalog")
// public class MovieCatalogResources {

// // @Autowired
// // private DiscoveryClient discoveryClient;

// @Autowired
// private RestTemplate restTemplate;

// // @Autowired
// // private WebClient.Builder webClienBuilder;

// @RequestMapping("/{userId}")
// public List<CatalogItem> getCatalogItems(@PathVariable("userId") String
// userId) {

// // RestTemplate restTemplate = new RestTemplate(); (bad thing as it is
// getting
// // created for every request, so we need a bean that makes it singleton)
// // Movie movie =
// restTemplate.getForObject("http://localhost:8081/movies/foo",
// // Movie.class);
// // System.out.println(movie);

// // getting rating detail api 2
// Userrating userrating =
// restTemplate.getForObject("http://ratingsdata-service/ratingsdata/user/" +
// userId,
// Userrating.class);

// return userrating.getUserRating().stream().map(rating -> {

// // getting movie detail api call 2
// // resttemplate way
// Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" +
// rating.getMovieId(),
// Movie.class);

// /**
// * WebClienBuilder is the new way of sending requests and getting responses
// it's
// * in the reactive web dependency RestTemplate is deprecated and it's the new
// * way of doing stuff
// */
// // Movie movie = webClienBuilder.build()
// // .get()
// // .uri("http://localhost:8081/movies/" + rating.getMovieId())
// // .retrieve()
// // .bodyToMono(Movie.class)
// // .block();

// // putting all together
// return new CatalogItem(movie.getName(), movie.getOverView(),
// rating.getRating());
// }).collect(Collectors.toList());

// // return Collections.singletonList(new CatalogItem("Titanic", "nice", 4));
// }

// }
