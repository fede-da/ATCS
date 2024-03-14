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
	/**
	 * This method performs addition on two integers.
	 *
	 * @return Returns user's movies ratings average
	 */
	public Double getRatingAvg(){
		Double sum = 0.0;
		for(Map.Entry<Integer, Double> entry : movieRatings.entrySet()){
			sum+= entry.getValue();
		}
		return sum/movieRatings.size();
	}

}
