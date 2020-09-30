package com.example.ratingsdataservice.resources;

import java.util.Arrays;
import java.util.List;

import com.example.ratingsdataservice.models.Rating;
import com.example.ratingsdataservice.models.Userrating;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsResource {

    @RequestMapping("/{movieId}")
    public Rating getRating(@PathVariable("movieId") String movieId) {
        return new Rating(movieId, 90);
    }

    @RequestMapping("/user/{userId}")
    public Userrating getRatingForuser(@PathVariable("userId") String userId) {
        List<Rating> ratings = Arrays.asList(new Rating("440", 5), new Rating("435", 3), new Rating("440", 440),
                new Rating("435", 3));

        Userrating userrating = new Userrating();
        userrating.setUserRating(ratings);
        return userrating;
    }

}
