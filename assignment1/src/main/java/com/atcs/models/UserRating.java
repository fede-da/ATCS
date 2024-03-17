package com.atcs.models;

import java.util.*;

public class UserRating {
	private int _userId;
	public Map<Item, Double> _movieRatings = new HashMap<>();
	private Map<Integer, Long> _timestampRating = new HashMap<>();

	public UserRating() {
		super();
	}
	public UserRating(int userId, int movieId, double movieRating, long timestamp){
		_userId=userId;
		this.addMovieWithRating(movieId,movieRating);
		this.addMovieWithTimestamp(movieId, timestamp);
	}
	public UserRating(String[] userRatingRow){
		_userId = Integer.parseInt(userRatingRow[0]);
		this.addMovieWithRating(Integer.parseInt(userRatingRow[1]),Double.parseDouble(userRatingRow[2]));
		this.addMovieWithTimestamp(Integer.parseInt(userRatingRow[1]), Long.parseLong(userRatingRow[3]));
	}
	
	public void addMovieWithRating(Integer movieId, Double movieRating){
		_movieRatings.put(new Item(movieId), movieRating);
	}
	
	public void addMovieWithTimestamp(int movieId, long timestamp){
		_timestampRating.put(movieId, timestamp);
	}
	
	public int getUserId() {
		return _userId;
	}
	public void setUserId(int userId) {
		this._userId = userId;
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
	
	public Map<Integer, Long> get_timestampRating() {
		return _timestampRating;
	}

	public void set_timestampRating(Map<Integer, Long> _timestampRating) {
		this._timestampRating = _timestampRating;
	}

	@Override
	public String toString() {
		return "UserRating [_userId=" + _userId + ", _movieRatings=" + _movieRatings + ", _timestampRating="
				+ _timestampRating + "]";
	}

	public Map<Integer, Double> getMovieRatingAsInteger(){
		Map<Integer,Double> toReturn = new HashMap<>();
		for(Map.Entry<Item,Double> entry:_movieRatings.entrySet()){
			toReturn.put(entry.getKey().getId(),entry.getValue());
		}
		return toReturn;
	}
	
}
