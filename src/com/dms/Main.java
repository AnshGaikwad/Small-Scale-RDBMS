package com.dms;

import com.dms.commands.Create;
import com.dms.commands.Help;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	    Scanner sc = new Scanner(System.in);

	    while(true){
	        System.out.print("> ");
	        String command = sc.nextLine();
	        System.out.println("Command: " + command);
            command = command.toUpperCase(Locale.ROOT);
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
                Create create = new Create(command);
	            String createdTable = create.createTable();
                if(createdTable != null)
                    System.out.println(">> " + createdTable + " Table Created Successfully");
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
