package gui.controller;

import handling.Alert_Windows;
import handling.CSV_ProjectHandler;
import handling.Manager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import object.ClientStorageObject;
import object.StorageObject;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by Eike on 30.05.2017.
 */
public class CTR_Project_Edit {

    @FXML
    public TextField textfield_name;
    @FXML
    public TextField textfield_hour;
    @FXML
    public TextField textfield_min;
    @FXML
    public TextField textfield_maxHour;
    @FXML
    public ChoiceBox chb_client;
    @FXML
    public Label label_fail;
    @FXML
    public TextField textField_addMin;

    private CSV_ProjectHandler csv_projectHandler = new CSV_ProjectHandler();
    private String path = "";

    private CTR_Project_Module project;
    //private String name;
    //private int rawSeconds;
    private int minutes;
    private int hours;
    //private int maxHour;
    private Stage stage;
    //private String client;

    public CTR_Project_Edit(CTR_Project_Module project, Stage stage){
        this.project = project;
        //this.name = name;
        //rawSeconds = seconds;
        this.minutes = (project.getMainSec() / 60) % 60;
        hours = project.getMainSec() / 3600;
        //this.maxHour = maxHour;
        this.stage = stage;
        //this.client = client;
        path = project.getProjectpath();
    }

    public void initialize() {
        textfield_name.setText(project.getName());
        textfield_hour.setText(Integer.toString(hours));
        textfield_min.setText(Integer.toString(minutes));
        textfield_maxHour.setText(Integer.toString(project.getMaxTimeHours()));
        textField_addMin.setPromptText("0");

        int i = 0;
        int index = 0;
        for(ClientStorageObject c : Manager.clients) {
            if(c.getName().equals(project.getClientName())) {
                index = i;
            }
            chb_client.getItems().add(c.getName());
            i++;
        }
        chb_client.getSelectionModel().select(index);
    }

    public void projPath() {
        String temp = Alert_Windows.chooseDir();
        if(!temp.equals("")) {
            path = temp;
        }
    }


