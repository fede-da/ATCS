package com.atcs.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.atcs.models.Item;
import com.atcs.models.UserRating;

public class ItemRatingTreeMap {
	private TreeMap<Integer, Item> itemRatings;

	public ItemRatingTreeMap() {
		this.itemRatings = new TreeMap();
	}

	public ItemRatingTreeMap(UserRatingTreeMap userRatingTreeMap){
		this.itemRatings = new TreeMap();
		// For each UserRating
		for (Map.Entry<Integer,UserRating> ur : userRatingTreeMap.getUserRatings().entrySet()) {
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

	public Map<Integer, Map<Integer,Double>> getItemRatingsMap(){
		Map<Integer, Map<Integer,Double>> to_return= new HashMap<>();
		for(Map.Entry<Integer,Item> _itemRatingEntry : itemRatings.entrySet()){
			Map<Integer,Double> tmpMap = new HashMap<>();
			tmpMap= _itemRatingEntry.getValue().getUserIdAndItemRating();
			to_return.put(_itemRatingEntry.getKey(),tmpMap);
		}
		return to_return;
	}

	public Item getItemById(int itemId){
		return this.itemRatings.get(itemId);
	}

	Map<Integer,Double> getInnerItemRatingsMapByItemId(Integer itemId){
		return this.getItemRatingsMap().get(itemId);
	}

	public Integer getRandomItem() throws Exception {
		Random random = new Random(); 
		int n = random.nextInt(this.itemRatings.size())+1; 
		int i = 0;
		for (Map.Entry<Integer, Item> entry : itemRatings.entrySet()) {
			if (i++ == n) {
				return entry.getKey();
			}
		} throw new Exception("Cannot get random item"); 

	}
}