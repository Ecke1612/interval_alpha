package main.launcher;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CTR_StartScreen {

    @FXML
    public Label label_console;
    @FXML
    VBox vbox_bottom;

    private SimpleStringProperty strConsole = new SimpleStringProperty();

    public void initialize() {
        strConsole.set("output");
        label_console.textProperty().bind(strConsole);
    }

    public void updateConsole(String value) {
        strConsole.set(value);
        System.out.println("ausgeführt: " + strConsole.get());
    }


}
