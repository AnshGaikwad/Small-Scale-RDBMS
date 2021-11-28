package com.dms.commands;

import com.dms.utils.OperatorUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Update {
    String command;

    public Update(String c) {
        command = c;
    }

    public int updateTable() {
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[1];
        String tableCSV = tableName + ".csv";

        String[] tableAttributes = checkIfTableSchemaExists(schemaCSV, tableName);
        if (tableAttributes == null) {
            System.out.println("[!!] Invalid Table Name");
            return -1;
        }

        boolean tableFileExists = checkIfTableFileExists(tableCSV);
        if (!tableFileExists) {
            System.out.println("[!!] Invalid Table Name");
            return -1;
        }

        String[] condition = getCondition(tableAttributes);
        ArrayList<Integer> rowsAffected = getRowsAffected(tableCSV, condition);

        String[] attributesAndValues = getAttributesAndValues();
        ArrayList<Integer> columnsAffected = getColumnsAffected(attributesAndValues, tableAttributes);

        boolean valuesUpdated = updateValuesInTable(tableCSV, rowsAffected, columnsAffected, attributesAndValues);
        if(!valuesUpdated){
            System.out.println("[!!] Error Updating Values");
            return -1;
        }

        return rowsAffected.size();
    }

    private ArrayList<Integer> getColumnsAffected(String[] attributesAndValues, String[] tableAttributes) {

        ArrayList<Integer> columnsAffected = new ArrayList<>();
        for(int i = 1; i < tableAttributes.length; i+=2){
            for (String aAV : attributesAndValues) {
                if (aAV.contains(tableAttributes[i])) {
                    columnsAffected.add(i/2);
                    break;
                }
            }
        }

        return columnsAffected;

    }

    private boolean updateValuesInTable(String tableCSV, ArrayList<Integer> rowsAffected, ArrayList<Integer> columnsAffected, String[] attributesAndValues) {

        boolean success = false;
        for(int rA : rowsAffected){
            for(int i = 0; i < attributesAndValues.length; i++){
                String value = attributesAndValues[i].split("=")[1].trim();
                try {
                    success = updateCSV(tableCSV, value, rA, columnsAffected.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return success;
    }

    public static boolean updateCSV(String tableCSV, String replace, int row, int col) throws IOException {

        File inputFile = new File(tableCSV);

        // Read existing file
        CSVReader reader = new CSVReader(new FileReader(inputFile), ',');
        List<String[]> csvBody = reader.readAll();
        // get CSV row column  and replace with by using row and column
        csvBody.get(row)[col] = replace;
        reader.close();

        // Write to CSV file which is open
        CSVWriter writer = new CSVWriter(new FileWriter(inputFile), ',');
        writer.writeAll(csvBody);
        writer.flush();
        writer.close();

        return true;
    }

    private String[] getAttributesAndValues() {
        String attributesAndValuesAll = command.substring(command.indexOf("SET")+4);
        attributesAndValuesAll = attributesAndValuesAll.substring(0, attributesAndValuesAll.indexOf("WHERE") -1);

        return attributesAndValuesAll.split(",");
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
