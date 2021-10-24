package com.dms;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.Arrays;
import java.util.List;
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
                String schemaCSV = "schema.csv";
                String tableName = command.split(" ")[2];
                String tableCSV = tableName + ".csv";

                CSVReader reader = null;
                try {
                    reader = new CSVReader(new FileReader(schemaCSV), ',' , '"' , 1);
                    List<String[]> allElements = reader.readAll();

                    //Read CSV line by line and use the string array as you want
                    String[] nextLine;
                    int rowNumber = 0;
                    while ((nextLine = reader.readNext()) != null) {
                        rowNumber++;
                        if (nextLine[0].equals(tableName)) {
                            allElements.remove(rowNumber);
                            System.out.println(">> Table exists!");
                            File file = new File(tableCSV);
                            if (file.delete()) {
                                System.out.println(">> File deleted successfully");
                            } else {
                                System.out.println("[!!] Failed to delete the file");
                            }
                            break;
                        }
                    }

                    CSVWriter writer = new CSVWriter(new FileWriter(schemaCSV));
                    writer.writeAll(allElements);
                    writer.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
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
