package gui.controller;

import handling.File_Handler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import object.ConfigObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Eike on 21.06.2017.
 */
public class CTR_Config {

    @FXML
    public CheckBox check_update;
    @FXML
    public CheckBox check_multiClock;
    @FXML
    public CheckBox check_autostop;
    @FXML
    Label label_console;
    @FXML
    public ComboBox cbox_interval;
    @FXML
    public ComboBox cbox_minTime;
    @FXML
    public ComboBox cbox_rushHour;

    public static ConfigObject configObject;

    public CTR_Config() {
        configObject = new ConfigObject();
        System.out.println(configObject.isDoUpdate());
        if(File_Handler.fileExist("ver/config.dat")) {
            try {
                configObject = (ConfigObject) File_Handler.loadObjects("ver/config.dat");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("objekteloading fehlgeschlagen");
            }
        }
    }

    public void initialize() {
        check_update.setSelected(configObject.isDoUpdate());
        check_multiClock.setSelected(configObject.isMultiClock());
        check_autostop.setSelected(configObject.isAutostop());

        initCbox();
        for(int i=0; i < cbox_interval.getItems().size(); i++) {
            if(configObject.getAutostopinterval() == (int)cbox_interval.getItems().get(i)) {
                cbox_interval.getSelectionModel().select(i);
            }
        }

        for(int i=0; i < cbox_minTime.getItems().size(); i++) {
            if(configObject.getAutostopMinTime() == (int)cbox_minTime.getItems().get(i)) {
                cbox_minTime.getSelectionModel().select(i);
            }
        }

        for(int i=0; i < cbox_rushHour.getItems().size(); i++) {
            if(configObject.getAutostopRushHour() == (int)cbox_rushHour.getItems().get(i)) {
                cbox_rushHour.getSelectionModel().select(i);
            }
        }

        check_autostop.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                label_console.setText("Auto-Stop funktion geändert. Bitte Uhr einmal pausieren und wieder starten");
                save();
            }
        });

        check_update.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                label_console.setText("Update-Funktion geändert");
                save();
            }
        });

        check_multiClock.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                label_console.setText("Multiclock-Funktion geändert");
                save();
            }
        });

        cbox_interval.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                label_console.setText("Erweiterte Auto-Stop funktion geändert. Bitte Uhr einmal pausieren und wieder starten");
                save();
            }
        });

        cbox_minTime.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                label_console.setText("Erweiterte Auto-Stop funktion geändert. Bitte Uhr einmal pausieren und wieder starten");
                save();
            }
        });

        cbox_rushHour.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                label_console.setText("Erweiterte Auto-Stop funktion geändert. Bitte Uhr einmal pausieren und wieder starten");
                save();
            }
        });
    }

    private void initCbox(){
        for(int i = 5; i <= 60; i += 5) {
            cbox_interval.getItems().add(i);
        }
        for(int i = 1; i < 6; i++) {
            cbox_minTime.getItems().add(i);
        }
        for(int i = 8; i <= 24; i++) {
            cbox_rushHour.getItems().add(i);

        }
    }

    public void save() {
        configObject.setDoUpdate(check_update.isSelected());
        configObject.setMultiClock(check_multiClock.isSelected());
        configObject.setAutostop(check_autostop.isSelected());
        configObject.setAutostopinterval((int) cbox_interval.getSelectionModel().getSelectedItem());
        configObject.setAutostopMinTime((int) cbox_minTime.getSelectionModel().getSelectedItem());
        configObject.setAutostopRushHour((int) cbox_rushHour.getSelectionModel().getSelectedItem());
        File_Handler.writeObject(configObject, "ver/config.dat");
        //label_console.setText("Änderungen gespeichert!");
    }

    public ConfigObject getConfigObject() {
        return configObject;
    }

    public void setConfigObject(ConfigObject configObject) {
        this.configObject = configObject;
    }

}
