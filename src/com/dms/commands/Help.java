package com.dms.commands;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Help {

    String command;
    public Help (String c){
        command = c;
    }

    public void executeHelp(){
        if(command.contains("TABLES")) {
            executeHelpTables();
        }else if(command.contains("CREATE TABLE")){
            createCommand();
        }else if(command.contains("DROP TABLE")){
            dropCommand();
        }else if(command.contains("DESCRIBE")){
            describeCommand();
        }else if(command.contains("SELECT")){
            selectCommand();
        }else if(command.contains("INSERT")){
            insertCommand();
        }else if(command.contains("DELETE")){
            deleteCommand();
        }else if(command.contains("UPDATE")){
            updateCommand();
        }else{
            System.out.println("Command doesn't exists");
        }
    }

    private void command (String syntax, String description, String output) {
        System.out.println("Syntax:");
        System.out.println(syntax);
        System.out.println("Description:");
        System.out.println(description);
        System.out.println("Output:");
        System.out.println(output);
    }

    private void createCommand () {
        String syntax = "CREATE TABLE table_name ( attribute_1 attribute1_type CHECK (constraint1),\\n\" +\n" +
                "                            \"attribute_2 attribute2_type, …, PRIMARY KEY ( attribute_1, attribute_2 ),\\n\" +\n" +
                "                            \"FOREIGN KEY ( attribute_y ) REFERENCES table_x ( attribute_t ), FOREIGN\\n\" +\n" +
                "                            \"KEY ( attribute_w ) REFERENCES table_y ( attribute_z )… );";
        String description = "The “CREATE TABLE” token is followed by any number of attribute name – attribute\\n\" +\n" +
                "                            \"type pairs separated by commas. Each attribute name – attribute type pair can\\n\" +\n" +
                "                            \"optionally be followed by a constraint specified using the keyword “CHECK”\\n\" +\n" +
                "                            \"followed by a domain constraint.(Note that optional means that the input by the user is\\n\" +\n" +
                "                            \"optional, not the implementation). This is followed by the token “PRIMARY KEY”\\n\" +\n" +
                "                            \"and a list of attribute names separated by commas, enclosed in parentheses. Note that\\n\" +\n" +
                "                            \"the specification of the primary key constraint is mandatory in this project and will\\n\" +\n" +
                "                            \"always follow the listing of attributes. After the primary key constraint, the command\\n\" +\n" +
                "                            \"should accept an optional list of foreign key constraints specified with the token\\n\" +\n" +
                "                            \"“FOREIGN KEY” followed by an attribute name enclosed in parentheses, followed\\n\" +\n" +
                "                            \"by the keyword “REFERENCES”, a table name and an attribute name enclosed in\\n\" +\n" +
                "                            \"parentheses. Multiple foreign key constraints are separated by commas.";
        String output = "The output is “Table created successfully” if table creation succeeds, and a\\n\" +\n" +
                "                            \"descriptive error message if it fails";
        command(syntax, description, output);
    }

    private void dropCommand () {
        String syntax = "DROP TABLE table_name;";
        String description = "The “DROP TABLE” token is followed by a table name";
        String output = "The output is “Table dropped successfully” if table dropping succeeds, and a\\n\" +\n" +
                "                            \"descriptive error message if it fails.\\n";
        command(syntax, description, output);
    }

    private void describeCommand () {
        String syntax = "DESCRIBE table_name;";
        String description = "The token “DESCRIBE” is followed by a table name.\\n\" +\n" +
                "                            \"The output should be the list of attribute names and types in the table and a list of any\\n\" +\n" +
                "                            \"constraints (primary key, foreign key, domain), one row for each attribute. If there are\\n\" +\n" +
                "                            \"any constraints for an attribute, you should print the primary key constraint first,\\n\" +\n" +
                "                            \"foreign key constraint next and domain constraints last. If the table does not exist, you\\n\" +\n" +
                "                            \"should print an error message.";
        String output = "An example output is as follows:\\n\" +\n" +
                "                            \"snum -- int -- primary key -- snum>0\\n\" +\n" +
                "                            \"sname -- char(30)\\n\" +\n" +
                "                            \"age -- int -- age > 0 AND age < 100\\n\" +\n" +
                "                            \"deptid -- int -- foreign key references Department(deptid)";
        command(syntax, description, output);
    }

    private void selectCommand () {
        String syntax = "SELECT attribute_list FROM table_list WHERE condition_list;";
        String description = "The token “SELECT” is followed by an attribute list, followed by the token “FROM”\\n\" +\n" +
                "                            \"and a table name list. This is followed by an optional “WHERE” keyword and\\n\" +\n" +
                "                            \"condition list. For simplicity, you are only asked to implement an attribute list\\n\" +\n" +
                "                            \"consisting of attribute names separated by commas and not using the dot notation, in\\n\" +\n" +
                "                            \"addition to “*”, which stands for all attributes. You can also assume that no attributes\\n\" +\n" +
                "                            \"of different tables will have the same name. The table list will also be a simple list of\\n\" +\n" +
                "                            \"table names separated by commas. The condition list has the following format:\\n\" +\n" +
                "                            \"attribute1 operator value1\\n\" +\n" +
                "                            \"OR\\n\" +\n" +
                "                            \"attribute1 operator value1 AND/OR attribute2 operator value2 AND/OR attribute3\\n\" +\n" +
                "                            \"operator value3…\\n\" +\n" +
                "                            \"The operator can be any of “=”, “!=”, “<”, “>”, “<=”, “>=”.\\n\" +\n" +
                "                            \"For simplicity, you can assume that if there are multiple conjunction/disjunction\\n\" +\n" +
                "                            \"operators in the predicate, they will all be the same operator (i.e. there will not be\\n\" +\n" +
                "                            \"AND and OR operators mixed in the same condition). Hence, the conditions do not\\n\" +\n" +
                "                            \"need to be enclosed in parentheses. The values in the conditions can either be a\\n\" +\n" +
                "                            \"constant value or the name of another attribute\" +\n" +
                "                            \"An example command is as follows:\\n\" +\n" +
                "                            \"SELECT num FROM Student, Enrolled WHERE num = snum AND age > 18;\\n\" +\n" +
                "                            \"assuming num and age are attributes of the Student table and snum is an attribute of\\n\" +\n" +
                "                            \"the Enrolled table.";
        String output = "The output of this command should be the list of matching tuples if there is no error.\\n\" +\n" +
                "                            \"Otherwise, a descriptive error message should be printed. The first line of the result\\n\" +\n" +
                "                            \"should be the names of the attributes separated by tab characters (as you would get\\n\" +\n" +
                "                            \"from Sqlplus). Then you should print the tuples, one line per record, with different\\n\" +\n" +
                "                            \"attribute values separated by tab characters.";
        command(syntax, description, output);
    }

    private void insertCommand () {
        String syntax = "INSERT INTO table_name VALUES ( val1, val2, … );";
        String description = "The “INSERT INTO” token is followed by a table name, followed by the token\\n\" +\n" +
                "                            \"“VALUES” and a list of values separated by commas enclosed in parentheses. Each\\n\" +\n" +
                "                            \"value should be either a number (integer or decimal) or a string enclosed in single\\n\" +\n" +
                "                            \"quotes. Note that you are asked to implement only one form of this command, where\\n\" +\n" +
                "                            \"the values listed are inserted into the table in the same order that they are specified,\\n\" +\n" +
                "                            \"i.e. the first value corresponds to the value of the first attribute, the second value\\n\" +\n" +
                "                            \"corresponds to the value of the second attribute etc. Note that to satisfy this\\n\" +\n" +
                "                            \"requirement, you should store the ordering of attributes when a table is created.";
        String output = "The\\n\" +\n" +
                "                            \"output should be the message “Tuple inserted successfully” if the insertion succeeds,\\n\" +\n" +
                "                            \"and a descriptive error message if it fails. ";
        command(syntax, description, output);
    }

    private void deleteCommand () {
        String syntax = "DELETE FROM table_name WHERE condition_list;";
        String description = "The “DELETE FROM” token is followed by a table name, followed by the optional\\n\" +\n" +
                "                            \"“WHERE” keyword and a condition list. The condition list has the following format:\\n\" +\n" +
                "                            \"attribute1 operator value1\\n\" +\n" +
                "                            \"OR\\n\" +\n" +
                "                            \"attribute1 operator value1 AND/OR attribute2 operator value2 AND/OR attribute3\\n\" +\n" +
                "                            \"operator value3…\\n\" +\n" +
                "                            \"The operator can be any of “=”, “!=”, “<”, “>”, “<=”, “>=”.\\n\" +\n" +
                "                            \"For simplicity, you can assume that if there are multiple conjunction/disjunction\\n\" +\n" +
                "                            \"operators in the predicate, they will all be the same operator (i.e. there will not be\\n\" +\n" +
                "                            \"AND and OR operators mixed in the same condition). Hence, the conditions do not\\n\" +\n" +
                "                            \"need to be enclosed in parentheses.\\n";
        String output = "The output should be the message “X rows affected”, where X is the number of tuples\\n\" +\n" +
                "                            \"deleted if there are no errors. Otherwise a descriptive error message should be printed.";
        command(syntax, description, output);
    }

    private void updateCommand () {
        String syntax = "UPDATE table_name SET attr1 = val1, attr2 = val2… WHERE condition_list;";
        String description = "The “UPDATE” token is followed by a table name, which is followed by the token\\n\" +\n" +
                "                            \"“SET” and a list of attribute name=attribute value pairs separated by commas. This is\\n\" +\n" +
                "                            \"followed by an optional “WHERE” token and a condition list in the same form as the\\n\" +\n" +
                "                            \"condition list in the DELETE command.";
        String output = "The output should be the message “X rows affected”, where X is the number of tuples\\n\" +\n" +
                "                            \"updated if there are no errors. Otherwise a descriptive error message should be\\n\" +\n" +
                "                            \"printed.\\n";
        command(syntax, description, output);
    }

    private void executeHelpTables() {
        String schemaCSV = "schema.csv";
        File schemaFile = new File(schemaCSV);

        // Checking if the specified file exists or not
        if (schemaFile.exists()) {
            try {
                CSVReader reader = new CSVReader(new FileReader(schemaCSV));

                //Read CSV line by line and use the string array as you want
                String[] nextLine;

                boolean tableExists = false, checkFirst = true;
                while ((nextLine = reader.readNext()) != null) {
                    if(checkFirst){
                        System.out.println("Tables Defined:");
                        tableExists = true;
                        checkFirst = false;
                    }
                    System.out.println("=> " + nextLine[0]);
                }

                if(!tableExists)
                    System.out.println(">> No Tables Defined!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                File file = new File(schemaCSV);
                boolean result = file.createNewFile();
                if(!result){
                    System.out.println("[!!] Error creating new File");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            System.out.println(">> No Tables Defined!");
        }
    }
}
