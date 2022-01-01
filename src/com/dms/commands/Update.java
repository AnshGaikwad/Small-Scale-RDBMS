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
import java.util.Objects;

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

        String[] condition = getCondition(tableAttributes).split("-");
        ArrayList<Integer> rowsAffected = getRowsAffected(tableCSV, condition);

        String[] attributesAndValues = getAttributesAndValues();
        ArrayList<Integer> columnsAffected = getColumnsAffected(attributesAndValues, tableAttributes);

        if(rowsAffected.size() == 0){
            return 0;
        }

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
        if(command.contains("WHERE"))
            attributesAndValuesAll = attributesAndValuesAll.substring(0, attributesAndValuesAll.indexOf("WHERE") -1);
        else
            attributesAndValuesAll = attributesAndValuesAll.substring(0, attributesAndValuesAll.length());

        return attributesAndValuesAll.split(",");
    }


    private ArrayList<Integer> getRowsAffected(String tableCSV, String[] condition) {

        ArrayList<Integer> rowsAffected = new ArrayList<>();

        if(condition.length > 3){
            String logicalOperator = condition[0], column1 = condition[1], operator1 = condition[2], value1 = condition[3],
                    column2 = condition[4], operator2 = condition[5], value2 = condition[6];

            String[] column = {column1, column2};
            String[] operator = {operator1, operator2};
            String[] value = {value1, value2};

            ArrayList<ArrayList<Integer>> rowsCanBeAffected =  new ArrayList<>();
            for(int l = 0; l < 2; l++){

                int index = -1;

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
                        if(attributes[i].equals(column[l])){
                            index = i;
                        }
                    }

                    int conditionVal = -1;
                    try{
                        conditionVal = Integer.parseInt(value[l]);
                    }catch (NumberFormatException ignored){

                    }

                    OperatorUtil operatorUtil = new OperatorUtil(reader, index, conditionVal, value[l]);
                    switch (operator[l]) {
                        case "<=" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.lessThanEqualTo()));
                        case ">=" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.moreThanEqualTo()));
                        case "!=" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.notEqualTo()));
                        case "=" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.equalTo()));
                        case "<" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.lessThan()));
                        case ">" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.moreThan()));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            ArrayList<Integer> rowsAffected1 = new ArrayList<>(rowsCanBeAffected.get(0));
            ArrayList<Integer> rowsAffected2 = new ArrayList<>(rowsCanBeAffected.get(1));
            if(Objects.equals(logicalOperator, "AND")){
                rowsAffected1.retainAll(rowsAffected2);
                return rowsAffected1;
            }
            else if(Objects.equals(logicalOperator, "OR")){
                List<Integer> rowsAffected2Copy = new ArrayList<>(rowsAffected2);
                rowsAffected2Copy.removeAll(rowsAffected1);
                rowsAffected1.addAll(rowsAffected2Copy);
                return rowsAffected1;
            }else{
                return null;
            }


        }else{
            String column = condition[0], operator = condition[1], value = condition[2];



            int index = -1;

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

    }

    private String getCondition(String[] tableAttributes) {

        String condition = command.substring(command.indexOf("WHERE")+6);

        if(condition.contains("AND") || condition.contains("OR")){

            StringBuilder sb = new StringBuilder();
            String conditionsBuilder;

            if(condition.contains("AND")){
                sb.append("AND").append("-");
                conditionsBuilder = command.substring(command.indexOf("WHERE") + 6, command.indexOf("AND") - 1) + "-" +
                        command.substring(command.indexOf("AND") + 4);
            }else if(condition.contains("OR")){
                sb.append("OR").append("-");
                conditionsBuilder = command.substring(command.indexOf("WHERE") + 6, command.indexOf("OR") - 1) + "-" +
                        command.substring(command.indexOf("OR") + 3);
            }else {
                conditionsBuilder = "";
            }


            String[] conditions = conditionsBuilder.split("-");

            for(int i = 0; i < 2; i++){
                String column = null, operator = null, value = null;

                String[] operators = {"<=", ">=", "!=", "=", "<", ">"};

                for(int t = 1; t < tableAttributes.length; t++){
                    if(conditions[i].contains(tableAttributes[t])){
                        column = tableAttributes[t];
                        break;
                    }
                }

                for(String o : operators){
                    if(conditions[i].contains(o)){
                        operator = o;
                        break;
                    }
                }

                if(column != null && operator != null)
                    value = conditions[i].substring(column.length()+operator.length());

                sb.append(column).append("-");
                sb.append(operator).append("-");
                sb.append(value).append("-");
            }

            return sb.toString();

        }else{

            StringBuilder sb = new StringBuilder();

            String column = null, operator = null, value = null;

            String[] operators = {"<=", ">=", "!=", "=", "<", ">"};

            for(int i = 1; i < tableAttributes.length; i++){
                if(condition.contains(tableAttributes[i])){
                    column = tableAttributes[i];
                    break;
                }
            }

            for(String o : operators){
                if(condition.contains(o)){
                    operator = o;
                    break;
                }
            }

            if(column != null && operator != null)
                value = condition.substring(column.length()+operator.length());

            sb.append(column).append("-");
            sb.append(operator).append("-");
            sb.append(value).append("-");

            return sb.toString();
        }
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
