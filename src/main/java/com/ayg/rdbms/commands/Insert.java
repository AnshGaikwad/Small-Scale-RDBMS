package com.ayg.rdbms.commands;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Insert {
    String command;

    public Insert(String c) {
        command = c;
    }

    public String insertInsideTable() {
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[2];
        String tableCSV = tableName + ".csv";


        // checks schema
        String[] tableAttributes = checkIfTableSchemaExists(schemaCSV, tableName);
        if (tableAttributes == null) {
            return "[!!] Table doesn't exists";
        }

        boolean tableFileExists = checkIfTableFileExists(tableCSV);
        if (!tableFileExists) {
            return "[!!] Table doesn't exists";
        }

        String[] attributes = checkIfAttributesMissMatch(tableAttributes);
        if (attributes == null) {
            return "[!!] Invalid number of values";
        }


        try {
            boolean insertInTable = insertInTable(attributes, tableCSV);
            if(!insertInTable) {
                return "[!!] Failed to insert values in Table";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private boolean insertInTable(String[] attributes, String tableCSV) throws IOException {
        CSVWriter tableWriter;
        tableWriter = new CSVWriter(new FileWriter(tableCSV, true));
        StringBuilder tableRecord = new StringBuilder();
        for (String attribute : attributes) {
            tableRecord.append(attribute.trim()).append(",");
        }

        String[] tableRecords = tableRecord.toString().split(",");
        tableWriter.writeNext(tableRecords);
        tableWriter.close();

        return true;
    }

    private String[] checkIfAttributesMissMatch(String[] tableAttributes) {
        int numOfAttributes = (tableAttributes.length - 1) / 2;
        String attribute = command.substring(command.indexOf("(") + 1);
        attribute = attribute.substring(0, attribute.indexOf(")"));
        String[] attributes = attribute.split(",");

        if(numOfAttributes == attributes.length)
            return attributes;

        return null;
    }

    private boolean checkIfTableFileExists(String tableCSV) {
        File file = new File(tableCSV);
        return file.exists();
    }

    private String[] checkIfTableSchemaExists(String schemaCSV, String tableName) {
        File schemaFile = new File(schemaCSV);

        // Checking if the specified file exists or not
        if (schemaFile.exists()) {
            CSVReader reader;
            try {
                reader = new CSVReader(new FileReader(schemaCSV));

                //Read CSV line by line and use the string array as you want
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine[0].equals(tableName)) {
                        return nextLine;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File file = new File(schemaCSV);
                boolean result = file.createNewFile();
                if (!result) {
                    // To do
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
