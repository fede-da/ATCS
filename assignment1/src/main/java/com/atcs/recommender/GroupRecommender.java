package com.atcs.recommender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
