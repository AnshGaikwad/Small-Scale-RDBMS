package com.ayg.rdbms.commands;

import com.ayg.rdbms.utils.OperatorUtil;
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

    public String deleteFromTable() {
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[2];
        String tableCSV = tableName + ".csv";

        String[] tableAttributes = checkIfTableSchemaExists(schemaCSV, tableName);
        if (tableAttributes == null) {
            return "[!!] Invalid Table Name";
        }

        boolean tableFileExists = checkIfTableFileExists(tableCSV);
        if (!tableFileExists) {
            return "[!!] Invalid Table Name";
        }

        String[] condition = getCondition(tableAttributes).split("-");
        ArrayList<Integer> rowsAffected = getRowsAffected(tableCSV, condition);

        if(rowsAffected == null){
            return "[!!] Invalid operator for type String";
        }

        return Integer.toString(deletedFromTable(tableCSV, rowsAffected));
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
                    try {
                        switch (operator[l]) {
                            case "<=" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.lessThanEqualTo()));
                            case ">=" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.moreThanEqualTo()));
                            case "!=" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.notEqualTo()));
                            case "=" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.equalTo()));
                            case "<" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.lessThan()));
                            case ">" -> rowsCanBeAffected.add(new ArrayList<>(operatorUtil.moreThan()));
                        }
                    }catch (NullPointerException e){
                        rowsCanBeAffected.add(null);
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            ArrayList<Integer> rowsAffected1, rowsAffected2;
            try {
                rowsAffected1 = new ArrayList<>(rowsCanBeAffected.get(0));
            }catch (NullPointerException e){
                rowsAffected1 = null;
            }

            try {
                rowsAffected2 = new ArrayList<>(rowsCanBeAffected.get(1));
            }catch (NullPointerException e){
                rowsAffected2 = null;
            }

            if(Objects.equals(logicalOperator, "AND")){
                if(rowsAffected1 == null) return null;
                else if(rowsAffected2 == null) return null;
                else{
                    rowsAffected1.retainAll(rowsAffected2);
                    return rowsAffected1;
                }
            }
            else if(Objects.equals(logicalOperator, "OR")){
                if(rowsAffected1 == null && rowsAffected2 != null) return rowsAffected2;
                else if(rowsAffected2 == null && rowsAffected1 != null) return rowsAffected1;
                else if(rowsAffected1 == null && rowsAffected2 == null) return null;
                else{
                    List<Integer> rowsAffected2Copy = new ArrayList<>(rowsAffected2);
                    rowsAffected2Copy.removeAll(rowsAffected1);
                    rowsAffected1.addAll(rowsAffected2Copy);
                    return rowsAffected1;
                }
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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return rowsAffected;
        }

    }

    private String getCondition(String[] tableAttributes) {

        String condition = command.substring(command.indexOf("WHERE")+6).replaceAll("\\s", "");;

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
                    // To do
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
