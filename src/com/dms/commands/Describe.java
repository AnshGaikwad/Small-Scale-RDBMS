package com.dms.commands;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;

public class Describe {

    String command;
    public Describe (String c){
        command = c;
    }

    public void describeTable(){
        String tableName = command.split(" ")[1];
        String schemaCSV = "schema.csv";
        readSchema(schemaCSV, tableName);
    }

    private void readSchema(String schemaCSV, String tableName) {
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(schemaCSV));

            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if(nextLine[0].equals(tableName)){
                    System.out.println(nextLine[0]);
                    for(int i = 1; i < nextLine.length - 1; i+=2){
                        System.out.println(nextLine[i] + " -- " + nextLine[i+1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
