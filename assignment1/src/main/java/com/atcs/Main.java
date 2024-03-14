package com.atcs;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/resources/data/ml-latest-small/ratings.csv"))) {
            List<String[]> rows = reader.readAll();
            System.out.println("Total number of ratings expected: 100836 , actual number of ratings: " + (rows.size()-1)); 
            
//            for (String[] row : rows) {
//                for (String cell : row) {
//                    System.out.print(cell + "\t");
//                }
//                System.out.println();
//            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}
