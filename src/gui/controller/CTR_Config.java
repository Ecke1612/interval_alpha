package gui.controller;

import handling.File_Handler;
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
    }

    public void save() {
        configObject.setDoUpdate(check_update.isSelected());
        configObject.setMultiClock(check_multiClock.isSelected());
        File_Handler.writeObject(configObject, "ver/config.dat");
    }

    public ConfigObject getConfigObject() {
        return configObject;
    }

    public void setConfigObject(ConfigObject configObject) {
        this.configObject = configObject;
    }

    /*
    public void loadConfig() throws IOException {

        configList.clear();
        if(File_Handler.fileExist("ver/config.txt")) {
            configList = File_Handler.fileLoader("ver/config.txt");
            try {
                if (configList.get(0).equals("0")) {
                    doUpdate = false;
                }
                if (configList.get(1).equals("1")) {
                    multiClock = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Config.txt vermutlich fehlerhaft");
            }
        }
    }

    public void save() {
        doUpdate = check_update.isSelected();
        multiClock = check_multiClock.isSelected();

        configList.add(getBoolAsString(doUpdate));
        configList.add(getBoolAsString(multiClock));
        try {
            File_Handler.fileWriter("ver/config.txt", configList);
            label_console.setText("erfolgreich gespeichert");
        } catch (IOException e) {
            e.printStackTrace();
            label_console.setText("speichern fehlgeschlagen!");
        }
    }

    private String getBoolAsString(boolean bool) {
        if(bool) return "1";
        else return "0";
    }
    */
}