    public void save() throws IOException {
        boolean approved = true;
        boolean alreadyAsked = false;

        //Nsme ändern was auch jede Namensvariable in den Storageobjekten umfasst
        if(!textfield_name.getText().equals(project.getName())) {
            System.out.println("name geändert: " + textfield_name.getText());
            project.setName(textfield_name.getText());
            //project.label_projName.setText(textfield_name.getText());
        }
        //Zeit Korrektur überprüfen ------------------------------------------------------------------------------------
        //Stunden als auch Minuten wurde geändert
        if(!textfield_hour.getText().equals(Integer.toString(hours)) && !textfield_min.getText().equals(Integer.toString(minutes))){
            try {
                int hours = Integer.parseInt(textfield_hour.getText());
                int minutes = Integer.parseInt(textfield_min.getText()) + checkAddMin();
                setProjectTime(hours, minutes);
                System.out.println("Es wurden Stunden und MInuten geändert");
                alreadyAsked = true;
            } catch (Exception e) {
                e.printStackTrace();
                approved = false;
                label_fail.setText("Stunden und Minuten enthalten einen Fehler");
            }
        //Ausschließlich Stunden wurden geändert
        } else if (!textfield_hour.getText().equals(Integer.toString(hours)) && textfield_min.getText().equals(Integer.toString(minutes))){
            try {
                int hours = Integer.parseInt(textfield_hour.getText());
                minutes = minutes + checkAddMin();
                setProjectTime(hours, minutes);
                System.out.println("Es wurden nur Stunden geändert");
                alreadyAsked = true;
            }catch (Exception e) {
                e.printStackTrace();
                approved = false;
                label_fail.setText("das Stundenfeld enthält einen Fehler");
            }
        //Nur das Minutenfeld wurde geändert
        } else if(textfield_hour.getText().equals(Integer.toString(hours)) && !textfield_min.getText().equals(Integer.toString(minutes))) {
            try {
                int minutes = Integer.parseInt(textfield_min.getText())  + checkAddMin();
                setProjectTime(hours, minutes);
                System.out.println("Es wurden nur Minuten geändert");
                alreadyAsked = true;
            }catch (Exception e) {
                e.printStackTrace();
                approved = false;
                label_fail.setText("das Minutenfeld enthält einen Fehler");
            }
        }
        //Und wenn alles das nicht der Fall ist, dann weiß ich auch nicht
        else {
            label_fail.setText("unvorhergesehener Fehler");
        }
        //--------------------------------------------------------------------------------------------------------------
        //Minuten hinzufügen oder Abziehen

        if(!textField_addMin.getText().equals("")) {
            if(!alreadyAsked) {
                try {
                    int minutes = Integer.parseInt(textField_addMin.getText()) + this.minutes;
                    setProjectTime(hours, minutes);
                    System.out.println("Min wurden hinzugefügt");

                } catch (Exception e) {
                    e.printStackTrace();
                    label_fail.setText("Fehler im Feld Minuten addieren");
                }
            }
        }

        //--------------------------------------------------------------------------------------------------------------

        //überprüfen ob Kunde geändert wurde
        if(!chb_client.getSelectionModel().getSelectedItem().equals(project.getClientName())) {
            System.out.println("Kunde geändert: " + (String) chb_client.getSelectionModel().getSelectedItem());
            ClientStorageObject clientElement = Manager.clients.get((chb_client.getSelectionModel().getSelectedIndex()));
            project.setClient(clientElement);
            //project.label_client.setText(clientElement.getName());
        }

        //Überprüfen ob maxTime geänder wurde
        if(!textfield_maxHour.getText().equals(Integer.toString(project.getMaxTimeHours()))) {
            try {
                int newmaxHour = Integer.parseInt(textfield_maxHour.getText());
                project.setMaxTimeHours(newmaxHour);
                System.out.println("max hours geändert");
            } catch (Exception e) {
                label_fail.setText("Die maximale Zeit konnte nicht geändert werden");
            }
        }

        //Überprüfe Pfad
        if(!path.equals(project.getProjectpath())) {
            project.setProjectpath(path);
            project.menu_goToDir.setDisable(false);
            System.out.println("Pfad geändert");
        }

        if(approved) {
            csv_projectHandler.csvWriter();
            stage.close();
            System.out.println("seceonds1 : " + project.getMainSec());
            project.initialize();
        }
    }

    //überprüfe auch das AddMin feld
    private int checkAddMin() {
        int minutes = 0;
        if(!textField_addMin.getText().equals("")) {
            try {
                minutes = Integer.parseInt(textField_addMin.getText());
                System.out.println("min added");
                return minutes;

            } catch (Exception e) {
                e.printStackTrace();
                label_fail.setText("Fehler im Feld Minuten addieren");
            }
        }
        return minutes;
    }

    //rechnet zunächst die Stunden und Minuten in reine Minuten um und ersetzt sie im Projekt
    //dann wird die Zeit für das letzte StorageObjekt berechnet
    private void setProjectTime(int hours, int minutes) {
        System.out.println("Set Project Time");
        minutes = (hours * 60) + minutes;
        int seconds = minutes * 60;
        project.label_time.setText(Manager.printTime(seconds));
        String comment = "Korrektur";
        if((seconds - project.getMainSec()) > 0) {
            comment = Alert_Windows.inputBox("Kommentar", "Kommentar für die korrigierte Zeit einfügen", "Was hast du in dieser Zeit gemacht?");
            if (comment.equals("")) {
                //comment = "Zeit korrigiert";
                StorageObject lastStore = project.getStorageObjects().get(project.getStorageObjects().size()-1);
                lastStore.setSeconds(lastStore.getSec() + (seconds - project.getMainSec()));
            } else {
                project.addStorageObject(new StorageObject(LocalDate.now(),seconds-project.getMainSec(), comment));
            }
        } else {
            StorageObject lastStore = project.getStorageObjects().get(project.getStorageObjects().size()-1);
            lastStore.setSeconds(lastStore.getSec() + (seconds - project.getMainSec()));
        }

        project.setMainSec(seconds);
        System.out.println("sec-prosec: " + (seconds-project.getMainSec()));
    }

    public void abort() {
        stage.close();
    }

}
