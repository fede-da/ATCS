package com.atcs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.atcs.models.UserRating;
import com.atcs.utils.DataReader;
import com.atcs.utils.UserRatingSet;
import com.atcs.utils.UserRatingUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class Main {

	public static void main(String[] args) {
		try (CSVReader reader = new CSVReader(DataReader.getRatingsBufferedReader())) {
			List<String[]> rows = reader.readAll();
			System.out.println("Total number of ratings expected: 100836 , actual number of ratings: " + (rows.size()-1));
			//created new user rating set
			UserRatingSet userRatingSet = new UserRatingSet();

			//TODO: sostituire 300 con rows.size()
			for (int i = 1; i < 300; i++) {
				UserRating currentUserData = new UserRating(rows.get(i));
				//add user to custom set
				userRatingSet.addUserRating(currentUserData);
			}

			Map<Integer, Double> userAverage = userRatingSet.getUserAvgRatings();

//			System.out.println(UserRatingUtil.calculateUserSimilarity(userRatingsMap, itemRatingsMap, userAverage));
//			System.out.println(UserRatingUtil.calculateUserSimilarity(userRatingsMap, itemRatingsMap, userRatingSet.getUserAvgRatings()));

		} catch (IOException | CsvException e) {
			e.printStackTrace();
		}
	}
//	Total number of ratings expected: 100836 , actual number of ratings: 100836
//	{1={2=0.15045803482694003, 3=-0.8390106496954882}, 2={1=0.15045803482694003, 3=-0.24253562503633302}, 3={1=-0.8390106496954882, 2=-0.24253562503633302}}
}
