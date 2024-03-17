package com.atcs.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.atcs.models.UserRating;

public class UserRatingUtil {
	private static Integer _userA = 0;
	private static Integer _userB = 0;
	private static Double getRatingAvg(List<Map<Integer, Double>> movieUserRatings){
		Double sum = 0.0;
		for (int i=0; i<movieUserRatings.size(); i++) {
			for(Map.Entry<Integer, Double> entry : movieUserRatings.get(i).entrySet()){
				sum+= entry.getValue();
			}
		}
		return sum;
	}


	/**
	 * This method calculates the average rating for each user in a given map containing user ratings. 
	 * 
	 * @param map Map containing user ratings 
	 * @return Returns a map where the keys represent userIDs and the values represent the average rating for each user.
	 */
	public static Map<Integer,Double> calculateUsersAverage (Map<Integer, List<Map<Integer,Double>>> map){
		Map<Integer,Double> userAverages = new HashMap<>(); 
		for (int i=1; i<map.size(); i++) {
			Double sum = 0.0; 
			List<Map<Integer, Double>> list = map.get(i);
			sum = getRatingAvg(list); 
			userAverages.put(i, sum/list.size());
		}
		return userAverages; 
	}

	/**
	 * Calculates the numerator for Pearson correlation coefficient between two users.
	 * 
	 * @param a UserID of the first user
	 * @param b UserID of the second user
	 * @param itemRatingsMap Map containing item ratings for users
	 * @param userAverage Map containing average ratings for each user
	 * @return The numerator for Pearson correlation coefficient
	 */
	public static Double getNumerator (Integer a, Integer b, Map<Integer, Map<Integer,Double>> itemRatingsMap, Map<Integer, Double> userAverage, UserRating userRatingA, UserRating userRatingB, double lambda, boolean weightedFunction){
		double bigSum = 0.0;
		double diffA = 0.0;
		double diffB = 0.0;

		for(int j=1; j<itemRatingsMap.size(); j++) {
			double ratingAUserjItem = 0.0;
			double ratingBUserjItem = 0.0;
			double avgA = 0.0;
			double avgB = 0.0;
			double weight = 1; 
			if(userRatingA != null && userRatingB != null) {
				if(userRatingA.get_timestampRating()!=null && userRatingB.get_timestampRating() != null) {
					Calendar calendar = Calendar.getInstance();
					calendar.set(1880, Calendar.JANUARY, 1, 0, 0, 0);
					long oldTimestamp = calendar.getTimeInMillis();

					Long valueA=oldTimestamp; 
					Long valueB=oldTimestamp; 
					if (userRatingA.get_timestampRating().get(j) != null) {
						valueA = userRatingA.get_timestampRating().get(j); 
					}
					if (userRatingB.get_timestampRating().get(j) != null) {
						valueB = userRatingB.get_timestampRating().get(j); 
					}
					Long max = Math.max(valueA, valueB); 
					weight = Math.exp(-lambda * Math.abs(max));
				}
			}
			Map<Integer,Double> ratingUserjItem = itemRatingsMap.get(j);
			if (ratingUserjItem != null && userAverage != null) {
				if(ratingUserjItem.get(a) != null) {
					ratingAUserjItem = ratingUserjItem.get(a);
				}
				if (userAverage.get(a) != null) {
					avgA = userAverage.get(a);
				}
				if (ratingUserjItem.get(b) != null) {
					ratingBUserjItem = ratingUserjItem.get(b);
				}
				if (userAverage.get(b) != null) {
					avgB = userAverage.get(b);
				}
			}
			diffA = ratingAUserjItem - avgA;
			diffB = ratingBUserjItem - avgB;
			if (weightedFunction == true) {
				bigSum += weight * (diffA * diffB);
			} else {
				bigSum += diffA * diffB; 
			}
		}
		return bigSum;
	}

