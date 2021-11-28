package com.dms.commands;

public class Update {
    String command;

    public Update(String c) {
        command = c;
    }

    public String updateTable() {
        String schemaCSV = "schema.csv";
        String tableName = command.split(" ")[2];
        String tableCSV = tableName + ".csv";

        return tableName;
    }
}
