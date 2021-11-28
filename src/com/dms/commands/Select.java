package com.dms.commands;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Select {

    String command;

    public Select(String c) {
        command = c;
    }

    public void selectFromTable() {
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[3];
        String columnName = command.split(" ")[1];
        String tableCSV = tableName + ".csv";

        String[] tableAttributes = checkIfTableSchemaExists(schemaCSV, tableName);
        if (tableAttributes == null) {
            System.out.println("[!!] Invalid Table Name");
            return;
        }

        boolean tableFileExists = checkIfTableFileExists(tableCSV);
        if (!tableFileExists) {
            System.out.println("[!!] Invalid Table Name");
            return;
        }

        String[] condition = getCondition(tableAttributes);

        // remaining code comes here


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
                    System.out.println("[!!] Error creating new File");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String[] getCondition(String[] tableAttributes) {

        String condition = command.substring(command.indexOf("WHERE")+6);
        String column = null, operator = null, value = null;

        String[] operators = {"<=", ">=", "!=", "=", "<", ">"};

        for(int i = 1; i < tableAttributes.length; i++){
            if(command.contains(tableAttributes[i])){
                column = tableAttributes[i];
                break;
            }
        }

        for(String o : operators){
            if(command.contains(o)){
                operator = o;
                break;
            }
        }

//        System.out.println(column.length()+" "+ operator.length() +" " + condition.length() + " " +  command.indexOf(";"));

        if(column != null && operator != null)
            value = condition.substring(column.length()+operator.length(), condition.length()-1);

        return new String[]{column, operator, value};
    }
}
