package com.atcs.models;

import java.util.*;

public class UserRating {
	private int _userId;
	public Map<Item, Double> _movieRatings = new HashMap<>();

	private long _timestamp;

	public UserRating(int userId, int movieId, double movieRating, long timestamp){
		_userId=userId;
		this.addMovieWithRating(movieId,movieRating);
		_timestamp=timestamp;
	}

	public UserRating(String[] userRatingRow){
		_userId = Integer.parseInt(userRatingRow[0]);
		this.addMovieWithRating(Integer.parseInt(userRatingRow[1]),Double.parseDouble(userRatingRow[2]));
		_timestamp=Long.parseLong(userRatingRow[3]);
	}

	public int getUserId() {
		return _userId;
	}
	public void setUserId(int userId) {
		this._userId = userId;
	}

	public long getTimestamp() {
		return _timestamp;
	}
	public void setTimestamp(long timestamp) {
		this._timestamp = timestamp;
	}
	public void addMovieWithRating(Integer movieId, Double movieRating){
		_movieRatings.put(new Item(movieId), movieRating);
	}
	public Map<Item, Double> getMovieRatings() {
		return _movieRatings;
	}
	public Double getUserRatingAvg(){
		Double _sum = 0.0;
		for (Map.Entry<Item, Double> entry : _movieRatings.entrySet()) {
			_sum+= entry.getValue();
		}
		return _sum/_movieRatings.size();
	}
	
	@Override
	public String toString() {
		return "UserRating [userId=" + _userId + ", movieRatings=" + _movieRatings + ", timestamp=" + _timestamp + "]";
	}

	public Map<Integer, Double> getMovieRatingAsInteger(){
		Map<Integer,Double> toReturn = new HashMap<>();
		for(Map.Entry<Item,Double> entry:_movieRatings.entrySet()){
			toReturn.put(entry.getKey().getId(),entry.getValue());
		}
		return toReturn;
	}
	
}
