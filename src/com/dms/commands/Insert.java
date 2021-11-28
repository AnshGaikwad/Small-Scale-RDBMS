package com.dms.commands;

public class Insert {
    String command;
    public Insert (String c){
        command = c;
    }

    public String insertInsideTable(){
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[2];
        String tableCSV = tableName + ".csv";

        return tableName;
    }
}
