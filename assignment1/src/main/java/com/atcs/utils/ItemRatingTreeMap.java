package com.atcs.utils;

import com.atcs.models.Item;
import com.atcs.models.UserRating;

import java.util.*;

public class ItemRatingTreeMap {
    private TreeMap<Integer, Item> itemRatings;

    public ItemRatingTreeMap() {
        this.itemRatings = new TreeMap();
    }

    public ItemRatingTreeMap(UserRatingTreeMap userRatingTreeMap){
        this.itemRatings = new TreeMap();
        // For each UserRating
        for (Map.Entry<Integer,UserRating> ur : userRatingTreeMap.getUserRaings().entrySet()) {
            // iterate over its item ratings
            for (Map.Entry<Item, Double> entry : ur.getValue().getMovieRatings().entrySet()) {
                Item currentItem = entry.getKey();
                Double rating = entry.getValue();
                int itemId = currentItem.getId();

                // If the item already exists in the TreeMap, update the rating; otherwise, add a new Item
                Item itemInMap = itemRatings.get(itemId);
                if (itemInMap == null) {
                    itemInMap = new Item(itemId);
                    itemRatings.put(itemId, itemInMap);
                }
                itemInMap.userIdAndItemRating.put(ur.getValue().getUserId(), rating);
            }
        }
    }

    //TODO: Complete below if required
//    public Double getItemAvgRatings() {
//        Double ab
//        for (Item item : itemRatings) {
//            double average = item.userRatings.values().stream()
//                    .mapToDouble(Double::doubleValue)
//                    .average()
//                    .orElse(0.0);
//            itemsAvgRatingMap.put(item.getId(), average);
//        }
//        return itemsAvgRatingMap;
//    }
}
