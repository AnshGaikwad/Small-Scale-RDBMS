package com.ayg.rdbms;

import com.ayg.rdbms.commands.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Objects;

public class Controller {

    @FXML
    private Label outputLabel;

    @FXML
    private Button executeButton;

    @FXML
    private TextField commandEditText;

    @FXML
    private Text timeText;

    @FXML
    protected void execute() {

        long start = System.currentTimeMillis();

        // start of function

        String command = commandEditText.getText();

        if (command.contains("HELP")) {
            Help help = new Help(command);
            String output = help.executeHelp();
            outputLabel.setText(output);
        } else if (command.contains("CREATE TABLE")) {
            Create create = new Create(command);
            String output = create.createTable();
            if (Objects.equals(output, ""))
                outputLabel.setText(">> " + command.split(" ")[2] + " Table Created Successfully");
            else
                outputLabel.setText(output);
        } else if (command.contains("DROP TABLE")) {
            Drop drop = new Drop(command);
            String output = drop.dropTable();
            if (Objects.equals(output, ""))
                outputLabel.setText(">> " + command.split(" ")[2] + " Table Dropped Successfully");
            else
                outputLabel.setText(output);
        } else if (command.contains("DESCRIBE")) {
            Describe describe = new Describe(command);
            String output = describe.describeTable();
            outputLabel.setText(output);
        } else if (command.contains("INSERT")) {
            Insert insert = new Insert(command);
            String output = insert.insertInsideTable();
            if (Objects.equals(output, ""))
                outputLabel.setText(">> Tuple inserted inside " + command.split(" ")[2] + " Successfully");
            else
                outputLabel.setText(output);
        } else if (command.contains("DELETE")) {
            Delete delete = new Delete(command);
            String output = delete.deleteFromTable();
            if (output.charAt(0) == '1')
                outputLabel.setText(">> " + output + " Row Affected");
            else if (output.charAt(0) != '[')
                outputLabel.setText(">> " + output + " Rows Affected");
            else
                outputLabel.setText(output);
        } else if (command.contains("UPDATE")) {
            Update update = new Update(command);
            String output = update.updateTable();
            if (output.charAt(0) == '1')
                outputLabel.setText(">> " + output + " Row Affected");
            else if (output.charAt(0) != '[')
                outputLabel.setText(">> " + output + " Rows Affected");
            else
                outputLabel.setText(output);
        } else if (command.contains("SELECT")) {
            Select select = new Select(command);
            String output = select.selectFromTable();
            outputLabel.setText(output);
        } else if (command.contains("QUIT")) {
            System.exit(0);
        } else {
            outputLabel.setText("[!!] Invalid Command");
        }

        long end = System.currentTimeMillis();
        timeText.setText("Time Taken: " + (end - start) + " ms");
    }
}