	/**
	 * Calculates the denominator for Pearson correlation coefficient of a user.
	 * 
	 * @param a UserID for whom the denominator is calculated
	 * @param itemRatingsMap Map containing item ratings for users
	 * @param userAverage Map containing average ratings for each user
	 * @return The denominator for Pearson correlation coefficient of the user
	 */
	public static Double getUserDenom (Integer a, Map<Integer, Map<Integer,Double>> itemRatingsMap, Map<Integer, Double> userAverage){
		double sum = 0.0;
		double diffA = 0.0; 

		for(int j=1; j<itemRatingsMap.size(); j++) {
			double ratingAUserjItem = 0.0; 
			double avgA = 0.0; 
			Map<Integer,Double> ratingUserjItem = itemRatingsMap.get(j); 
			if (ratingUserjItem != null) {
				if(ratingUserjItem.get(a) != null) {
					ratingAUserjItem = ratingUserjItem.get(a); 
				}
				if (userAverage.get(a) != null) {
					avgA = userAverage.get(a); 
				}
			}
			diffA = ratingAUserjItem - avgA; 

			sum += Math.pow(diffA, 2);  
		}
		return Math.sqrt(sum); 
	}

	/**
	 * Calculates the similarity scores between users based on Pearson correlation coefficient.
	 * 
	 * @param userRatingsMap Map containing the ratings given by users for items
	 * @param itemRatingsMap Map containing item ratings for users
	 * @param userAverage Map containing average ratings for each user
	 * @return A map where each userID is mapped to another map containing similarity scores with other users
	 */
	//public static Map<Integer, Map<Integer, Double>> calculateUserSimilarity(
	public static Map<Integer, Map<Integer, Double>> calculateUserSimilarity(Map<Integer, Map<Integer, Double>> userRatingsMap, Map <Integer, Map<Integer, Double>> itemRatingsMap, Map<Integer, Double> userAverage, UserRatingTreeMap userRatingTreeMap, boolean weightedFunction) {
		Map<Integer, Map<Integer, Double>> userSimilarityMap = new HashMap<>();

		for (Integer userA : userRatingsMap.keySet()) {
			Map<Integer, Double> similarityScores = new HashMap<>();
			for (Integer userB : userRatingsMap.keySet()) {
				if (!userA.equals(userB)) {
					UserRating userRatingA = userRatingTreeMap.getUserRatings().get(userA); 
					UserRating userRatingB = userRatingTreeMap.getUserRatings().get(userB); 

					double num = getNumerator(userA, userB, itemRatingsMap, userAverage, userRatingA, userRatingB, 0.0000001, weightedFunction);


					double denom1 = getUserDenom(userA, itemRatingsMap, userAverage);
					double denom2 = getUserDenom(userB, itemRatingsMap, userAverage);

					Double similarity = 0.0;
					if (denom1 == 0.0 || denom2 == 0.0) {
						return null;
					}
					similarity = num/(denom1*denom2);
					similarityScores.put(userB, similarity);
				}
			}
			userSimilarityMap.put(userA, similarityScores);
		}
		return userSimilarityMap;
	}

	public static List<Integer> top10Users (Map<Integer, Map<Integer, Double>> map, Integer user) {
		List<Integer> listAllNeighbor = new ArrayList<>(); 
		for (Map.Entry<Integer, Double> entry : map.get(user).entrySet()) {
			listAllNeighbor.add(entry.getKey()); 
		}

		Collections.sort(listAllNeighbor, (entry1, entry2) -> entry2.compareTo(entry1));

		return listAllNeighbor.stream()
				.limit(10)
				.collect(Collectors.toList());
	}

	public static List<Integer> top10Movies (Map<Integer, Map<Integer, Double>> map, Integer movie) {
		List<Integer> listAllNeighbor = new ArrayList<>(); 
		for (Map.Entry<Integer, Double> entry : map.get(movie).entrySet()) {
			listAllNeighbor.add(entry.getKey()); 
		}

		Collections.sort(listAllNeighbor, (entry1, entry2) -> entry2.compareTo(entry1));

		return listAllNeighbor.stream()
				.limit(10)
				.collect(Collectors.toList());
	}
}