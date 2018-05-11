package object;

import gui.controller.CTR_Client;
import gui.controller.CTR_Config;
import gui.controller.CTR_Project_Module;
import handling.Alert_Windows;
import handling.CSV_ClientHandler;
import handling.CSV_ProjectHandler;
import handling.Manager;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * Created by Eike on 10.06.2017.
 */
public class Client_ObjectUI {

    private VBox mainVBox;
    private String name;
    private Color color;
    private int index;
    private CTR_Client ctr_client;
    Label labelName = new Label();

    public Client_ObjectUI(String name, Color color, int index, CTR_Client ctr_client) {
        this.name = name;
        this.color = color;
        this.index = index;
        this.ctr_client = ctr_client;
    }

    public VBox createNode() {
        mainVBox = new VBox();
        mainVBox.setStyle("-fx-background-color: " + Manager.getHexColorString(color) +";" +
        "-fx-padding: 20;");
        //mainVBox.getStylesheets().add(getClass().getResource(Manager.getCSSPath(CTR_Config.configObject.getCssIndex())).toExternalForm());
        mainVBox.getStyleClass().add("clientobject");

        Label labelactiveProjects = new Label();

        Button btn_rename = new Button("umbennen");
        btn_rename.getStyleClass().add("btns");
        btn_rename.setStyle(Manager.getCSSTextColorByBrightness(color,true));

        Button btn_delete = new Button(" entfernen ");
        btn_delete.getStyleClass().add("btns");
        btn_delete.setStyle(Manager.getCSSTextColorByBrightness(color,true));

        ColorPicker colorPicker = new ColorPicker();
        //colorPicker.getStyleClass().add(Manager.getCSSComboBoxColorByBrightness(color));
        if(color.getBrightness() < 0.81) {
            colorPicker.getStyleClass().add("colorpickerWhite");
        } else {
            colorPicker.getStyleClass().add("colorpickerBlack");
        }
        //colorPicker.setStyle(".arrow-button {-fx-background-color: rgb(0,0,0);}");

        colorPicker.setMaxWidth(100);


        labelName.setText(name);
        labelName.setStyle("-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                Manager.getCSSTextColorByBrightness(color,false));
        labelactiveProjects.setText(activeProjects() + " aktive Projekte");
        labelactiveProjects.setStyle(Manager.getCSSTextColorByBrightness(color,false));


        btn_rename.setOnAction(event -> {
            try {
                rename();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        btn_delete.setOnAction(event -> {
            try {
                delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        colorPicker.setOnAction(event -> {
            try {
                changeColor(colorPicker);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Fehler beim Farbe ändern");
            }
        });
        mainVBox.setSpacing(5);
        mainVBox.getChildren().addAll(labelName, labelactiveProjects, btn_rename, btn_delete, colorPicker);
        return mainVBox;
    }

    private int activeProjects() {
        int count = 0;
        for(CTR_Project_Module project : Manager.projectList) {
            if(project.getClient().getName().equals(name)){
             count++;
            }
        }
        return count;
    }

    private void rename() throws IOException {
        String newName = Alert_Windows.inputBox("Kunde umbennen", "Neuen Kundennamen vergeben", "Gib den neuen Namen für den Kunden ein");
        // Iterate Kundenliste und suche nach gleichem Namen - ersetze mit neuem
        for(int i=0; i<Manager.clients.size(); i++) {
            if(Manager.clients.get(i).getName().equals(name)){
                Manager.clients.set(i, (new ClientStorageObject(newName, Manager.clients.get(i).getColor())));
            }
            name = newName;
            labelName.setText(newName);
        }
        CSV_ClientHandler.csvWriter();
        //Iteratte Projektliste und sucht nach Kundenname und ersetzt diesen mit neuem + setzt Clientlabel entsprechend
        for(CTR_Project_Module project : Manager.projectList) {
            if(project.getClient().getName().equals(name)){
                project.getClient().setName(newName);
                project.label_client.setText(newName);
            }
        }
        CSV_ProjectHandler.csvWriter();
        name = newName;
    }

    private void delete() throws IOException {
        System.out.println(name + " gelöscht");
        Manager.clients.remove(index);
        ctr_client.flowPane.getChildren().remove(index);
        for(int i=0; i<ctr_client.uiClients.size(); i++) {
            ctr_client.uiClients.get(i).setIndex(i);
        }
        CSV_ClientHandler.csvWriter();
    }


    private void changeColor(ColorPicker colorPicker) throws IOException {
        System.out.println("Farbe geändert");
        Manager.clients.get(index).setColor(colorPicker.getValue());
        CSV_ClientHandler.csvWriter();
        ctr_client.initialize();
        for(CTR_Project_Module project : Manager.projectList) {
            project.initialize();
        }
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
