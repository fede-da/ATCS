package com.atcs;

public class UserRating {
	private int userId; 
	private int movieId; 
	private double rating; 
	private long timestamp;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getMovieId() {
		return movieId;
	}
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "Dato [userId=" + userId + ", movieId=" + movieId + ", rating=" + rating + ", timestamp=" + timestamp
				+ "]";
	} 
}
