package com.ayg.rdbms.commands;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Describe {

    String command;
    boolean read = false;

    public Describe(String c) {
        command = c;
    }

    public String describeTable() {
        String tableName = command.split(" ")[1];
        String schemaCSV = "schema.csv";
        String output = readSchema(schemaCSV, tableName);

        if(!read)
            return "[!!] Table doesn't exists";

        return output;
    }

    private String readSchema(String schemaCSV, String tableName) {
        CSVReader reader;


        StringBuilder sb = new StringBuilder();

        try {
            reader = new CSVReader(new FileReader(schemaCSV));

            //Read CSV line by line and use the string array as you want
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(tableName)) {
                    sb.append(nextLine[0]).append("\n");
                    for (int i = 1; i < nextLine.length - 1; i++) {
                        if(!Objects.equals(nextLine[i], "-"))
                            sb.append(nextLine[i]);
                        if(Objects.equals(nextLine[i+1], "-")){
                            sb.append("\n");
                        }else if(!Objects.equals(nextLine[i], "-")){
                            sb.append(" -- ");
                        }
                    }
                    read = true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
