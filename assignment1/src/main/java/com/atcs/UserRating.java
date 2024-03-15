package com.atcs;

import java.util.*;

public class UserRating {
	private int _userId;
	public Map<Integer, Double> _movieRatings = new HashMap<>();

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
		_movieRatings.put(movieId, movieRating);
	}
	public Map<Integer, Double> getMovieRatings() {
		return _movieRatings;
	}
	
	@Override
	public String toString() {
		return "UserRating [userId=" + _userId + ", movieRatings=" + _movieRatings + ", timestamp=" + _timestamp + "]";
	}

	
}
