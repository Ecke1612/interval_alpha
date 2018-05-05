package main;

import gui.controller.CTR_Project_Module;
import handling.Archiv_Handler;
import handling.CSV_ProjectHandler;
import handling.File_Handler;
import handling.Manager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import gui.controller.CTR_Dashboard;
import javafx.stage.WindowEvent;
import main.launcher.Launcher;

import java.io.IOException;

public class Main_Application extends Application {

    public static final String build = "0.5";

    private final String fn = "SourceSansPro-";
    private final String[] fonts = {"uiicons.ttf", fn+"Black.tff", fn+"BlackItalic.tff", fn+"Bold.tff",
            fn+"BoldItalic.tff", fn+"ExtraLight.tff", fn+"Italic.tff", fn+"Light.tff", fn+"LightItalic.tff",
            fn+"Regular.tff", fn+"Semibold.tff", fn+"SemiboldItalic.tff"};

    public static Stage primaryStage;
    public static Parent dashboard;

    public static CTR_Dashboard ctr_dashboard;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("main app startet jetzt...");
        Main_Application.primaryStage = primaryStage;

        if(!File_Handler.fileExist("ver")){
            File_Handler.createDir("ver");
        }
        if(!File_Handler.fileExist("data")){
            File_Handler.createDir("data");
        }

        loadDashboard();

        primaryStage.setMinWidth(200);
        primaryStage.setMinHeight(150);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    for(CTR_Project_Module project : Manager.projectList) {
                        if(project.isRunning()) {
                            project.clockStop();
                        }
                    }
                    CSV_ProjectHandler.csvWriter();
                    System.out.println("Daten gespeichert");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        for(String font : fonts) {
            loadFonts(font);
        }

        //loadArchiv----------------------------------------------------------------------------------------------------
        if(!File_Handler.fileExist("data/archiv.csv")) {
            File_Handler.createFile("data/archiv.csv");
        }
        Archiv_Handler.loadArchiv();

        primaryStage.show();
    }

    public void loadDashboard() throws IOException {
        dashboard = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));
        primaryStage.setTitle(Launcher.appName);
        Scene dashboardScene = new Scene(dashboard, 1005, 720);
        dashboardScene.getStylesheets().add(getClass().getResource("/css/ui_view.css").toExternalForm());
        primaryStage.setScene(dashboardScene);
    }

    public void loadFonts(String file) {
        Font.loadFont(getClass().getResourceAsStream("/font/" + file), 10);
    }

    public static void setdashboardController(CTR_Dashboard ctr_dashboard) {
        Main_Application.ctr_dashboard = ctr_dashboard;
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public String getBuild() {
        return build;
    }
}
