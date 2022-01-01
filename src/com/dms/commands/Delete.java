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
import java.util.Objects;

public class Delete {
    String command;

    public Delete(String c) {
        command = c;
    }

    public int deleteFromTable() {
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[2];
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

        return deletedFromTable(tableCSV, rowsAffected);
    }

    private int deletedFromTable(String tableCSV, ArrayList<Integer> rowsAffected) {

        try {
            CSVReader readerAll = new CSVReader(new FileReader(tableCSV));
            List<String[]> allElements = readerAll.readAll();

            for(int i = 0; i < rowsAffected.size(); i++){
                allElements.remove(rowsAffected.get(i)-i);
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


            if(!command.contains("WHERE")){
                String[] nextLine = reader.readNext();
                for (int i = 1; (nextLine = reader.readNext()) != null; i++){
                    rowsAffected.add(i);
                }
                return rowsAffected;
            }

            String[] attributes = reader.readNext();

            for(int i = 0; i < attributes.length; i++){
                if(attributes[i].equals(column)){
                    index = i;
                }
            }

            int conditionVal = -1;
            try{
                conditionVal = Integer.parseInt(value);
            }catch (NumberFormatException ignored){

            }

            OperatorUtil operatorUtil = new OperatorUtil(reader, index, conditionVal, value);
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

        if(column != null && operator != null)
            value = condition.substring(column.length()+operator.length());


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
