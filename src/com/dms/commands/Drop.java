package com.dms.commands;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.Arrays;
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

        try {

            List<String[]> allElements = getTable(schemaCSV, tableName);
            if(allElements == null){
                System.out.println("[!!] Table Doesn't Exists");
                return null;
            }

            boolean deleteFileSuccess = deleteTableCSV(tableCSV);
            if(!deleteFileSuccess){
                System.out.println("[!!] Failed to delete the file");
                return null;
            }

            boolean schemeDataChange = deleteRowFromSchema(schemaCSV, allElements);
            if(!schemeDataChange){
                System.out.println("[!!] Failed to change schema file");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tableName;
    }

    private List<String[]> getTable(String schemaCSV, String tableName) throws IOException {
        CSVReader readerAll = new CSVReader(new FileReader(schemaCSV));
        List<String[]> allElements = readerAll.readAll();

        //Read CSV line by line
        String[] nextLine;
        int rowNumber = 0;
        CSVReader reader = new CSVReader(new FileReader(schemaCSV));
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine[0].equals(tableName)) {
                allElements.remove(rowNumber);

                return allElements;
            }
            rowNumber++;
        }
        return null;
    }

    private boolean deleteRowFromSchema(String schemaCSV, List<String[]> allElements) throws IOException {

        CSVWriter writer = new CSVWriter(new FileWriter(schemaCSV));
        writer.writeAll(allElements);
        writer.close();

        return true;
    }

    private boolean deleteTableCSV(String tableCSV) {
        File file = new File(tableCSV);
        return file.delete();
    }
}
