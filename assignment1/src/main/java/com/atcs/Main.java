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
			for (int i=1; i<300; i++) {
				List<Map<Integer,Double>> userList = new ArrayList<>(); 
				Map<Integer,Double> userItemRatings = new HashMap<>(); 
				UserRating data = new UserRating();
				String[] row = rows.get(i);
				int userId = Integer.parseInt(row[0]); 
				int movieId = Integer.parseInt(row[1]);
				Double rating = Double.parseDouble(row[2]); 
				data.setUserId(userId);
				data.addMovieWithRating(movieId , rating);
				data.setTimestamp(Long.parseLong(row[3]));

				if (userRatingsMap.containsKey(userId)) {
					userList = userRatingsMap.get(userId); 
				}else {
					userList = new ArrayList<>(); 
					userRatingsMap.put(userId, userList); 
				}
				userList.add(data.getMovieRatings()); 
				//				System.out.println(userList);  
				//				System.out.println(userRatingsMap.toString()); 

				if (itemRatingsMap.containsKey(movieId)) {
					userItemRatings = itemRatingsMap.get(movieId); 
				}else {
					userItemRatings = new HashMap<>(); 
					itemRatingsMap.put(movieId, userItemRatings); 
				}
				userItemRatings.put(userId, rating); 
			}

			Map<Integer, Double> userAverage = UserRatingUtil.calculateUsersAverage(userRatingsMap); 

			//			Scanner scanner = new Scanner(System.in);
			//
			//			System.out.print("Enter the first data: ");
			//			int dato1 = scanner.nextInt();
			//
			//			System.out.print("Enter the second data: ");
			//			int dato2 = scanner.nextInt();

			System.out.println(UserRatingUtil.calculateUserSimilarity(userRatingsMap, itemRatingsMap, userAverage));

		} catch (IOException | CsvException e) {
			e.printStackTrace();
		}
	}
}
