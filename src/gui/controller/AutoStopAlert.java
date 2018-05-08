package gui.controller;

import handling.Alert_Windows;
import handling.CSV_ProjectHandler;
import handling.Manager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import object.StorageObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AutoStopAlert {

    private int actualSeconds;
    private CTR_Project_Module prj_module;

    private ComboBox hourBox = new ComboBox();
    private ComboBox minBox = new ComboBox();

    public AutoStopAlert(int actuelSeconds, CTR_Project_Module prj_module) {
        //this.actualSeconds = actuelSeconds;
        this.prj_module = prj_module;
        LocalDateTime dateTimeinit = LocalDateTime.now();
        System.out.println("actuelSec: " + actuelSeconds);

        Stage autoStopStage = new Stage();
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        HBox hbox_Time = new HBox();
        Scene scene = new Scene(vbox,380,200);
        autoStopStage.setTitle("Noch da?");
        autoStopStage.setScene(scene);

        Label message = new Label("Es wurde inaktivität nach feierabend festgestellt. Bist du noch da?");
        String zero = "";
        if(dateTimeinit.getMinute() < 10){
            zero = "0";
        }
        Label label_initTime = new Label("Festgestellt um: " + dateTimeinit.getHour() + ":" + zero +dateTimeinit.getMinute() + " Uhr!");
        Button btn_yes = new Button("Ja, mach weiter");
        Button btn_no = new Button("Nein, Zeit korrigieren");
        Label label_explanation = new Label("Stelle die Uhrzeit ein, bis wann die Uhr hätte laufen soll");
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
                hourBox = createHourBox(dateTimeNow.getHour());
                minBox = createMinBox(60);

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
            int correctedSeconds;
            LocalDateTime newDateTime = LocalDateTime.of(dateTimeinit.getYear(), dateTimeinit.getMonth(), dateTimeinit.getDayOfMonth(),
                    (int) hourBox.getSelectionModel().getSelectedItem(),(int) minBox.getSelectionModel().getSelectedItem(), 0);
            long difference = java.time.Duration.between(newDateTime, LocalDateTime.now()).getSeconds();
            //correctedSeconds = prj_module.get() - (int) difference;
            int corMainSec = prj_module.getMainSec() - (int) difference;
            System.out.println("newTimeTime: " + newDateTime + "now: " + LocalDateTime.now() + "difference: " + difference);
            setProjectTime(corMainSec);
            try {
                prj_module.clockStop();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                CSV_ProjectHandler.csvWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            prj_module.initialize();
            autoStopStage.close();
        });

        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        hbox.setSpacing(10);
        //hbox.setPadding(new Insets(10));
        hbox_Time.setSpacing(10);
        //hbox_Time.setPadding(new Insets(10));

        hbox.getChildren().addAll(btn_yes, btn_no);
        hbox_Time.getChildren().addAll();
        vbox.getChildren().addAll(message, label_initTime, hbox, label_explanation, hbox_Time, btn_saveCorrection);
        autoStopStage.show();

        autoStopStage.setOnCloseRequest(event -> {
            prj_module.autoStopTimeline.play();
            prj_module.autoStopOffset = prj_module.getNewSec();
            autoStopStage.close();
        });
    }

    private ComboBox createHourBox(int hour) {
        ComboBox comboBox = new ComboBox();
        for(int i = 8; i < hour+1; i++) {
            comboBox.getItems().add(i);
        }
        comboBox.getSelectionModel().selectFirst();
        return comboBox;
    }

    private ComboBox createMinBox(int minutes) {
        ComboBox comboBox = new ComboBox();
        for(int i = 0; i < minutes; i += 5) {
            comboBox.getItems().add(i);
        }
        comboBox.getSelectionModel().selectFirst();
        return comboBox;
    }

    //rechnet zunächst die Stunden und Minuten in reine Minuten um und ersetzt sie im Projekt
    //dann wird die Zeit für das letzte StorageObjekt berechnet
    private void setProjectTime(int seconds) {
        //0int minutes = (seconds / 60) % 60;
        //int hours = seconds / 3600;
        System.out.println("Set MainProject Time: " + seconds);
        //System.out.println("set newsec: " + newnewsec);
        //int sec = minutes * 60;
        prj_module.label_time.setText(Manager.printTime(seconds));
        //prj_module.setNewSec(newnewsec);

        //StorageObject lastStore = prj_module.getStorageObjects().get(prj_module.getStorageObjects().size()-1);
        //lastStore.setSeconds(lastStore.getSec() + (seconds - prj_module.getMainSec()));
        prj_module.addStorageObject(new StorageObject(LocalDate.now(),seconds - prj_module.getMainSec(), "Auto-Stop-Korrekttur"));

        prj_module.setMainSec(seconds);


    }

}
