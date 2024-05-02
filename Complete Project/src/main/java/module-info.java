module com.example.sl {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.sl to javafx.fxml;
    exports com.example.sl;
}