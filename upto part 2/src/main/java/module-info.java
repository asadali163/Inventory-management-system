module com.example.sl {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sl to javafx.fxml;
    exports com.example.sl;
}