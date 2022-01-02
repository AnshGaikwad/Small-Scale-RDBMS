package com.ayg.rdbms.commands;

import com.ayg.rdbms.utils.CheckConstraint;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.Objects;

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

        String attribute = checkIfAttributesMissMatch(tableAttributes);
        if (attribute.charAt(0) == '[') {
            return attribute;
        }

        String[] attributes = attribute.split(",");
        String checkConstraints = checkConstraints(attributes, tableAttributes, tableCSV);
        if(!checkConstraints.equals("")){
            return checkConstraints;
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

    private String checkConstraints(String[] attributes, String[] tableAttributes, String tableCSV) {

        StringBuilder sb = new StringBuilder();
        for(String tA : tableAttributes){
            sb.append(tA);
        }

        String[] columns = sb.toString().split("-");

        for(int i = 0; i < columns.length; i++){
            if(columns[i].contains("PRIMARY KEY")){
                if(Objects.equals(attributes[i], "")){
                    return "[!!] Not Null Constraint Violated";
                }
                try {
                    CSVReader reader = new CSVReader(new FileReader(tableCSV));
                    String[] nextLine = reader.readNext();
                    while((nextLine = reader.readNext()) != null){
                        if(Objects.equals(attributes[i], nextLine[i])){
                            return "[!!] Unique Constraint Violated";
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(columns[i].contains("FOREIGN KEY")){
                String tableFk = "", column = "";
                for(int t = 0; t < tableAttributes.length; t++){
                    if(tableAttributes[t].contains("FOREIGN KEY")){
                        tableFk = tableAttributes[t+1] + ".csv";
                        column = tableAttributes[t+2];
                        break;
                    }
                }

                try {
                    CSVReader reader = new CSVReader(new FileReader(tableFk));
                    String[] nextLine = reader.readNext();
                    int columnNumber = -1;
                    for(int c = 0; c < nextLine.length; c++){
                        if(column.equals(nextLine[c])){
                            columnNumber = c;
                        }
                    }
                    boolean violated = true;

                    while((nextLine = reader.readNext()) != null){
                        if(Objects.equals(nextLine[columnNumber].trim(), attributes[i].trim())){
                            violated = false;
                        }
                    }

                    if(violated){
                        return "[!!] Referential Integrity Constraint Violated";
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if(columns[i].contains("NOT NULL")){
                if(Objects.equals(attributes[i], "")){
                    return "[!!] Not Null Constraint Violated";
                }
            }
            if(columns[i].contains("UNIQUE")){
                try {
                    CSVReader reader = new CSVReader(new FileReader(tableCSV));
                    String[] nextLine = reader.readNext();
                    while((nextLine = reader.readNext()) != null){
                        if(Objects.equals(attributes[i], nextLine[i])){
                            return "[!!] Unique Constraint Violated";
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(columns[i].contains("CHECK")){
                String condition = "";
                for(int c = 0; c < tableAttributes.length; c++){
                    if(tableAttributes[c].contains("CHECK")){
                        condition = tableAttributes[c+1];
                        break;
                    }
                }
                String[] operators = {"<=", ">=", "!=", "=", "<", ">"};
                String operator = "";
                for(String o : operators){
                    if(condition.contains(o)){
                        operator = o;
                    }
                }

                String value = condition.substring(condition.indexOf(operator) + operator.length());
                boolean check = false;
                CheckConstraint checkConstraint = new CheckConstraint(attributes[i], operator, value);
                switch (operator) {
                    case "<=" -> check = checkConstraint.lessThanEqualTo();
                    case ">=" -> check = checkConstraint.moreThanEqualTo();
                    case "!=" -> check = checkConstraint.notEqualTo();
                    case "=" -> check = checkConstraint.equalTo();
                    case "<" -> check = checkConstraint.lessThan();
                    case ">" -> check = checkConstraint.moreThan();
                }

                if(!check){
                    return "[!!] Check Constraint Violated";
                }
            }
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

    private String checkIfAttributesMissMatch(String[] tableAttributes) {
        int numOfAttributes = 0;

        for(String tA : tableAttributes){
            if(Objects.equals(tA, "-")) numOfAttributes++;
        }

        String attribute = command.substring(command.indexOf("(") + 1);
        attribute = attribute.substring(0, attribute.indexOf(")"));
        String[] attributes = attribute.split(",");

        if(numOfAttributes == attributes.length){

            String columnOneType = tableAttributes[2];
            if(Objects.equals(columnOneType, "INT")){
                try {
                    int intValue = Integer.parseInt(attributes[0]);
                } catch (NumberFormatException e) {
                    if(!Objects.equals(attributes[0], ""))
                        return "[!!] Required INT type";
                }
            }

            int count = 0;

            for(int i = 3; i < tableAttributes.length; i++){
                if(i+2 <= tableAttributes.length && Objects.equals(tableAttributes[i], "-")){
                    count++;
                    if(Objects.equals(tableAttributes[i + 2], "INT")){
                        try {
                            int intValue = Integer.parseInt(attributes[count]);
                        } catch (NumberFormatException e) {
                            if(!Objects.equals(attributes[count], ""))
                                return "[!!] Required INT type";
                        }
                    }
                }
            }

            return attribute;
        }

        return "[!!] Invalid number of Attributes";
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
