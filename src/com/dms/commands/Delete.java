package com.dms.commands;

import com.dms.utils.OperatorUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Delete {
    String command;

    public Delete(String c) {
        command = c;
    }

    public String deleteFromTable() {
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[2];
        String tableCSV = tableName + ".csv";

        String[] tableAttributes = checkIfTableSchemaExists(schemaCSV, tableName);
        if (tableAttributes == null) {
            System.out.println("[!!] Invalid Table Name");
            return null;
        }

        boolean tableFileExists = checkIfTableFileExists(tableCSV);
        if (!tableFileExists) {
            System.out.println("[!!] Invalid Table Name");
            return null;
        }

        String[] condition = getCondition(tableAttributes);

        ArrayList<Integer> rowsAffected = getRowsAffected(tableCSV, condition);
        int numOfRowsAffected = deletedFromTable(tableCSV, rowsAffected);
        if(numOfRowsAffected == 0) {
            System.out.println("[!!] Failed to delete values from Table");
            return null;
        }


        return tableName;
    }

    private int deletedFromTable(String tableCSV, ArrayList<Integer> rowsAffected) {

        try {
            CSVReader readerAll = new CSVReader(new FileReader(tableCSV));
            List<String[]> allElements = readerAll.readAll();
            for(int ra : rowsAffected){
                allElements.remove(ra);
            }
            CSVWriter writer = new CSVWriter(new FileWriter(tableCSV));
            writer.writeAll(allElements);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rowsAffected.size();

    }

    private ArrayList<Integer> getRowsAffected(String tableCSV, String[] condition) {

        String column = condition[0], operator = condition[1], value = condition[2];
        int index = -1;
        ArrayList<Integer> rowsAffected = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(tableCSV));
            String[] attributes = reader.readNext();

            for(int i = 0; i < attributes.length; i++){
                if(attributes[i].equals(column)){
                    index = i;
                }
            }

            int conditionVal = Integer.parseInt(value);
            OperatorUtil operatorUtil = new OperatorUtil(reader, index, conditionVal);
            switch (operator) {
                case "<=" -> rowsAffected = operatorUtil.lessThanEqualTo();
                case ">=" -> rowsAffected = operatorUtil.moreThanEqualTo();
                case "!=" -> rowsAffected = operatorUtil.notEqualTo();
                case "=" -> rowsAffected = operatorUtil.equalTo();
                case "<" -> rowsAffected = operatorUtil.lessThan();
                case ">" -> rowsAffected = operatorUtil.moreThan();
                default -> System.out.println("Easter Egg here");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rowsAffected;
    }

    private String[] getCondition(String[] tableAttributes) {

        String condition = command.split(" ")[4];

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

        if(column != null && operator != null)
            value = condition.substring(column.length()+operator.length(), command.indexOf(";"));

        return new String[]{column, operator, value};
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
}
