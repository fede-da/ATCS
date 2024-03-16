package com.atcs.recommender;

import com.atcs.models.Item;
import com.atcs.models.UserRating;
import com.atcs.utils.ItemRatingTreeMap;
import com.atcs.utils.UserRatingTreeMap;
import com.atcs.utils.UserRatingUtil;

import java.util.Map;

public class Recommender {
    private static double predictUserRatingOnItemNumerator(UserRatingTreeMap _userRatingsMap,ItemRatingTreeMap _itemRatingsMap, UserRating ur){
        double sim = 0.0;
        double ratingAUserjItem = 0.0;
        double avgA = 0.0;
        double diffA = 0.0;
        double numeratorSum = 0.0;
        for(Integer currentUserId : _userRatingsMap.getUserRatings().keySet()) {
            //Map<Integer, Double> tmpMap = UserRatingUtil.calculateUserSimilarity(_userRatingsMap, _itemRatingsMap, _userRatingsMap.getUserAvgRatings()).get(ur.getUserId());
            Map<Integer, Double> tmpMap = UserRatingUtil.calculateUserSimilarity(_userRatingsMap.getUserRatingsMap(), _itemRatingsMap.getItemRatingsMap(), _userRatingsMap.getUserAvgRatings()).get(ur.getUserId());
            if(tmpMap.get(currentUserId)!=null){
                sim = tmpMap.get(currentUserId);
                Map<Integer, Double> ratingUserjItem = _itemRatingsMap.getItemRatingsMap().get(currentUserId);
                if (ratingUserjItem != null) {
                    if (ratingUserjItem.get(currentUserId) != null) {
                        ratingAUserjItem = ratingUserjItem.get(currentUserId);
                    }
                    Map<Integer, Double> userAverage = _userRatingsMap.getUserAvgRatings();
                    if (userAverage.get(currentUserId) != null) {
                        avgA = userAverage.get(currentUserId);
                    }
                }
                diffA = ratingAUserjItem - avgA;
                numeratorSum += diffA * sim;
            }
        }
        return numeratorSum;
    }
    public static double predictUserRatingOnItem(UserRatingTreeMap _userRatingsMap,
                                                 ItemRatingTreeMap _itemRatingsMap,
                                                 UserRating ur,
                                                 Item i){
        double userAverage = ur.getUserRatingAvg();
        double numerator = predictUserRatingOnItemNumerator(_userRatingsMap, _itemRatingsMap, ur);
        double denumerator = 0.0;
        for(Integer currentUserId : _userRatingsMap.getUserRatings().keySet()){
            Map<Integer, Map<Integer, Double>> tmpMap = UserRatingUtil.calculateUserSimilarity(_userRatingsMap.getUserRatingsMap(),_itemRatingsMap.getItemRatingsMap(),_userRatingsMap.getUserAvgRatings());
            Map<Integer, Double> tmpMapInner =tmpMap.get(ur.getUserId());
            if(tmpMapInner!=null){
                if(tmpMapInner.get(currentUserId)!=null)
                    denumerator += tmpMap.get(ur.getUserId()).get(currentUserId);
            }
        }
        if(denumerator!=0.0)
            return userAverage + (numerator/denumerator);
        throw new ArithmeticException("hjfsdhjlfgjgh");
    }
}
