package com.dms.commands;

public class Delete {
    String command;
    public Delete (String c){
        command = c;
    }

    public String deleteFromTable(){
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[2];
        String tableCSV = tableName + ".csv";

        return tableName;
    }
}
