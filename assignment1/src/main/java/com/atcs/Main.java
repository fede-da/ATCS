package com.atcs;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atcs.utils.DataReader;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class Main {
    public static void main(String[] args) {
        try (CSVReader reader = new CSVReader(DataReader.getRatingsBufferedReader())) {
            List<String[]> rows = reader.readAll(); 

            
            Map<Integer, List<UserRating>> userRatingsMap = new HashMap<>();
            	
            System.out.println("Total number of ratings expected: 100836 , actual number of ratings: " + (rows.size()-1)); 
           
            //TODO: sostituire 500 con rows.size()
            for (int i=1; i<500; i++) {
            	 List<UserRating> ratingsForEachUser;
            	 UserRating data = new UserRating(); 
            	 String[] row = rows.get(i);
            	 int userId = Integer.parseInt(row[0]); 
                 data.setUserId(userId);
                 data.setMovieId(Integer.parseInt(row[1]));
                 data.setRating(Double.parseDouble(row[2])); 
                 data.setTimestamp(Long.parseLong(row[3])); 
                
                 if (userRatingsMap.containsKey(userId)) {
                     ratingsForEachUser = userRatingsMap.get(userId);
                 } else {
                     ratingsForEachUser = new ArrayList<>();
                     userRatingsMap.put(userId, ratingsForEachUser);
                 }
                 ratingsForEachUser.add(data);
             }
//            System.out.println(userRatingsMap);

            
            for (Map.Entry<Integer, List<UserRating>> entry : userRatingsMap.entrySet()) { 
            	Map<Integer, Double> userAverages = new HashMap<>();    
            	
            	Integer user = entry.getKey();
            	List<UserRating> userData = entry.getValue();
            		
            	Double average = calculateAverage(userData); 
            	
            	userAverages.put(user, average);
            	
            	System.out.println("User-Averages: " + userAverages);
            }
            
            
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
    
    private static double calculateAverage(List<UserRating> data) {
        double sum = 0;
        for (UserRating d : data) {
            sum += d.getRating();
        }
        return sum / data.size();
    }
}
 