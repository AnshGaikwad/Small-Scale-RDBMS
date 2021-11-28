package com.dms;

import com.dms.commands.Help;
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
	        String command = sc.nextLine();
	        System.out.println(command);
            if(command.contains("HELP")){
                Help help = new Help();
                if(command.contains("CREATE TABLE")){
                    help.createCommand();
                }else if(command.contains("DROP TABLE")){
                    help.dropCommand();
                }else if(command.contains("DESCRIBE")){
                    help.describeCommand();
                }else if(command.contains("SELECT")){
                    help.selectCommand();
                }else if(command.contains("INSERT")){
                    help.insertCommand();
                }else if(command.contains("DELETE")){
                    help.deleteCommand();
                }else if(command.contains("UPDATE")){
                    help.updateCommand();
                }else{
                    System.out.println("Command doesn't exists");
                }
            }else if(command.contains("CREATE TABLE")){
	            String tableName = command.split(" ")[2];
	            String schemaCSV = "schema.csv";
                String tableCSV = tableName + ".csv";
                String attribute = command.substring(command.indexOf("(") + 1);
                attribute = attribute.substring(0, attribute.indexOf(")"));
                String[] attributes = attribute.split(" ");

                System.out.println(tableName + Arrays.toString(attributes));

                boolean tableExists = false;
                CSVWriter tableWriter, schemaWriter;

                File schemaFile = new File(schemaCSV);

                // Checking if the specified file exists or not
                if (schemaFile.exists()) {
                    CSVReader reader;
                    try {
                        reader = new CSVReader(new FileReader(schemaCSV));
//                        System.out.println("Reader : "+Arrays.toString(reader.readNext()));
                        //Read CSV line by line and use the string array as you want
                        String[] nextLine;
                        while ((nextLine = reader.readNext()) != null) {
                            System.out.println("Next Line : "+Arrays.toString(nextLine));
                            if(nextLine[0].equals(tableName)){
                                System.out.println("[!!] Table exists already");
                                tableExists = true;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        File file = new File(schemaCSV);
                        boolean result = file.createNewFile();
                        System.out.println("File: " + file);
                        if(result){
                            System.out.println("File Created");
                        }else{
                            System.out.println("Error");
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }

                if(tableExists){
                    continue;
                }

                try {
                    tableWriter = new CSVWriter(new FileWriter(tableCSV));
                    schemaWriter = new CSVWriter(new FileWriter(schemaCSV, true));
                    StringBuilder schemaRecord = new StringBuilder((tableName + ","));
                    StringBuilder tableRecord = new StringBuilder();
                    for(int i = 0; i < attributes.length; i++){
                        schemaRecord.append(attributes[i]);
                        if(i%2 == 0){
                            tableRecord.append(attributes[i]).append(",");
                            schemaRecord.append(",");
                        }
                    }
                    String[] schemaRecords = schemaRecord.toString().split(",");
                    System.out.println(Arrays.toString(schemaRecords));
                    String[] tableRecords = tableRecord.toString().split(",");
                    System.out.println(Arrays.toString(tableRecords));
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

                CSVReader reader, reader2;
                try {
                    reader = new CSVReader(new FileReader(schemaCSV));
                    reader2 = new CSVReader(new FileReader(schemaCSV));
                    List<String[]> allElements = reader2.readAll();

                    //Read CSV line by line and use the string array as you want
                    String[] nextLine;
                    int rowNumber = 0;
                    while ((nextLine = reader.readNext()) != null) {
                        System.out.println(tableName);
                        System.out.println(nextLine[0]);
                        System.out.println(rowNumber);
                        if (nextLine[0].equals(tableName)) {
                            System.out.println(rowNumber);
                            allElements.remove(rowNumber);
                            System.out.println(">> Table exists!");
                            break;
                        }
                        rowNumber++;
                    }

                    File file = new File(tableCSV);
                    if (file.delete()) {
                        System.out.println(">> File deleted successfully");
                    } else {
                        System.out.println("[!!] Failed to delete the file");
                    }

                    CSVWriter writer = new CSVWriter(new FileWriter(schemaCSV));
                    writer.writeAll(allElements);
                    writer.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
	        else if(command.contains("DESCRIBE")){
                String tableName = command.split(" ")[1];
                String schemaCSV = "schema.csv";
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
            }else if(command.contains("HELP TABLES")){
                // Get all table names
            }else if(command.contains("Quit")){
	            break;
            }else{
	            System.out.println("[!!] Invalid Input");
            }
        }
    }
}
