package cz.cuni.mff.mongodb_java;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.util.List;

import java.io.File;

public class TPCHDatasetLoader {

    //not used currently
    private String[] FILES = {
            "..\\..\\..\\dataset\\TPC-H\\tpch-data\\customer.tbl",
            "..\\..\\..\\dataset\\TPC-H\\tpch-data\\customer.tbl",
            "..\\..\\..\\dataset\\TPC-H\\tpch-data\\lineitem.tbl",
            "..\\..\\..\\dataset\\TPC-H\\tpch-data\\nation.tbl",
            "..\\..\\..\\dataset\\TPC-H\\tpch-data\\orders.tbl",
            "..\\..\\..\\dataset\\TPC-H\\tpch-data\\part.tbl",
            "..\\..\\..\\dataset\\TPC-H\\tpch-data\\partsupp.tbl",
            "..\\..\\..\\dataset\\TPC-H\\tpch-data\\region.tbl",
            "..\\..\\..\\dataset\\TPC-H\\tpch-data\\supplier.tbl"
    };
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

    /*
    public static void main(String[] args){
        //final String dir = System.getProperty("user.dir");
        //System.out.println("current dir = " + dir);
        //final File folder = new File("..\\..\\..\\dataset\\TPC-H\\tpch-data");
        //listFilesForFolder(folder);

        List<String[]> customers = readDataFromCustomSeparator("..\\..\\..\\dataset\\TPC-H\\tpch-data\\customer.tbl");
        List<String[]> lineitems = readDataFromCustomSeparator("..\\..\\..\\dataset\\TPC-H\\tpch-data\\lineitem.tbl");
        List<String[]> nations = readDataFromCustomSeparator("..\\..\\..\\dataset\\TPC-H\\tpch-data\\nation.tbl");
        List<String[]> orderss = readDataFromCustomSeparator("..\\..\\..\\dataset\\TPC-H\\tpch-data\\orders.tbl");
        List<String[]> parts = readDataFromCustomSeparator("..\\..\\..\\dataset\\TPC-H\\tpch-data\\part.tbl");
        List<String[]> partsupps = readDataFromCustomSeparator("..\\..\\..\\dataset\\TPC-H\\tpch-data\\partsupp.tbl");
        List<String[]> regions = readDataFromCustomSeparator("..\\..\\..\\dataset\\TPC-H\\tpch-data\\region.tbl");
        List<String[]> suppliers = readDataFromCustomSeparator("..\\..\\..\\dataset\\TPC-H\\tpch-data\\supplier.tbl");


    }
    */
}
