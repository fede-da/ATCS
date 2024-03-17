package com.atcs.utils;

import com.atcs.Main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataReader {
    public static BufferedReader getRatingsBufferedReader(){
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("data/ml-latest-small/ratings.csv");
        if (inputStream == null) {
            try {
                throw new FileNotFoundException("Resource file not found");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
