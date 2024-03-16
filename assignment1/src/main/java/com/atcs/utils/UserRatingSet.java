package com.atcs.utils;

import com.atcs.models.UserRating;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class UserRatingSet {
    private TreeSet<UserRating> userRatings;

    public UserRatingSet() {
        this.userRatings = new TreeSet<>(Comparator.comparingInt(UserRating::getUserId));
    }

    public boolean addUserRating(UserRating userRating) {
        return userRatings.add(userRating);
    }

    public boolean removeUserRating(UserRating userRating) {
        return userRatings.remove(userRating);
    }

    // Getter for the TreeSet (Optional, depending on whether you want to expose the internal structure)
    public TreeSet<UserRating> getUserRatings() {
        return userRatings;
    }

    public Map<Integer,Double> getUserAvgRatings(){
        Map<Integer,Double> usersAvgRatingMap = new HashMap<>();
        for(UserRating ur : userRatings){
            usersAvgRatingMap.put(ur.getUserId(),ur.getUserRatingAvg());
        }
        return usersAvgRatingMap;
    }

}

