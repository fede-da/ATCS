package com.atcs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atcs.utils.DataReader;
import com.atcs.utils.UserRatingUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class Main {

	public static void main(String[] args) {
		try (CSVReader reader = new CSVReader(DataReader.getRatingsBufferedReader())) {
			List<String[]> rows = reader.readAll();
			System.out.println("Total number of ratings expected: 100836 , actual number of ratings: " + (rows.size()-1)); 

			Map<Integer, List<Map<Integer,Double>>> userRatingsMap = new HashMap<>(); 
			Map <Integer, Map<Integer, Double>> itemRatingsMap = new HashMap<>(); 

			//TODO: sostituire 300 con rows.size()
			for (int i = 1; i < 300; i++) {
				UserRating currentUserData = new UserRating(rows.get(i));
				int userId = currentUserData.getUserId();

				int movieId = Integer.parseInt(rows.get(i)[1]);
				double rating = Double.parseDouble(rows.get(i)[2]);

				List<Map<Integer, Double>> userList = userRatingsMap.getOrDefault(userId, new ArrayList<>());
				if (!userRatingsMap.containsKey(userId)) {
					userRatingsMap.put(userId, userList);
				}
				userList.add(currentUserData.getMovieRatings());

				Map<Integer, Double> userItemRatings = itemRatingsMap.getOrDefault(movieId, new HashMap<>());
				if (!itemRatingsMap.containsKey(movieId)) {
					itemRatingsMap.put(movieId, userItemRatings);
				}
				userItemRatings.put(userId, rating);
			}

			Map<Integer, Double> userAverage = UserRatingUtil.calculateUsersAverage(userRatingsMap); 

			System.out.println(UserRatingUtil.calculateUserSimilarity(userRatingsMap, itemRatingsMap, userAverage));

		} catch (IOException | CsvException e) {
			e.printStackTrace();
		}
	}
//	Total number of ratings expected: 100836 , actual number of ratings: 100836
//	{1={2=0.15045803482694003, 3=-0.8390106496954882}, 2={1=0.15045803482694003, 3=-0.24253562503633302}, 3={1=-0.8390106496954882, 2=-0.24253562503633302}}
}
