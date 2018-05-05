package gui.controller;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Main_Application;

import java.time.LocalDate;

/**
 * Created by Eike on 06.06.2017.
 */
public class CTR_Archiv {

    private ScrollPane scrollPane = new ScrollPane();
    private VBox vboxMain = new VBox();
    private  HBox hboxHeader = new HBox();
    private VBox vboxEntrys = new VBox();
    private DatePicker datePicker = new DatePicker(LocalDate.now());
    private TextField searchField =new TextField();
    private Button btn_search = new Button();

    public void initialize() {
        btn_search.setText("suche");

        hboxHeader.getChildren().addAll(datePicker, searchField, btn_search);
        vboxMain.getChildren().addAll(hboxHeader, vboxEntrys);
        scrollPane.setContent(vboxMain);
        Main_Application.ctr_dashboard.borderpane.setCenter(null);
        Main_Application.ctr_dashboard.borderpane.setCenter(scrollPane);
        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            System.out.println("selected Date: " + date);
            updateView(date);
        });

    }

    private void updateView(LocalDate date) {

    }

}
