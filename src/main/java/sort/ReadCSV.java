package sort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadCSV {
    String fileName;

    public ReadCSV(String fileName) {
        this.fileName = fileName;
    }

    public static Double[] readCSV(String fileName) {
        ArrayList<Double> array = new ArrayList<>();

        try (FileReader reader = new FileReader(fileName);
             BufferedReader br = new BufferedReader(reader)) {

            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
                array.add(Double.parseDouble(line));
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return array.toArray(new Double[0]);
    }
}
