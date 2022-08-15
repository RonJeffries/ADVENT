module com.ronjeffries.adventureFour {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires tornadofx;


    opens com.ronjeffries.adventureFour to javafx.fxml;
    exports com.ronjeffries.adventureFour;
}