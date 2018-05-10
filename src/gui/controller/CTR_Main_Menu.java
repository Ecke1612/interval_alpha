package gui.controller;

import handling.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import main.Main_Application;
import object.ConfigObject;

import java.io.IOException;

/**
 * Created by Eike on 28.05.2017.
 */
public class CTR_Main_Menu {

    @FXML
    public VBox vbox_main;
    @FXML
    public ToggleButton menu_dashboard;
    @FXML
    private Label version;

    public void initialize() {
        vbox_main.getStylesheets().add(getClass().getResource(Manager.getCSSPath(CTR_Config.configObject.getCssIndex())).toExternalForm());
        System.out.println(Manager.getCSSPath(CTR_Config.configObject.getCssIndex()));
        version.setText("Version: " + Main_Application.build);
    }

    public void newProject() throws IOException {
        Main_Application.ctr_dashboard.showNewProjectWindow();
    }

    public void showDashboard() throws IOException {
        Main_Application.ctr_dashboard.borderpane.setCenter(null);
        Main_Application.ctr_dashboard.showDashboardAtRuntime();
    }

    public void showReport() throws IOException {
        CTR_Report ctr_report = new CTR_Report();
        ctr_report.initialize();
    }

    public void showClient() {
        CTR_Client ctr_client = new CTR_Client();
        ctr_client.initialize();
    }

    public void showConfig() throws IOException {
        //Muss hier geladen werden, da CTR-Config auch der CTR_StartScreen ist und diese nicht die fxml laden sollte/kann
        Parent configUI = FXMLLoader.load(getClass().getResource("/fxml/config.fxml"));
        Main_Application.ctr_dashboard.borderpane.setCenter(configUI);
    }

}
