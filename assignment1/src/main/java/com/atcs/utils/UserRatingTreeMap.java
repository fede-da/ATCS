package com.atcs.utils;

import com.atcs.models.UserRating;

import java.util.*;

public class UserRatingTreeMap {
    private TreeMap<Integer,UserRating> userRatings;

    public UserRatingTreeMap() {
        this.userRatings = new TreeMap<>();
    }

    public void addUserRating(int userId, int movieId, double movieRating, long _timestamp) {
        if (userRatings.get(userId) == null) {
            // If the userId does not exist, create a new UserRating and put it in the map
            userRatings.put(userId, new UserRating(userId, movieId, movieRating, _timestamp));
        } else {
            // If the userId exists, add the movie rating to the existing UserRating
            userRatings.get(userId).addMovieWithRating(movieId, movieRating);
        }
    }

    public TreeMap<Integer,UserRating> getUserRaings() {
        return userRatings;
    }

    public Map<Integer,Double> getUserAvgRatings(){
        Map<Integer,Double> usersAvgRatingMap = new HashMap<>();
        for(Map.Entry<Integer, UserRating> ur : userRatings.entrySet()){
            usersAvgRatingMap.put(ur.getValue().getUserId(),ur.getValue().getUserRatingAvg());
        }
        return usersAvgRatingMap;
    }
}

