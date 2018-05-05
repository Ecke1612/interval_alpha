package main.launcher;

import gui.controller.CTR_Config;
import handling.File_Handler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Main_Application;
import updater.Updater_Main;

public class Launcher extends Application {

    private Main_Application mainApp = new Main_Application();
    private Updater_Main updater = new Updater_Main();
    public static final String appName = "Interval";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent startScreen = FXMLLoader.load(getClass().getResource("/fxml/startScreen.fxml"));
        Scene scene = new Scene(startScreen);
        primaryStage.setScene(scene);
        primaryStage.setTitle(appName +" laden...");

        primaryStage.show();

        new Thread(() -> Platform.runLater(() -> {
            CTR_Config ctr_config = new CTR_Config();
            if(ctr_config.getConfigObject().isDoUpdate()) {
                try {
                    if(!File_Handler.fileExist("ver")){
                        File_Handler.createDir("ver");
                    }
                    if(!File_Handler.fileExist("data")){
                        File_Handler.createDir("data");
                    }

                    update(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    mainApp.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                primaryStage.close();
            }
        })).start();

    }


    public void update(Stage primaryStage) throws Exception {
        boolean update = updater.start(mainApp.getBuild());
        if(update) {
            Runtime runTime = Runtime.getRuntime();
            runTime.exec("java -jar " + appName + ".jar");
            primaryStage.close();
        } else {
            mainApp.start(new Stage());
            primaryStage.close();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
