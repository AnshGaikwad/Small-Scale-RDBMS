module com.ayg.rdbms {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencsv;
    requires java.sql;


    opens com.ayg.rdbms to javafx.fxml;
    exports com.ayg.rdbms;
}