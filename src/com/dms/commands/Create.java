package com.dms.commands;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Create {

    String command;

    public Create(String c) {
        command = c;
    }

    private boolean checkIfTableExists(String schemaCSV, String tableName) {
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
                        return true;
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
                    System.out.println("[!!] Error creating new File");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private boolean appendAttributes(String schemaCSV, String tableName) {

        CSVWriter tableWriter, schemaWriter;

        String tableCSV = tableName + ".csv";
        String attribute = command.substring(command.indexOf("(") + 1);
        attribute = attribute.substring(0, attribute.indexOf(")"));
        String[] attributes = attribute.split(" ");

        try {
            tableWriter = new CSVWriter(new FileWriter(tableCSV));
            schemaWriter = new CSVWriter(new FileWriter(schemaCSV, true));
            StringBuilder schemaRecord = new StringBuilder((tableName + ","));
            StringBuilder tableRecord = new StringBuilder();
            for (int i = 0; i < attributes.length; i++) {
                schemaRecord.append(attributes[i]);
                if (i % 2 == 0) {
                    tableRecord.append(attributes[i]).append(",");
                    schemaRecord.append(",");
                }
            }

            String[] schemaRecords = schemaRecord.toString().split(",");
            String[] tableRecords = tableRecord.toString().split(",");
            schemaWriter.writeNext(schemaRecords);
            schemaWriter.close();
            tableWriter.writeNext(tableRecords);
            tableWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public String createTable() {
        String tableName = command.split(" ")[2];
        String schemaCSV = "schema.csv";

        boolean tableExists = checkIfTableExists(schemaCSV, tableName);
        if (tableExists) {
            System.out.println("[!!] Table exists already");
            return null;
        }

        boolean appendAttributesDone = appendAttributes(schemaCSV, tableName);
        if (!appendAttributesDone) {
            System.out.println("[!!] Attributes aren't updated");
            return null;
        }

        return tableName;
    }
}
