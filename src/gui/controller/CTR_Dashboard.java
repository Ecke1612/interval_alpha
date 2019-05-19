package gui.controller;

import handling.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main_Application;
import object.ReminderObject;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * Created by Eike on 20.05.2017.
 */
public class CTR_Dashboard implements Initializable {

    @FXML
    public VBox vbox_projList;
    @FXML
    public BorderPane borderpane;
    @FXML
    public ToggleButton btn_switchMenu;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    HBox left_menu_hbox;
    @FXML
    private Parent em_Menu;
    @FXML
    private CTR_Main_Menu em_MenuController;
    @FXML
    ToggleButton getBtn_switchMenu;

    public static Stage stageNewProject;
    private CSV_ProjectHandler csv_projectHandler = new CSV_ProjectHandler();
    private CSV_ClientHandler csv_clientHandler = new CSV_ClientHandler();
    private Designer designer = new Designer();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //borderpane.getStylesheets().add(getClass().getResource(Manager.getCSSPath(CTR_Config.configObject.getCssName())).toExternalForm());
        //setze den CTR_StartScreen als statische Variable in main ein und setze den Dashboardbutton übern den
        // inludierten Menu CTR_StartScreen auf selected
        Main_Application.setdashboardController(this);
        em_MenuController.menu_dashboard.setSelected(true);
        //btn_switchMenu.getStyleClass().add(".green");
        btn_switchMenu.setStyle(designer.returner("background", CTR_Config.configObject.getCssName()));
        System.out.println(btn_switchMenu.getStyleClass().toString());
        //getBtn_switchMenu.getStyleClass().add(".dashboard .menuswitch-btns");
        //Clients laden
        if(csv_clientHandler.fileExist("data/clients.csv")) {
            try {
                csv_clientHandler.csvLoader();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Wenn die csv Datei existiert, dann lade sie ins Programm
        if(csv_projectHandler.fileExist("data/trackingData.csv")) {
            try {
                csv_projectHandler.csvLoader();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Checking Reminders
        Timeline reminderTime = new Timeline();
        reminderTime.setCycleCount(Timeline.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.seconds(15), event -> {
            for(CTR_Project_Module project : Manager.projectList) {
                if(project.getReminderObject() != null) {
                    LocalTime localTime = LocalTime.now();
                    if(localTime.getHour() == project.getReminderObject().getHour() && localTime.getMinute() == project.getReminderObject().getMin()) {
                        showRemind(project);
                    }
                }
            }
        });
        reminderTime.getKeyFrames().add(frame);
        reminderTime.play();
    }

    public CTR_Dashboard() {

    }

    //lädt das NeueProjekt-Fenster
    public void showNewProjectWindow() throws IOException {
        stageNewProject = new Stage();

        //Parent newProject = FXMLLoader.load(getClass().getResource("/gui/fxml/new_project.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/new_project.fxml"));
        CTR_newProject ctr_newProject = new CTR_newProject();
        fxmlLoader.setController(ctr_newProject);
        Parent newProject = fxmlLoader.load();

        stageNewProject.setTitle("Neues Projekt anlegen");
        Scene sceneNewProject = new Scene(newProject);
        stageNewProject.initOwner(Main_Application.primaryStage);
        stageNewProject.setScene(sceneNewProject);
        stageNewProject.showAndWait();

    }

    public void showDashboardAtRuntime() throws IOException {
        vbox_projList.getChildren().clear();
        for(Parent projectUI : Manager.projectUIList) {
            vbox_projList.getChildren().add(projectUI);
        }
        scrollPane.setContent(vbox_projList);
        borderpane.setCenter(scrollPane);
    }

    public void switchMenu() {
        int menuSize = 324;

        if(btn_switchMenu.isSelected()) {
            left_menu_hbox.setMinWidth(30);
            left_menu_hbox.setPrefWidth(30);
            left_menu_hbox.setMaxWidth(30);
            btn_switchMenu.setText(">");
            Main_Application.primaryStage.setWidth(Main_Application.primaryStage.getWidth() - menuSize);
        } else {
            left_menu_hbox.setMinWidth(menuSize);
            left_menu_hbox.setPrefWidth(menuSize);
            left_menu_hbox.setMaxWidth(menuSize);
            btn_switchMenu.setText("<");
            Main_Application.primaryStage.setWidth(Main_Application.primaryStage.getWidth() + menuSize);
        }
    }

    public void removeProject(int index) {
        vbox_projList.getChildren().remove(index);
    }

    public void showRemind(CTR_Project_Module project) {
        Stage reminderStage = new Stage();
        reminderStage.setTitle("Erinnerung");
        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(10));
        Scene scene = new Scene(vBox);
        reminderStage.setScene(scene);

        Label label_Header = new Label("Projekt: " + project.getName() + " erinnert an:");
        Label label_text = new Label(project.getReminderObject().getText());

        Button btn_okay = new Button("Okay");
        btn_okay.setOnAction(event -> {
            project.deleteReminder();
            reminderStage.close();
        });

        vBox.getChildren().addAll(label_Header, label_text, btn_okay);
        reminderStage.setAlwaysOnTop(true);
        reminderStage.show();
    }

}
