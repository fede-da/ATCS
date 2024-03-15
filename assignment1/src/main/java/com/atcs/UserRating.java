package com.atcs;

import java.util.*;

public class UserRating {
	private int userId;
	public Map<Integer, Double> movieRatings = new HashMap<>();

	private long timestamp;

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public void addMovieWithRating(Integer movieId, Double movieRating){
		movieRatings.put(movieId, movieRating);
	}
	public Map<Integer, Double> getMovieRatings() {
		return movieRatings;
	}
	
	@Override
	public String toString() {
		return "UserRating [userId=" + userId + ", movieRatings=" + movieRatings + ", timestamp=" + timestamp + "]";
	}

	
}
