package object;

import gui.controller.CTR_Dashboard;
import gui.controller.CTR_Project_Module;
import gui.controller.CTR_Report;
import handling.Archiv_Handler;
import handling.CSV_ProjectHandler;
import handling.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static main.Main_Application.ctr_dashboard;

/**
 * Created by Eike on 11.06.2017.
 */
public class Report_Object {
    @FXML
    private Label label_projName;
    @FXML
    private Label label_client;
    @FXML
    private Label label_time;
    @FXML
    private Label label_firstChar;
    @FXML
    private VBox vbox_detail;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Label label_wholeTime;
    @FXML
    public TitledPane titlepane;
    @FXML
    public MenuItem menu_reopen;

    private String name;
    private ClientStorageObject client;
    private int index, maxTime;
    private LocalDate date = LocalDate.now();
    private ArrayList<StorageObject> storageObjects = new ArrayList<>();
    private CTR_Report ctr_report;
    private boolean closed = false;

    public Report_Object(String name, ClientStorageObject client, int maxTime, ArrayList<StorageObject> storageObjects, int index, CTR_Report ctr_report) {
        this.name = name;
        this.client = client;
        this.maxTime = maxTime;
        this.storageObjects = storageObjects;
        this.index = index;
        this.ctr_report = ctr_report;
    }

    public Report_Object(String name, ClientStorageObject client, int maxTime, int index) {
        this.name = name;
        this.client = client;
        this.maxTime = maxTime;
        this.index = index;
        this.ctr_report = ctr_report;
    }

    public void initialize() {
        label_projName.setText(name);
        label_client.setText(client.getName());
        label_time.setText(Manager.printTimeWithoutSec(timeOfDay()));
        char p = client.getName().charAt(0);
        char pUpper = Character.toUpperCase(p);
        label_firstChar.setText(String.valueOf(pUpper));
        label_firstChar.setStyle("-fx-background-color: " + Manager.getHexColorString(client.getColor()) + ";" + Manager.getCSSTextColorByBrightness(client.getColor()));
        menu_reopen.setDisable(true);
        if(closed) {
            label_firstChar.setStyle("-fx-background-color: rgb(180,180,180);");
            menu_reopen.setDisable(false);
        }
        if(maxTime == 0) {
            progress.setVisible(false);
            label_wholeTime.setText(Manager.printTimeWithoutSec(getWholeTime()));
        } else {
            String zero = "";
            if(maxTime < 10) zero = "0";
            label_wholeTime.setText(Manager.printTimeWithoutSec(getWholeTime()) + " / " + zero + maxTime);
            double percent = ((double)getWholeTime() / (double)((maxTime) * 3600));
            System.out.println("prozent: " + getWholeTime() + " / " + (maxTime) + " = " + percent);
            progress.setProgress(percent);

        }
        setTitlepane();
    }

    private void setTitlepane() {
        vbox_detail.getChildren().clear();
        for(StorageObject store : storageObjects) {
            if(date.equals(store.getDate())){
                Label label = new Label();
                label.setText(Manager.printTime(store.getSec()) + "   -   " + store.getComment());
                vbox_detail.getChildren().add(label);
            }
        }
    }

    public void addStorageObject(StorageObject store) {
        storageObjects.add(store);
    }

    public ArrayList<LocalDate> getAllDates() {
        ArrayList<LocalDate> dates = new ArrayList<>();
        for(StorageObject store : storageObjects) {
            dates.add(store.getDate());
        }
        return dates;
    }

    private int timeOfDay() {
        int time = 0;
        for(StorageObject store : storageObjects) {
            if(date.equals(store.getDate())) {
                time += store.getSec();
            }
        }
        return time;
    }

    private int getWholeTime() {
        int time = 0;
        for(StorageObject store : storageObjects) {
            time += store.getSec();
        }
        return time;
    }

    public int getSecondsByDate(LocalDate date) {
        int time = 0;
        for(StorageObject store : storageObjects) {
            if(date.equals(store.getDate())) {
                time += store.getSec();
            }
        }
        return time;
    }

    public void reopen() throws IOException {

        //Erstelle ein neues Projekt mit den Report-Daten--------------------------------------------------------
        if(closed) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/project_module.fxml"));
            CTR_Project_Module project_module = new CTR_Project_Module(client, name, maxTime, Manager.projectList.size(), "");
            project_module.setStorageObjects(storageObjects);
            fxmlLoader.setController(project_module);
            Manager.projectList.add(project_module);

            Parent project = fxmlLoader.load();
            ctr_dashboard.vbox_projList.getChildren().add(project);
            Manager.projectUIList.add(project);

            //CTR_Dashboard.stageNewProject.close();
            CSV_ProjectHandler.csvWriter();
            //---------------------------------------------------------------------------------------------------------
            //Lösche dieses Objekt aus dem Archiv-------------------------------------------------------------------
            Archiv_Handler.getArchivObjects().remove(index);
            Archiv_Handler.writeWholeArchiv();
            Archiv_Handler.loadArchiv();
            if(ctr_report != null) {
                ctr_report.initialize();
            } else {
                System.out.println("Konnte UI nicht aktualisieren");
            }

        } else {
            System.out.println("Projekt ist bereits aktiv");
        }
    }

    public void setClosed(boolean value) {
        closed = value;
    }

    public void setCtr_report(CTR_Report ctr_report) {
        this.ctr_report = ctr_report;
    }

    public String getName() {
        return name;
    }

    public ClientStorageObject getClient() {
        return client;
    }

    public int getIndex() {
        return index;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public ArrayList<StorageObject> getStorageObjects() {
        return storageObjects;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
