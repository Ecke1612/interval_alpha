package gui.controller;

import handling.File_Handler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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


    private ArrayList<String> configList;
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

        check_autostop.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                label_console.setText("Auto-Stop funktion geändert. Bitte Uhr einmal pausieren und wieder starten");
            }
        });

        check_update.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                label_console.setText("Update-Funktion geändert");
            }
        });

        check_multiClock.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                label_console.setText("Multiclock-Funktion geändert");
            }
        });
    }

    public void save() {
        configObject.setDoUpdate(check_update.isSelected());
        configObject.setMultiClock(check_multiClock.isSelected());
        configObject.setAutostop(check_autostop.isSelected());
        File_Handler.writeObject(configObject, "ver/config.dat");
        label_console.setText("Änderungen gespeichert!");
    }

    public ConfigObject getConfigObject() {
        return configObject;
    }

    public void setConfigObject(ConfigObject configObject) {
        this.configObject = configObject;
    }

}
