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
	        String command = sc.nextLine();
	        System.out.println(command);
	        if(command.contains("CREATE TABLE")){
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
                File tableFile = new File(tableCSV);

                // Checking if the specified file exists or not
                if (schemaFile.exists()) {
                    CSVReader reader;
                    try {
                        reader = new CSVReader(new FileReader(schemaCSV));
                        System.out.println(Arrays.toString(reader.readNext()));
                        //Read CSV line by line and use the string array as you want
                        String[] nextLine;
                        while ((nextLine = reader.readNext()) != null) {
                            System.out.println(Arrays.toString(nextLine));
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
            }else if(command.contains("HELP")){
                if(command.contains("CREATE TABLE")){
                    System.out.println("Syntax: ");
                    System.out.println("CREATE TABLE table_name ( attribute_1 attribute1_type CHECK (constraint1),\n" +
                            "attribute_2 attribute2_type, …, PRIMARY KEY ( attribute_1, attribute_2 ),\n" +
                            "FOREIGN KEY ( attribute_y ) REFERENCES table_x ( attribute_t ), FOREIGN\n" +
                            "KEY ( attribute_w ) REFERENCES table_y ( attribute_z )… );");
                    System.out.println("Description:");
                    System.out.println("The “CREATE TABLE” token is followed by any number of attribute name – attribute\n" +
                            "type pairs separated by commas. Each attribute name – attribute type pair can\n" +
                            "optionally be followed by a constraint specified using the keyword “CHECK”\n" +
                            "followed by a domain constraint.(Note that optional means that the input by the user is\n" +
                            "optional, not the implementation). This is followed by the token “PRIMARY KEY”\n" +
                            "and a list of attribute names separated by commas, enclosed in parentheses. Note that\n" +
                            "the specification of the primary key constraint is mandatory in this project and will\n" +
                            "always follow the listing of attributes. After the primary key constraint, the command\n" +
                            "should accept an optional list of foreign key constraints specified with the token\n" +
                            "“FOREIGN KEY” followed by an attribute name enclosed in parentheses, followed\n" +
                            "by the keyword “REFERENCES”, a table name and an attribute name enclosed in\n" +
                            "parentheses. Multiple foreign key constraints are separated by commas.");
                    System.out.println("Output:");
                    System.out.println("The output is “Table created successfully” if table creation succeeds, and a\n" +
                            "descriptive error message if it fails");
                }else if(command.contains("DROP TABLE")){
                    System.out.println("Syntax:");
                    System.out.println("DROP TABLE table_name;");
                    System.out.println("Description:");
                    System.out.println("The “DROP TABLE” token is followed by a table name");
                    System.out.println("Output:");
                    System.out.println("The output is “Table dropped successfully” if table dropping succeeds, and a\n" +
                            "descriptive error message if it fails.\n");
                }else if(command.contains("DESCRIBE")){
                    System.out.println("Syntax:");
                    System.out.println("DESCRIBE table_name;");
                    System.out.println("Description:");
                    System.out.println("The token “DESCRIBE” is followed by a table name.\n" +
                            "The output should be the list of attribute names and types in the table and a list of any\n" +
                            "constraints (primary key, foreign key, domain), one row for each attribute. If there are\n" +
                            "any constraints for an attribute, you should print the primary key constraint first,\n" +
                            "foreign key constraint next and domain constraints last. If the table does not exist, you\n" +
                            "should print an error message.");
                    System.out.println("Output:");
                    System.out.println("An example output is as follows:\n" +
                            "snum -- int -- primary key -- snum>0\n" +
                            "sname -- char(30)\n" +
                            "age -- int -- age > 0 AND age < 100\n" +
                            "deptid -- int -- foreign key references Department(deptid)");
                }else if(command.contains("SELECT")){
                    System.out.println("Syntax:");
                    System.out.println("SELECT attribute_list FROM table_list WHERE condition_list;");
                    System.out.println("Description:");
                    System.out.println("The token “SELECT” is followed by an attribute list, followed by the token “FROM”\n" +
                            "and a table name list. This is followed by an optional “WHERE” keyword and\n" +
                            "condition list. For simplicity, you are only asked to implement an attribute list\n" +
                            "consisting of attribute names separated by commas and not using the dot notation, in\n" +
                            "addition to “*”, which stands for all attributes. You can also assume that no attributes\n" +
                            "of different tables will have the same name. The table list will also be a simple list of\n" +
                            "table names separated by commas. The condition list has the following format:\n" +
                            "attribute1 operator value1\n" +
                            "OR\n" +
                            "attribute1 operator value1 AND/OR attribute2 operator value2 AND/OR attribute3\n" +
                            "operator value3…\n" +
                            "The operator can be any of “=”, “!=”, “<”, “>”, “<=”, “>=”.\n" +
                            "For simplicity, you can assume that if there are multiple conjunction/disjunction\n" +
                            "operators in the predicate, they will all be the same operator (i.e. there will not be\n" +
                            "AND and OR operators mixed in the same condition). Hence, the conditions do not\n" +
                            "need to be enclosed in parentheses. The values in the conditions can either be a\n" +
                            "constant value or the name of another attribute" +
                            "An example command is as follows:\n" +
                            "SELECT num FROM Student, Enrolled WHERE num = snum AND age > 18;\n" +
                            "assuming num and age are attributes of the Student table and snum is an attribute of\n" +
                            "the Enrolled table");
                    System.out.println("Output:");
                    System.out.println("The output of this command should be the list of matching tuples if there is no error.\n" +
                            "Otherwise, a descriptive error message should be printed. The first line of the result\n" +
                            "should be the names of the attributes separated by tab characters (as you would get\n" +
                            "from Sqlplus). Then you should print the tuples, one line per record, with different\n" +
                            "attribute values separated by tab characters.");
                }else if(command.contains("INSERT")){
                    System.out.println("Syntax:");
                    System.out.println("INSERT INTO table_name VALUES ( val1, val2, … );");
                    System.out.println("Description:");
                    System.out.println("The “INSERT INTO” token is followed by a table name, followed by the token\n" +
                            "“VALUES” and a list of values separated by commas enclosed in parentheses. Each\n" +
                            "value should be either a number (integer or decimal) or a string enclosed in single\n" +
                            "quotes. Note that you are asked to implement only one form of this command, where\n" +
                            "the values listed are inserted into the table in the same order that they are specified,\n" +
                            "i.e. the first value corresponds to the value of the first attribute, the second value\n" +
                            "corresponds to the value of the second attribute etc. Note that to satisfy this\n" +
                            "requirement, you should store the ordering of attributes when a table is created.");
                    System.out.println("Output:");
                    System.out.println("The\n" +
                            "output should be the message “Tuple inserted successfully” if the insertion succeeds,\n" +
                            "and a descriptive error message if it fails. ");
                }else if(command.contains("DELETE")){
                    System.out.println("Syntax:");
                    System.out.println("DELETE FROM table_name WHERE condition_list;");
                    System.out.println("Description:");
                    System.out.println("The “DELETE FROM” token is followed by a table name, followed by the optional\n" +
                            "“WHERE” keyword and a condition list. The condition list has the following format:\n" +
                            "attribute1 operator value1\n" +
                            "OR\n" +
                            "attribute1 operator value1 AND/OR attribute2 operator value2 AND/OR attribute3\n" +
                            "operator value3…\n" +
                            "The operator can be any of “=”, “!=”, “<”, “>”, “<=”, “>=”.\n" +
                            "For simplicity, you can assume that if there are multiple conjunction/disjunction\n" +
                            "operators in the predicate, they will all be the same operator (i.e. there will not be\n" +
                            "AND and OR operators mixed in the same condition). Hence, the conditions do not\n" +
                            "need to be enclosed in parentheses.\n");
                    System.out.println("Output:");
                    System.out.println("The output should be the message “X rows affected”, where X is the number of tuples\n" +
                            "deleted if there are no errors. Otherwise a descriptive error message should be printed.");
                }else if(command.contains("UPDATE")){
                    System.out.println("Syntax:");
                    System.out.println("UPDATE table_name SET attr1 = val1, attr2 = val2… WHERE condition_list;");
                    System.out.println("Description:");
                    System.out.println("The “UPDATE” token is followed by a table name, which is followed by the token\n" +
                            "“SET” and a list of attribute name=attribute value pairs separated by commas. This is\n" +
                            "followed by an optional “WHERE” token and a condition list in the same form as the\n" +
                            "condition list in the DELETE command.");
                    System.out.println("Output:");
                    System.out.println("The output should be the message “X rows affected”, where X is the number of tuples\n" +
                            "updated if there are no errors. Otherwise a descriptive error message should be\n" +
                            "printed.\n");
                }else{
                    System.out.println("Command doesn't exists");
                }
            }else if(command.contains("Quit")){
	            break;
            }else{
	            System.out.println("[!!] Invalid Input");
            }
        }
    }
}
