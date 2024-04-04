package com.atcs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atcs.recommender.GroupRecommender;
import com.atcs.recommender.Recommender;
import com.atcs.recommender.SequentialGroupRecommender;
import com.atcs.utils.DataReader;
import com.atcs.utils.ItemRatingTreeMap;
import com.atcs.utils.UserRatingTreeMap;
import com.atcs.utils.UserRatingUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class Main {

	public static void main(String[] args) throws NumberFormatException, Exception {
		try (CSVReader reader = new CSVReader(DataReader.getRatingsBufferedReader())) {
			List<String[]> rows = reader.readAll();
			System.out.println("Total number of ratings expected: 100836 , actual number of ratings: " + (rows.size()-1) + "\n");
			UserRatingTreeMap userRatingTreeMap = new UserRatingTreeMap();

			//Use 300 to shortener waiting time 
			for (int i = 1; i <300; i++) {
				String[] row = rows.get(i);
				int userId = Integer.parseInt(row[0]);
				int movieId = Integer.parseInt(row[1]);
				Double rating = Double.parseDouble(row[2]);

				userRatingTreeMap.addUserRating(userId,movieId,rating,Long.parseLong(row[3]));
			}
			ItemRatingTreeMap itemRatingsMap = new ItemRatingTreeMap(userRatingTreeMap);
			Map<Integer, Double> userAverage = userRatingTreeMap.getUserAvgRatings();

			Map<Integer, Map<Integer, Double>> similarityMap = UserRatingUtil.calculateUserSimilarity(userRatingTreeMap.getUserRatingsMap(), itemRatingsMap.getItemRatingsMap(), userAverage, userRatingTreeMap, false);
			Map<Integer, Map<Integer, Double>> weightedSimilarityMap = UserRatingUtil.calculateUserSimilarity(userRatingTreeMap.getUserRatingsMap(), itemRatingsMap.getItemRatingsMap(), userAverage, userRatingTreeMap, true);

			int randomUser1 = userRatingTreeMap.getRandomUser();
			int randomUser2 = userRatingTreeMap.getRandomUser();
			int randomUser3 = userRatingTreeMap.getRandomUser();
			int randomItem = itemRatingsMap.getRandomItem();

			double predictionValue = Recommender.predictUserRatingOnItem(userRatingTreeMap,itemRatingsMap,userRatingTreeMap.getUserRatings().get(randomUser1), itemRatingsMap.getItemById(randomItem)); 

			System.out.println("Pearson correlation function for computing similarities between users: " + similarityMap + "\n");
			System.out.println("Prediction function for predicting movies scores: " + predictionValue + "\n");

			System.out.println("10 most similar users: " + UserRatingUtil.top10Users(similarityMap, randomUser1) + "\n");
			System.out.println("10 most relevant movies that the recommender suggests: " + Recommender.predictUserRatingOnItems(userRatingTreeMap, itemRatingsMap, userRatingTreeMap.getUserRatings().get(randomUser1)) + "\n");

			System.out.println("Weighted Pearson correlation function for computing similarities between users: " + weightedSimilarityMap + "\n");

			List<Integer> userGroup = new ArrayList<>(); 
			userGroup.add(randomUser1); 
			userGroup.add(randomUser2); 
			userGroup.add(randomUser3); 
			if (UserRatingUtil.areAllElementsEqual(userGroup)) {
	            userGroup.set(2, userRatingTreeMap.getRandomUser());
	        }
			System.out.println("Group: " +userGroup+ "\n"); 
			
			List<Integer> movieListWithAvgMethod = GroupRecommender.getGroupRecommendations(userGroup, userRatingTreeMap, itemRatingsMap, true, false, false); 
			List<Integer> movieListWithLeastMethod = GroupRecommender.getGroupRecommendations(userGroup, userRatingTreeMap, itemRatingsMap, false, true, false); 
			List<Integer> movieListWithDisagreementMethod = GroupRecommender.getGroupRecommendations(userGroup, userRatingTreeMap, itemRatingsMap, false, false, true); 
			
			System.out.println("Group recommendations with the first aggregation approach, average method: " + movieListWithAvgMethod + "\n"); 
			System.out.println("Group recommendations with the first aggregation approach, least misery method:" + movieListWithLeastMethod + "\n"); 
			System.out.println("Group recommendations with method that takes disagreements into account: " + movieListWithDisagreementMethod + "\n");
			
			Set<Integer> basicSequence = SequentialGroupRecommender.sequentialRecommendations(userGroup, userRatingTreeMap, itemRatingsMap);
			System.out.println("Sequence " + basicSequence);


		} catch (IOException | CsvException e) {
			e.printStackTrace();
		}
	}
}
