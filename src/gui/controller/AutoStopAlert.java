package gui.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AutoStopAlert {

    private int actualSeconds;
    private CTR_Project_Module prj_module;

    public AutoStopAlert(int actuelSeconds, CTR_Project_Module prj_module) {
        this.actualSeconds = actuelSeconds;
        this.prj_module = prj_module;

        Stage autoStopStage = new Stage();
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        HBox hbox_Time = new HBox();
        Scene scene = new Scene(vbox,380,160);
        autoStopStage.setTitle("Noch da?");
        autoStopStage.setScene(scene);

        Label message = new Label("Es wurde inaktivität nach feierabend festgestellt. Bist du noch da?");
        Button btn_yes = new Button("Ja, mach weiter");
        Button btn_no = new Button("Nein, Zeit korrigieren");
        ComboBox hourBox = createHourBox();
        ComboBox minBox = createMinBox();
        Button btn_saveCorrection = new Button("Korrektur speichern");

        btn_yes.setOnAction(event -> {
            prj_module.autoStopTimeline.play();
            prj_module.autoStopOffset = prj_module.getNewSec();
            autoStopStage.close();
        });

        btn_no.setOnAction(event -> {

        });

        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        hbox.setSpacing(10);
        hbox_Time.setSpacing(10);

        hbox.getChildren().addAll(btn_yes, btn_no);
        hbox_Time.getChildren().addAll(hourBox, minBox);
        vbox.getChildren().addAll(message, hbox, hbox_Time, btn_saveCorrection);
        autoStopStage.show();
    }

    private ComboBox createHourBox() {
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("0", "1","2","3","4","5","6","7","8","9","10","11","12",
                "13","14","15","16","17","18","19","20","21","22","23","24");
        comboBox.getSelectionModel().selectFirst();
        return comboBox;
    }

    private ComboBox createMinBox() {
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("0","5","10","15","20","25","30","35","40","45","50","55","60");
        comboBox.getSelectionModel().selectFirst();
        return comboBox;
    }

}
