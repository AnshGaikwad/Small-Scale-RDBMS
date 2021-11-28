package com.dms;

import com.dms.commands.Create;
import com.dms.commands.Describe;
import com.dms.commands.Drop;
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
            if(command.contains("HELP")){
                Help help = new Help(command);
                help.executeHelp();
            }else if(command.contains("CREATE TABLE")){
                Create create = new Create(command);
	            String createdTable = create.createTable();
                if(createdTable != null)
                    System.out.println(">> " + createdTable + " Table Created Successfully");
            }
	        else if(command.contains("DROP TABLE")){
                Drop drop = new Drop(command);
                String droppedTable = drop.dropTable();
                if(droppedTable != null)
                    System.out.println(">> " + droppedTable + " Table Dropped Successfully");
            }
	        else if(command.contains("DESCRIBE")){
                Describe describe = new Describe(command);
                describe.describeTable();
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
