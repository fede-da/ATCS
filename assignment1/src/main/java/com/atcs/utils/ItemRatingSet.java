package com.atcs.utils;

import com.atcs.models.Item;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

public class ItemRatingSet {
    private TreeSet<Item> itemRatings;

    public ItemRatingSet() {
        this.itemRatings = new TreeSet<>(Comparator.comparingInt(Item::getId));
    }

    public boolean addItemRating(Item item) {
        return itemRatings.add(item);
    }

    public boolean removeItemRating(Item item) {
        return itemRatings.remove(item);
    }

    // Getter for the TreeSet (Optional, depending on whether you want to expose the internal structure)
    public TreeSet<Item> getItemRatings() {
        return itemRatings;
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
