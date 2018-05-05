package gui.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class AutoStopAlert {

    private int actualSeconds;
    private CTR_Project_Module prj_module;

    private ComboBox hourBox = new ComboBox();
    private ComboBox minBox = new ComboBox();

    public AutoStopAlert(int actuelSeconds, CTR_Project_Module prj_module) {
        this.actualSeconds = actuelSeconds;
        this.prj_module = prj_module;
        LocalDateTime dateTimeinit = LocalDateTime.now();

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
        Button btn_saveCorrection = new Button("Korrektur speichern");
        btn_saveCorrection.setVisible(false);

        btn_yes.setOnAction(event -> {
            prj_module.autoStopTimeline.play();
            prj_module.autoStopOffset = prj_module.getNewSec();
            autoStopStage.close();
        });

        btn_no.setOnAction(event -> {
            LocalDateTime dateTimeNow = LocalDateTime.now();
            System.out.println(dateTimeNow.getHour());
            if(dateTimeinit.getDayOfMonth() == dateTimeNow.getDayOfMonth()) {
                //hourBox = createHourBox(dateTimeNow.getHour());
                //minBox = createMinBox(dateTimeNow.getMinute());

                hbox_Time.getChildren().addAll(hourBox, minBox);
            } else {
                hourBox = createHourBox(24);
                minBox = createMinBox(60);
                hbox_Time.getChildren().addAll(hourBox, minBox);
            }
            btn_saveCorrection.setVisible(true);


        });

        btn_saveCorrection.setOnAction(event -> {
            //Differenz ausrechnen von moment des klickens rückwirkend zu eingetstellter Zeit, denn bis hierhin sind die Sekunden ja weitergelaufen
            int correctedSecongs = 0;
            int hour = (int) hourBox.getSelectionModel().getSelectedItem();
            LocalDateTime newDateTime = LocalDateTime.of(dateTimeinit.getYear(), dateTimeinit.getMonth(), dateTimeinit.getDayOfMonth(),
                    (int) hourBox.getSelectionModel().getSelectedItem(),(int) minBox.getSelectionModel().getSelectedItem(), 0);
            long difference = java.time.Duration.between(newDateTime, LocalDateTime.now()).getSeconds();
            System.out.println("difference: " + difference);
        });

        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        hbox.setSpacing(10);
        hbox_Time.setSpacing(10);

        hbox.getChildren().addAll(btn_yes, btn_no);
        hbox_Time.getChildren().addAll();
        vbox.getChildren().addAll(message, hbox, hbox_Time, btn_saveCorrection);
        autoStopStage.show();
    }

    private ComboBox createHourBox(int hour) {
        ComboBox comboBox = new ComboBox();
        for(int i = 8; i < hour; i++) {
            comboBox.getItems().add(i);
        }
        comboBox.getSelectionModel().selectFirst();
        return comboBox;
    }

    private ComboBox createMinBox(int minutes) {
        ComboBox comboBox = new ComboBox();
        for(int i = 5; i < minutes; i += 5) {
            comboBox.getItems().add(i);
        }
        comboBox.getSelectionModel().selectFirst();
        return comboBox;
    }

}
