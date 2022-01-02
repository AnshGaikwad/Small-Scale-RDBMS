package com.ayg.rdbms.commands;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Drop {

    String command;

    public Drop(String c) {
        command = c;
    }

    public String dropTable() {
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[2];
        String tableCSV = tableName + ".csv";

        try {

            List<String[]> allElements = getTable(schemaCSV, tableName);
            if (allElements == null) {
                return "[!!] Table Doesn't Exists";
            }

            boolean deleteFileSuccess = deleteTableCSV(tableCSV);
            if (!deleteFileSuccess) {
                return "[!!] Failed to delete the file";
            }

            boolean schemeDataChange = deleteRowFromSchema(schemaCSV, allElements);
            if (!schemeDataChange) {
                return "[!!] Failed to change schema file";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
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
