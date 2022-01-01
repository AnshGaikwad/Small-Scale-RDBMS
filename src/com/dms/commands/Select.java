package com.dms.commands;

import com.dms.utils.OperatorUtil;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Select {

    String command;

    public Select(String c) {
        command = c;
    }

    public void selectFromTable() {
        String schemaCSV = "schema.csv";
        String tableNames = command.substring(command.indexOf("FROM")+5);
        tableNames = tableNames.substring(0, tableNames.indexOf("WHERE")-1);
        String columnList = command.substring(command.indexOf("SELECT")+7);
        columnList = columnList.substring(0, columnList.indexOf("FROM")-1);
        String[] tables = tableNames.split(",");
        String[] columns = columnList.split(",");

        for(String table : tables){
            String tableCSV = table + ".csv";
            String[] tableAttributes = checkIfTableSchemaExists(schemaCSV, table);
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
            ArrayList<Integer> rowsAffected = getRowsAffected(tableCSV, condition);
            ArrayList<Integer> columnsAffected = getColumnsAffected(columns, tableAttributes);

            boolean valuesSelected = selectTable(tableCSV, rowsAffected, columnsAffected, columns);
            if(!valuesSelected){
                System.out.println("[!!] Error Selecting Values");
                return;
            }
        }
    }

    private boolean selectTable(String tableCSV, ArrayList<Integer> rowsAffected, ArrayList<Integer> columnsAffected, String[] columns)  {

        boolean success = false;

        File inputFile = new File(tableCSV);

        try {
            CSVReader reader = new CSVReader(new FileReader(inputFile), ',');
            List<String[]> csvBody = reader.readAll();

            // Printing column name
            for(int cA : columnsAffected){
                System.out.print(csvBody.get(0)[cA] + " ");
            }
            System.out.println();

            for (int rA : rowsAffected) {
                for (int cA : columnsAffected) {
                    System.out.print(csvBody.get(rA)[cA] + "  ");
                }
                System.out.println();
            }

            reader.close();

            success = true;
        }catch (IOException e){
            e.printStackTrace();
        }

        return success;
    }

    private ArrayList<Integer> getColumnsAffected(String[] columns, String[] tableAttributes) {

        ArrayList<Integer> columnsAffected = new ArrayList<>();
        for(int i = 1; i < tableAttributes.length; i+=2){
            for (String column : columns) {
                if(column.contains("*")){
                    columnsAffected.add(i/2);
                    break;
                }
                if (column.contains(tableAttributes[i])) {
                    columnsAffected.add(i/2);
                    break;
                }
            }
        }

        return columnsAffected;

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
}
