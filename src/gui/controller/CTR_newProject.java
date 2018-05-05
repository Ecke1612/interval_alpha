package gui.controller;

import handling.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import main.Main_Application;
import object.ClientStorageObject;
import object.StorageObject;

import java.time.LocalDate;

/**
 * Created by Eike on 22.05.2017.
 */
public class CTR_newProject {

    @FXML
    private ChoiceBox chb_customer;
    @FXML
    private Button btn_newCustomer;
    @FXML
    private TextField input_name;
    @FXML
    private TextField input_maxHours;
    @FXML
    private Label label_output;
    @FXML
    private Button btn_projPath;

    private String path = "";

    //private CSV_ClientHandler csv_clientHandler = new CSV_ClientHandler();
    //private CSV_ProjectHandler csv_projectHandler = new CSV_ProjectHandler();

    // ggf. ausprobieren über static main auf Dashboard zuzugreifen und den Controller nicht über Java definieren
    //CTR_Dashboard ctr_dashboard;

    public CTR_newProject() {
        //this.ctr_dashboard = ctr_dashboard;
    }

    public void initialize() {
        chb_customer.getItems().clear();
        for(ClientStorageObject client : Manager.clients) {
            chb_customer.getItems().add(client.getName());
        }
        chb_customer.getSelectionModel().selectLast();
    }

    public void openProjPath() {
        path = Alert_Windows.chooseDir();
    }

    public void save() {
        // ist ein Projektname vergeben?
        if(!input_name.getText().isEmpty()) {
            //ist mindestens ein Kunde angelegt
            if(Manager.clients.size() == 0) {
                System.out.println("lege bitte zunächst einen Kunden an");
                label_output.setText("Bitte lege zunächst einen Kunden an");
            //erstmal einen Kunden anlegen
            } else {
                Integer customerIndex = chb_customer.getSelectionModel().getSelectedIndex();
                //Ist ein Kunde ausgewählt worden
                if (customerIndex != null) {
                    //Ist in Feld ein Integerwert eingetragen
                    try {
                        int maxHours = Integer.parseInt(input_maxHours.getText());

                        //erstelle neues Projekt!!!
                        try {

                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/project_module.fxml"));
                            CTR_Project_Module project_module = new CTR_Project_Module(Manager.clients.get(customerIndex), input_name.getText(), maxHours, Manager.projectList.size(), path);
                            fxmlLoader.setController(project_module);
                            Manager.projectList.add(project_module);
                            project_module.getStorageObjects().add(new StorageObject(LocalDate.now(), 0, "Projekt angelegt"));

                            Parent project = fxmlLoader.load();
                            Main_Application.ctr_dashboard.vbox_projList.getChildren().add(project);
                            Manager.projectUIList.add(project);

                            CTR_Dashboard.stageNewProject.close();
                            CSV_ProjectHandler.csvWriter();

                        } catch (Exception e){
                            System.out.println("Das Projekt konnte nicht erstellt werden " + e);
                            label_output.setText("Das Projekt konnte nicht erstellt werden");
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        System.out.println("unzulässiger Wert in Maximale Stunden");
                        label_output.setText("Unzulässiger Wert für maximale Stunden");
                    }

                } else {
                    System.out.println("Wähle bitte einen Kunden aus");
                    label_output.setText("Bitte wähle einen Kunden aus");
                }
            }
        } else {
            System.out.println("Gibt deinem Projekt bitte einen Namen");
            label_output.setText("Bitte gib deinem Projekt einen Namen");
        }
    }

    public void newClient() {
        NewClient newClient = new NewClient();
        newClient.createNewClient();
        initialize();
    }

    public void abort(){
        Main_Application.ctr_dashboard.stageNewProject.close();
    }


}
