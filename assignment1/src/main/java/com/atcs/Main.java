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
            System.out.println("Total number of ratings expected: 100836 , actual number of ratings: " + (rows.size()-1)); 
           
            //TODO: sostituire 500 con rows.size()
            for (int i=1; i<500; i++) {
            	 UserRating data = new UserRating();
            	 String[] row = rows.get(i);
            	 int userId = Integer.parseInt(row[0]); 
                 data.setUserId(userId);
                 data.addMovieWithRating(Integer.parseInt(row[1]),Double.parseDouble(row[2]));
                 data.setTimestamp(Long.parseLong(row[3]));
                 data.getRatingAvg();
             }
//            System.out.println(userRatingsMap);

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
    

}
 