package com.dms;

import com.dms.commands.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)  {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String command = sc.nextLine();

//            try {
            if (command.contains("HELP")) {
                Help help = new Help(command);
                help.executeHelp();
            } else if (command.contains("CREATE TABLE")) {
                Create create = new Create(command);
                String createdTable = create.createTable();
                if (createdTable != null)
                    System.out.println(">> " + createdTable + " Table Created Successfully");
            } else if (command.contains("DROP TABLE")) {
                Drop drop = new Drop(command);
                String droppedTable = drop.dropTable();
                if (droppedTable != null)
                    System.out.println(">> " + droppedTable + " Table Dropped Successfully");
            } else if (command.contains("DESCRIBE")) {
                Describe describe = new Describe(command);
                describe.describeTable();
            } else if (command.contains("INSERT")) {
                Insert insert = new Insert(command);
                String insertedInTable = insert.insertInsideTable();
                if (insertedInTable != null)
                    System.out.println(">> Tuple inserted inside " + insertedInTable + " Successfully");
            } else if (command.contains("DELETE")) {
                Delete delete = new Delete(command);
                int numOfRowsAffected = delete.deleteFromTable();
                if (numOfRowsAffected == 1)
                    System.out.println(">> " + numOfRowsAffected + " Row Affected");
                else if (numOfRowsAffected != -1)
                    System.out.println(">> " + numOfRowsAffected + " Rows Affected");
            } else if (command.contains("UPDATE")) {
                Update update = new Update(command);
                int numOfRowsAffected = update.updateTable();
                if (numOfRowsAffected == 1)
                    System.out.println(">> " + numOfRowsAffected + " Row Affected");
                else if (numOfRowsAffected != -1)
                    System.out.println(">> " + numOfRowsAffected + " Rows Affected");
            } else if (command.contains("SELECT")) {
                Select select = new Select(command);
                select.selectFromTable();
            } else if (command.contains("Quit")) {
                break;
            } else {
                System.out.println("[!!] Invalid Input");
            }
//            catch (Exception e){
//                System.out.println("[!!] Unable to process Command");
//            }
        }
        }
    }
