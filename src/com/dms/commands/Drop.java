package com.dms.commands;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Drop {

    String command;
    public Drop (String c){
        command = c;
    }

    public String dropTable(){
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[2];
        String tableCSV = tableName + ".csv";

        CSVReader reader, reader2;
        try {
            reader = new CSVReader(new FileReader(schemaCSV));
            reader2 = new CSVReader(new FileReader(schemaCSV));
            List<String[]> allElements = reader2.readAll();

            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            int rowNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                System.out.println(tableName);
                System.out.println(nextLine[0]);
                System.out.println(rowNumber);
                if (nextLine[0].equals(tableName)) {
                    System.out.println(rowNumber);
                    allElements.remove(rowNumber);
                    System.out.println(">> Table exists!");
                    break;
                }
                rowNumber++;
            }

            File file = new File(tableCSV);
            if (file.delete()) {
                System.out.println(">> File deleted successfully");
            } else {
                System.out.println("[!!] Failed to delete the file");
            }

            CSVWriter writer = new CSVWriter(new FileWriter(schemaCSV));
            writer.writeAll(allElements);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
