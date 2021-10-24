package com.dms;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	    Scanner sc = new Scanner(System.in);

	    while(true){
	        System.out.print("> ");
	        String command = sc.next();
	        if(command.contains("CREATE TABLE")){
	            String tableName = command.split(" ")[2];
	            String schemaCSV = "schema.csv";
                String tableCSV = tableName + ".csv";
                String attribute = command.substring(command.indexOf("(") + 1);
                attribute = attribute.substring(0, attribute.indexOf(")"));
                String[] attributes = attribute.split(" ");
                Boolean tableExists = false;
                CSVWriter tableWriter, schemaWriter = null;

                CSVReader reader = null;
                try {
                    reader = new CSVReader(new FileReader(schemaCSV), ',' , '"' , 1);

                    //Read CSV line by line and use the string array as you want
                    String[] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        if(nextLine[0].equals(tableName)){
                            System.out.println("[!!] Table exists already");
                            tableExists = true;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(tableExists){
                    continue;
                }

                try {
                    tableWriter = new CSVWriter(new FileWriter(tableCSV));
                    schemaWriter = new CSVWriter(new FileWriter(schemaCSV, true));
                    String schemaRecord = (tableName+",");
                    String tableRecord = "";
                    for(int i = 0; i < attributes.length; i++){
                        schemaRecord += i;
                        if(i%2 == 0){
                            tableRecord += i;
                            schemaRecord += ",";
                        }
                    }
                    String[] schemaRecords = schemaRecord.split(",");
                    String[] tableRecords = tableRecord.split(",");
                    schemaWriter.writeNext(schemaRecords);
                    schemaWriter.close();
                    tableWriter.writeNext(tableRecords);
                    tableWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
	        else if(command.contains("DROP TABLE")){

            }
	        else if(command.contains("DESCRIBE")){

            }else if(command.contains("EXIT")){
	            break;
            }else{
	            System.out.println("[!!] Invalid Input");
            }
        }
    }
}
