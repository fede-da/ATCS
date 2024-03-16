package com.atcs.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Item {
    public Item(int id) {
        this.id = id;
        userIdAndItemRating = new HashMap<>();
    }
    int id;
    public Map<Integer, Double> userIdAndItemRating;

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
}
