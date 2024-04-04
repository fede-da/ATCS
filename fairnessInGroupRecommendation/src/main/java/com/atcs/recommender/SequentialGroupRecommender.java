package com.atcs.recommender;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atcs.utils.ItemRatingTreeMap;
import com.atcs.utils.UserRatingTreeMap;

public class SequentialGroupRecommender {
	 /**
	 * This method returns a sequence of recommendations for a group of users.
	 * The sequence starts with the item having the highest average rating within the group,
	 * then iteratively adds subsequent items based on the average satisfaction of users in the group.
	 * @param group List of users for whom recommendations are desired
	 * @param userRatingTreeMap Map of user ratings
	 * @param itemRatingsMap Map of item ratings
	 * @return A set of sequential recommendations for the group of users
	 */
	public static Set<Integer> sequentialRecommendations(List<Integer> group, UserRatingTreeMap userRatingTreeMap, ItemRatingTreeMap itemRatingsMap) {
		Integer maxAvgForItem = GroupRecommender.getGroupRecommendations(group, userRatingTreeMap, itemRatingsMap, true, false, false).get(0);

		Set<Integer> sequence = new HashSet<>();
		sequence.add(maxAvgForItem); 

		while (sequence.size() < 10) { 
			Map<Integer, Double> userSatisfaction = new HashMap<>();
			for (Integer user : group) {
				double satisfaction = getSatisfaction(user, sequence, userRatingTreeMap, itemRatingsMap);
				userSatisfaction.put(user, satisfaction);
			}
			Integer nextItem = findNextItemToAdd(group, sequence, userSatisfaction, userRatingTreeMap, itemRatingsMap);
			sequence.add(nextItem);
		}
		return sequence; 
	}
	
	/**
	 * This method calculates the satisfaction level of a user based on a set of recommended items.
	 * Satisfaction is computed by considering the user's top ratings and the intersection with the recommended items.
	 * @param user The user whose satisfaction we're calculating
	 * @param S Set of recommended items
	 * @param userRatingTreeMap Map of user ratings
	 * @param itemRatingTreeMap Map of item ratings
	 * @return The satisfaction level of the user with the recommended items
	 */
	private static double getSatisfaction(Integer user, Set<Integer> S, UserRatingTreeMap userRatingTreeMap, ItemRatingTreeMap itemRatingTreeMap) {
		Set<Integer> M = getTopRatingsForUser(user, userRatingTreeMap, 10); 
		double[] numAndDenom = calculateNumAndDenom(user, userRatingTreeMap, S, M);
		if (numAndDenom[1]==0.0) {
			return 0; 
		}
		return numAndDenom[0] / numAndDenom[1];
	}

	/**
	 * This method calculates the numerator and denominator for user satisfaction calculation.
	 * @param user The user for whom the numerator and denominator are calculated
	 * @param userRatingTreeMap Map of user ratings
	 * @param S Set of recommended items
	 * @param M Set of top-rated items for the user
	 * @return An array containing the numerator and denominator for satisfaction calculation
	 */
	private static double[] calculateNumAndDenom(int user, UserRatingTreeMap userRatingTreeMap, Set<Integer> S, Set<Integer> M) {
		double numerator = 0;
		double denominator = 0;

		for (int i : S) {
			if (userRatingTreeMap.getUserRatings().get(user).getMovieRatingAsInteger().get(i)!=null) {
				numerator += userRatingTreeMap.getUserRatings().get(user).getMovieRatingAsInteger().get(i); 
			}
		}

		for (int j : M) {
			if (userRatingTreeMap.getUserRatings().get(user).getMovieRatingAsInteger().get(j)!=null) {
				denominator += userRatingTreeMap.getUserRatings().get(user).getMovieRatingAsInteger().get(j); 
			}
		}

		return new double[]{numerator, denominator};
	}

	/**
	 * This method retrieves the top-rated movies for a given user.
	 * @param user The user for whom top-rated movies are to be retrieved
	 * @param userRatingTreeMap Map of user ratings
	 * @param numberOfTopRatings The number of top-rated movies to retrieve
	 * @return A set containing the top-rated movies for the user
	 */
	private static Set<Integer> getTopRatingsForUser(Integer user, UserRatingTreeMap userRatingTreeMap, int numberOfTopRatings) {
		Map<Integer, Double> userRatings = userRatingTreeMap.getUserRatings().get(user).getMovieRatingAsInteger();

		List<Map.Entry<Integer, Double>> sortedRatings = new ArrayList<>(userRatings.entrySet());
		sortedRatings.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

		Set<Integer> topRatedMovies = new HashSet<>();
		int count = 0;
		for (Map.Entry<Integer, Double> entry : sortedRatings) {
			topRatedMovies.add(entry.getKey());
			count++;
			if (count >= numberOfTopRatings) {
				break;
			}
		}
		return topRatedMovies;
	}

	/**
	 * This method finds the next item to add to the sequence based on user satisfaction.
	 * It iterates over all possible items not already in the sequence and calculates the satisfaction
	 * of each user in the group if the item were added. It selects the item that minimizes the difference
	 * in satisfaction among users in the group.
	 * @param group List of users
	 * @param sequence Current sequence of items
	 * @param userSatisfaction Map containing user satisfaction values
	 * @param userRatingTreeMap Map of user ratings
	 * @param itemRatingsMap Map of item ratings
	 * @return The next item to add to the sequence
	 */
	private static Integer findNextItemToAdd(List<Integer> group, Set<Integer> sequence, Map<Integer, Double> userSatisfaction, UserRatingTreeMap userRatingTreeMap, ItemRatingTreeMap itemRatingsMap) {
		double minDiff = Double.MAX_VALUE;
		Integer bestItem = null;

		Set<Integer> allItems = itemRatingsMap.getItemRatingsMap().keySet();
		allItems.removeAll(sequence);

		for (int i = 0; i < group.size(); i++) {
			for (int j = i + 1; j < group.size(); j++) {
				Integer user1 = group.get(i);
				Integer user2 = group.get(j);

				for (Integer item : allItems) {
					Set<Integer> newSequence = new HashSet<>(sequence);
					newSequence.add(item);

					double newSatisfaction1 = getSatisfaction(user1, newSequence, userRatingTreeMap, itemRatingsMap);
					double newSatisfaction2 = getSatisfaction(user2, newSequence, userRatingTreeMap, itemRatingsMap);

					double diff = Math.abs(newSatisfaction1 - newSatisfaction2);

					// Update the best item (if found it) 
					if (diff < minDiff) {
						minDiff = diff;
						bestItem = item;
					}
				}
			}
		}
		return bestItem;
	}

}

