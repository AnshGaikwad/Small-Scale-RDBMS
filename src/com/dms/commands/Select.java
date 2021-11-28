package com.dms.commands;

public class Select {

    String command;

    public Select(String c) {
        command = c;
    }

    public void selectFromTable() {
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[2];
        String tableCSV = tableName + ".csv";

    }
}
