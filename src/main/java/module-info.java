module com.example.mokit_r31 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.mokit_r31 to javafx.fxml;
    exports com.example.mokit_r31;
}