package gui.controller;

import handling.CSV_ClientHandler;
import handling.Manager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import object.ClientStorageObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Eike on 01.07.2017.
 */
public class NewClient {

    public void createNewClient() {
        //String client = Alert_Windows.inputBox("Neuer Kunde", "Lege einen neuen Kunden an", "Bitte gib den Namen des Kunden an");

        Stage newClient = new Stage();
        VBox vbox = new VBox();
        Scene scene = new Scene(vbox);

        HBox hboxEntry = new HBox();
        HBox hboxButton = new HBox();

        hboxEntry.setPadding(new Insets(10));
        hboxButton.setPadding(new Insets(10));

        Label title = new Label("Kunde anlegen");
        TextField nameField = new TextField();
        ColorPicker colorPicker = new ColorPicker();

        Button btn_save = new Button("Speichern");
        Button btn_abort = new Button("Abbrechen");

        btn_save.setPadding(new Insets(5));
        btn_abort.setPadding(new Insets(5));

        newClient.setTitle("Kunden anlegen");
        newClient.setScene(scene);

        hboxEntry.getChildren().addAll(nameField, colorPicker);
        hboxButton.getChildren().addAll(btn_abort, btn_save);
        vbox.getChildren().addAll(title, hboxEntry, hboxButton);

        btn_abort.setOnAction(event -> {
            newClient.close();
        });

        btn_save.setOnAction(event -> {
            System.out.println("Speicherbutton");
            String name = nameField.getText();

            if(name.isEmpty()) {
                System.out.println("Kunde konnte nicht erstellt werden, da kein Name vergeben wurde");
            } else if(duplicateClient(name)){
                System.out.println("Der Kunde existiert bereits");
            } else {
                //Manager.clients.add(new ClientStorageObject(name, color_manager.getColors().get(colorChooser.getSelectionModel().getSelectedIndex()).getColor()));
                Manager.clients.add(new ClientStorageObject(name, colorPicker.getValue()));
                try {
                    CSV_ClientHandler.csvWriter();
                    System.out.println("Clients gespeichert");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newClient.close();
                //speicher Clientdaten in Datei
                try {
                    CSV_ClientHandler.csvWriter();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        newClient.showAndWait();
    }

    private boolean duplicateClient(String newName) {
        ArrayList<String> name = new ArrayList<>();
        for(ClientStorageObject client : Manager.clients) {
            name.add(client.getName());
        }
        if(name.contains(newName)) return true;
        else return false;
    }

}
