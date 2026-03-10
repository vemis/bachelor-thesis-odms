package cz.cuni.mff.couchbase_java;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.File;
import java.io.FileReader;
import java.util.List;

public class TPCHDatasetLoader {

    // Java code to illustrate
// Reading CSV File with different separator
    public static List<String[]> readDataFromCustomSeparator(String filePath)
    {
        try {
            // Create an object of file reader class with CSV file as a parameter.
            FileReader filereader = new FileReader(filePath);

            // create csvParser object with
            // custom separator semi-colon
            CSVParser parser = new CSVParserBuilder().withSeparator('|').build();

            // create csvReader object with parameter
            // filereader and parser
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();

            // Read all data at once
            List<String[]> allData = csvReader.readAll();



            return allData;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void printCSV(List<String[]> allData)
    {
        // Print Data.
        for (String[] row : allData) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

    public static void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                System.out.println(fileEntry.getName());
            }
        }
    }
}
