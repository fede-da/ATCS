package com.atcs.recommender;

import java.util.*;

import com.atcs.models.Item;
import com.atcs.models.UserRating;
import com.atcs.utils.ItemRatingTreeMap;
import com.atcs.utils.UserRatingTreeMap;

public class GroupRecommender {

	public static List<Integer> getGroupRecommendations(List<Integer> group, UserRatingTreeMap userRatingTreeMap, ItemRatingTreeMap itemRatingsMap, boolean avg, boolean least, boolean disagreement) {
		List<Integer> groupRecommendations = new ArrayList<>();
		Map<Integer, Integer> movieVotes = new HashMap<>();
		Map<Integer, Double> movieScores = new HashMap<>();
		Map<Integer, Double> userDisagreements = new HashMap<>();

		for (Integer user : group) {			
			UserRating userRating = userRatingTreeMap.getUserRatings().get(user); 
			List<Integer> userRecommendations = Recommender.predictUserRatingOnItems(userRatingTreeMap, itemRatingsMap, userRating); // Ottieni le raccomandazioni dell'utente

			for (Integer movie : userRecommendations) {
				Map<Integer, Double> movieR = userRating.getMovieRatingAsInteger(); 
				double rating = 0.0; 
				if(movieR != null) {
					if (movieR.get(movie)!=null) {
						rating = movieR.get(movie);
						if (rating != 0.0) { 
							movieVotes.put(movie, movieVotes.getOrDefault(movie, 0) + 1);
							Map<Integer, Double> movieRating = userRating.getMovieRatingAsInteger(); 
							movieScores.put(movie, movieScores.getOrDefault(movie, 0.0) + movieRating.get(movie));//user.getRating(movie)
						}
					}
				}
			}
			if (disagreement) {
				for (Integer movie : userRecommendations) {
					double userRatingValue = userRating.getMovieRatingAsInteger().getOrDefault(movie, 0.0);
					double avgRating = 0.0; 
					if (movieVotes.get(movie)!=null) {
						avgRating = movieScores.get(movie) / movieVotes.get(movie);
					}
					double disagreementScore = Math.abs(userRatingValue - avgRating);
					userDisagreements.put(movie, userDisagreements.getOrDefault(movie, 0.0) + disagreementScore);
				}
			}
		}

		if (avg) {
			movieScores = calculateAvg(movieVotes, movieScores); 
		}
		if (least) {
			movieScores = calculateLeast(movieVotes, movieScores); 
		}

		if (disagreement) {
			movieScores= calculateDisagreement(userDisagreements, movieScores); 
		}

		List<Map.Entry<Integer, Double>> sortedEntries = new ArrayList<>(movieScores.entrySet());
		sortedEntries.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

		for (Map.Entry<Integer, Double> entry : sortedEntries) {
			groupRecommendations.add(entry.getKey());
		}

		return groupRecommendations.subList(0, Math.min(10, groupRecommendations.size())); 
	}

	private static Map<Integer, Double> calculateAvg (Map<Integer, Integer> movieVotes, Map<Integer, Double> movieScores) {
		for (Map.Entry<Integer, Integer> entry : movieVotes.entrySet()) {
			Integer movie = entry.getKey();
			int votes = entry.getValue();
			double totalScore = movieScores.get(movie);
			double averageScore = totalScore / votes;
			//            groupRecommendations.add(movie);
			movieScores.put(movie, averageScore);
		}
		return movieScores; 
	}

	private static Map<Integer, Double> calculateLeast (Map<Integer, Integer> movieVotes, Map<Integer, Double> movieScores) {
		for (Map.Entry<Integer, Integer> entry : movieVotes.entrySet()) {
			Integer movie = entry.getKey();
			double minScore = Double.MAX_VALUE;

			for (Map.Entry<Integer, Double> scoreEntry : movieScores.entrySet()) {
				double userScore = scoreEntry.getValue();
				if (userScore < minScore) {
					minScore = userScore;
				}
			}
			movieScores.put(movie, minScore);
		}
		return movieScores;
	}

	private static Map<Integer, Double> calculateDisagreement (Map<Integer, Double> userDisagreements, Map<Integer, Double> movieScores) {
		for (Map.Entry<Integer, Double> entry : movieScores.entrySet()) {
			int movie = entry.getKey();
			double score = entry.getValue();
			double disagreementScore = userDisagreements.getOrDefault(movie, 0.0);
			movieScores.put(movie, score + disagreementScore);
		}
		return movieScores; 
	}

	// TODO: Complete recommend group functions
	public static Collection<Collection<Item>> recommendMovieForUsersGroup(Collection<UserRating> users) {
		List<Double> weights = new ArrayList<>(Collections.nCopies(users.size(), 1.0));
		Map<UserRating, Double> userSatisfactionScores = initializeUserSatisfactionScores(users);
		List<Collection<Item>> recommendationsForGroup = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			Collection<Item> recommendations = generateRecommendationForGroup(users, weights, userSatisfactionScores);
			recommendationsForGroup.add(recommendations);
			adjustWeightsBasedOnSatisfaction(weights, userSatisfactionScores);
		}

		return recommendationsForGroup;
	}

	private static Map<UserRating, Double> initializeUserSatisfactionScores(Collection<UserRating> users) {
		Map<UserRating, Double> satisfactionScores = new HashMap<>();
		for (UserRating user : users) {
			satisfactionScores.put(user, 0.0); // Start with a neutral satisfaction score
		}
		return satisfactionScores;
	}

	private static void adjustWeightsBasedOnSatisfaction(List<Double> weights, Map<UserRating, Double> userSatisfactionScores) {
		// Adjust the weights inversely proportional to the satisfaction scores
		double totalSatisfaction = userSatisfactionScores.values().stream().mapToDouble(Double::doubleValue).sum();
		int index = 0;
		for (Map.Entry<UserRating, Double> entry : userSatisfactionScores.entrySet()) {
			double satisfaction = entry.getValue();
			double dissatisfaction = totalSatisfaction - satisfaction;
			weights.set(index, dissatisfaction);
			index++;
		}
	}

	private static List<Item> generateRecommendationForGroup(Collection<UserRating> _users, Collection<Double> _weights, Map<UserRating, Double> userSatisfactionScores) {
		List<Item> recommendedItems = new ArrayList<>();

		// Generate the Item list based on the user ratings and weights
		// For example, you could use a weighted average of user ratings to score each Item

		// Update the satisfaction scores based on the recommended items
		// For example, if a user's top-rated item is included in the recommendation, increase their satisfaction score

		// Adjust weights as a side effect within this function or after calling it based on the updated satisfaction scores

		return recommendedItems;
	}

}
