package com.atcs.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Item implements Cloneable {
    int id;
    public Map<Integer, Double> userIdAndItemRating;

    public Item(int id) {
        this.id = id;
        this.userIdAndItemRating = new HashMap<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Item clonedItem = (Item) super.clone(); // This clones the Item object.
        // Now clone the Map to ensure the cloned Item has a separate Map instance.
        clonedItem.userIdAndItemRating = new HashMap<>(this.userIdAndItemRating);
        return clonedItem;
    }
}
