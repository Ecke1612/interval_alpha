package gui.controller;


import handling.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Main_Application;
import java.io.IOException;

/**
 * Created by Eike on 28.05.2017.
 */
public class CTR_Main_Menu {

    @FXML
    public VBox vbox_main;
    //@FXML
    //public ToggleButton menu_dashboard;
    @FXML
    public Label version;
    @FXML
    public ImageView logo_image;
    @FXML
    public Button btn_neuProj;
    @FXML
    public ToggleButton menu_dashboard;
    @FXML
    public ToggleButton btn_client;
    @FXML
    public ToggleButton btn_reports;
    @FXML
    public ToggleButton btn_config;
    @FXML
    public Label label_impressum;
    @FXML
    public ToggleButton btn_switchMenu;

    private int menu_width = 249;


    public void initialize() {
        //vbox_main.getStylesheets().add(getClass().getResource(Manager.getCSSPath(CTR_Config.configObject.getCssName())).toExternalForm());
        version.setText("/  VERSION: " + Main_Application.build);
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

    public void switchMenu() {
        if(btn_switchMenu.isSelected()) {
            logo_image.setImage(new Image(getClass().getResource("/images/logo_single.png").toExternalForm()));
            btn_neuProj.setText("");
            menu_dashboard.setText("");
            btn_client.setText("");
            btn_reports.setText("");
            btn_config.setText("");
            btn_config.setGraphic(new ImageView("/images/pencil.png"));
            btn_config.getGraphic().setOpacity(0.5);
            version.setText("");
            label_impressum.setText("");
            btn_switchMenu.setText(">");
            Main_Application.primaryStage.setWidth(Main_Application.primaryStage.getWidth() - menu_width);
        } else {
            logo_image.setImage(new Image(getClass().getResource("/images/logo.png").toExternalForm()));
            btn_neuProj.setText("NEUES PROJEKT STARTEN");
            menu_dashboard.setText("PROJEKTE");
            btn_client.setText("KUNDEN");
            btn_reports.setText("BERICHTE");
            btn_config.setText("EINSTELLUNGEN");
            btn_config.setGraphic(null);
            version.setText("/  VERSION: " + Main_Application.build);
            label_impressum.setText("© Eike Dreyer");
            btn_switchMenu.setText("<");
            Main_Application.primaryStage.setWidth(Main_Application.primaryStage.getWidth() + menu_width);
        }
    }

}
