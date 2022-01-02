package com.ayg.rdbms.commands;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.jar.Attributes;

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
                    // To Do
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    // CREATE TABLE Students (id INT CHECK (id>0), name STR, dept STR, PRIMARY KEY (id), FOREIGN KEY (dept) REFERENCES Depts (name))
    private boolean appendAttributes(String schemaCSV, String tableName) {

        CSVWriter tableWriter, schemaWriter;

        String tableCSV = tableName + ".csv";
        String attribute = command.substring(command.indexOf("(") + 1);
        String[] attributes = attribute.split(",");

        System.out.println(Arrays.toString(attributes));

        String columnPk = null, columnFk = null, tableFk = null, columnTableFk = null;

        for(String a : attributes){

            if(a.contains("PRIMARY KEY")){
                columnPk = a.substring(a.indexOf("(") + 1);
                columnPk = columnPk.substring(0, columnPk.indexOf(")"));
            }else if(a.contains("FOREIGN KEY")){
                columnFk = a.substring(a.indexOf("(") + 1);
                columnFk = columnFk.substring(0, columnFk.indexOf(")"));
                tableFk = a.substring(a.indexOf("REFERENCES") + 11);
                tableFk = tableFk.substring(0, tableFk.indexOf("(") - 1);
                columnTableFk = a.substring(a.indexOf("REFERENCES") + 11);
                columnTableFk = columnTableFk.substring(columnTableFk.indexOf("(") + 1);
                columnTableFk = columnTableFk.substring(0, columnTableFk.indexOf(")"));
            }
        }

        try {
            tableWriter = new CSVWriter(new FileWriter(tableCSV));
            schemaWriter = new CSVWriter(new FileWriter(schemaCSV, true));
            StringBuilder schemaRecord = new StringBuilder((tableName + ","));
            StringBuilder tableRecord = new StringBuilder();
            for (int i = 0; i < attributes.length; i++) {
                if(attributes[i].contains("PRIMARY KEY") || attributes[i].contains("FOREIGN KEY")) continue;
                tableRecord.append(attributes[i].trim().split(" ")[0]).append(",");
                String columnName = attributes[i].trim().split(" ")[0];
                for(String a : attributes[i].trim().split(" ")){
                    if(a.contains("(") && a.contains(")"))
                        a = a.substring(a.indexOf("(") + 1, a.indexOf(")"));
                    schemaRecord.append(a).append(",");
                }

                if(columnPk != null && Objects.equals(columnName, columnPk)){
                    schemaRecord.append("PRIMARY KEY").append(",");
                }
                if(columnFk != null && Objects.equals(columnName, columnFk)){
                    schemaRecord.append("FOREIGN KEY").append(",");
                    schemaRecord.append(tableFk).append(",");
                    schemaRecord.append(columnTableFk).append(",");
                }
                schemaRecord.append("-").append(",");
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
            return "[!!] Table exists already";
        }

        boolean appendAttributesDone = appendAttributes(schemaCSV, tableName);
        if (!appendAttributesDone) {
            return "[!!] Attributes aren't updated";
        }

        return "";
    }
}
