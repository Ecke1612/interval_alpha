package gui.controller;

import handling.Manager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Main_Application;
import object.ClientStorageObject;
import object.Client_ObjectUI;

import java.util.ArrayList;

/**
 * Created by Eike on 10.06.2017.
 */
public class CTR_Client {

    public ArrayList<Client_ObjectUI> uiClients;
    private ScrollPane scrollPane = new ScrollPane();
    public GridPane gridPane;
    public FlowPane flowPane;
    private VBox mainVBox;

    public CTR_Client() {
    }

    public void initialize() {
        VBox vbox = new VBox();
        HBox header = new HBox();
        Button btn_newClient = new Button("Neuer Kunde");

        btn_newClient.setOnAction(event -> {
            NewClient newClient = new NewClient();
            newClient.createNewClient();
            initialize();
        });

        uiClients = new ArrayList<>();
        int i = 0;
        for(ClientStorageObject client : Manager.clients) {
            uiClients.add(new Client_ObjectUI(client.getName(), client.getColor(), i, this));
            i++;
        }

        flowPane = new FlowPane();

        flowPane.setPadding(new Insets(10, 10, 10,10));
        flowPane.setVgap(10);
        flowPane.setHgap(10);

        for(Client_ObjectUI client : uiClients) {
            flowPane.getChildren().add(client.createNode());
        }

        //scrollPane.setContent(flowPane);
        //Main_Application.ctr_dashboard.borderpane.setCenter(null);
        header.getChildren().addAll(btn_newClient);
        vbox.getChildren().addAll(header, flowPane);
        Main_Application.ctr_dashboard.borderpane.setCenter(vbox);
    }

}
