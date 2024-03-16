package com.atcs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.atcs.models.Item;
import com.atcs.models.UserRating;
import com.atcs.recommender.Recommender;
import com.atcs.utils.DataReader;
import com.atcs.utils.ItemRatingTreeMap;
import com.atcs.utils.UserRatingTreeMap;
import com.atcs.utils.UserRatingUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class Main {

	public static void main(String[] args) {
		try (CSVReader reader = new CSVReader(DataReader.getRatingsBufferedReader())) {
			List<String[]> rows = reader.readAll();
			System.out.println("Total number of ratings expected: 100836 , actual number of ratings: " + (rows.size()-1));
			//created new user rating set
			UserRatingTreeMap userRatingTreeMap = new UserRatingTreeMap();

			//TODO: sostituire 300 con rows.size()
			for (int i = 1; i <300; i++) {
				UserRating currentUserData = new UserRating(rows.get(i));
				String[] row = rows.get(i);
				int userId = Integer.parseInt(row[0]);
				int movieId = Integer.parseInt(row[1]);
				Double rating = Double.parseDouble(row[2]);

				//add user to custom set
				userRatingTreeMap.addUserRating(userId,movieId,rating,Long.parseLong(row[3]));
			}
			ItemRatingTreeMap itemRatingsMap = new ItemRatingTreeMap(userRatingTreeMap);
			Map<Integer, Double> userAverage = userRatingTreeMap.getUserAvgRatings();

			Map<Integer, Map<Integer, Double>> similarityMap = UserRatingUtil.calculateUserSimilarity(userRatingTreeMap.getUserRatingsMap(), itemRatingsMap.getItemRatingsMap(), userAverage);
			
			System.out.println(similarityMap);
			System.out.println(Recommender.predictUserRatingOnItem(userRatingTreeMap,itemRatingsMap,userRatingTreeMap.getUserRatings().get(1),
					itemRatingsMap.getItemById(527)));
			//			System.out.println(UserRatingUtil.calculateUserSimilarity(userRatingsMap, itemRatingsMap, userRatingSet.getUserAvgRatings()));

			System.out.println(UserRatingUtil.top10User(similarityMap, userRatingTreeMap.getRandomUser()));
		
		} catch (IOException | CsvException e) {
			e.printStackTrace();
		}
	}
	//	Total number of ratings expected: 100836 , actual number of ratings: 100836
	//	{1={2=0.15045803482694003, 3=-0.8390106496954882}, 2={1=0.15045803482694003, 3=-0.24253562503633302}, 3={1=-0.8390106496954882, 2=-0.24253562503633302}}
}
