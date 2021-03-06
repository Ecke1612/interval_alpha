package object;

import gui.controller.CTR_Config;
import gui.controller.CTR_Dashboard;
import gui.controller.CTR_Project_Module;
import gui.controller.CTR_Report;
import handling.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main_Application;

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
    @FXML
    public VBox main_vbox;

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
        label_firstChar.setStyle("-fx-background-color: " + Manager.getHexColorString(client.getColor()) + ";" + Manager.getCSSTextColorByBrightness(client.getColor(),false));
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

        setTitlePaneWidth(Main_Application.primaryStage.getWidth());

        Main_Application.primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setTitlePaneWidth((double) newValue);
            }
        });

        setTitlepane();
    }

    private void setTitlePaneWidth(double value) {
        VBox vbox_center = (VBox) Main_Application.ctr_dashboard.borderpane.getChildren().get(1);
        double menu = (double)value - vbox_center.getWidth() + 50;
        main_vbox.setPrefWidth((double)value - menu);
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


    public void showDetails() {
        Stage stage = new Stage();
        VBox vBox = new VBox(5);
        vBox.setPadding(new Insets(10));
        Scene scene = new Scene(vBox);
        stage.setMaxWidth(600);
        stage.setMaxHeight(700);
        stage.setTitle("Übersicht " + name);
        stage.setScene(scene);

        VBox vboxData = new VBox(2);
        vboxData.setPadding(new Insets(5));
        ScrollPane scrollPane = new ScrollPane();
        for(StorageObject store : storageObjects) {
            Label label = new Label(store.getDate() + "  -  " + Manager.printTimeWithoutSec(store.getMin()*60) + "  -  " + store.getComment());
            label.setStyle("" +
                    "-fx-font-size: 14px;");
            vboxData.getChildren().add(label);
        }
        scrollPane.setContent(vboxData);

        VBox vbox_sum = new VBox(3);
        ArrayList<TimeProCommentObject> timeProCommentObjects = seperateTimeWithComments();
        for(TimeProCommentObject timepro : timeProCommentObjects) {
            Label label = new Label( Manager.printTimeWithoutSec(timepro.getTime()*60) + " - " + timepro.getComment());
            label.setStyle("" +
                    "-fx-font-size: 14px;");
            vbox_sum.getChildren().add(label);
        }

        Label label_sum = new Label("Summierte Aufteilungen der Zeiten:");

        Button btn_okay = new Button("Okay");
        btn_okay.setAlignment(Pos.CENTER_RIGHT);
        btn_okay.setOnAction(event -> {
            stage.close();
        });

        Button btn_export = new Button("Exportieren");
        btn_export.setAlignment(Pos.CENTER_RIGHT);
        btn_export.setOnAction(event -> {
            String path = Alert_Windows.saveFile();
            ArrayList<String> content = new ArrayList<>();
            content.add(client.getName() + " - " + name);
            content.add("");
            content.add("Kalkulierte Stunden: " + maxTime);
            content.add("Bearbeitet von: " + CTR_Config.configObject.getUsername());
            content.add("");
            content.add("");
            for(StorageObject store : storageObjects) {
                content.add(store.getDate() + "  -  " + Manager.printTimeWithoutSec(store.getMin()*60) + "  -  " + store.getComment());
            }
            content.add("");
            for(TimeProCommentObject timepro : timeProCommentObjects) {
                content.add(Manager.printTimeWithoutSec(timepro.getTime()*60) + " - " + timepro.getComment());
            }
            content.add("");
            String zero = "";
            if(maxTime < 10) zero = "0";
            content.add("Gesamt: " + Manager.printTimeWithoutSec(getWholeTime()) + " / " + zero + maxTime);
            try {
                File_Handler.fileWriterNewLine(path + ".txt", content);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.close();
        });

        HBox hbox = new HBox(5);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.getChildren().addAll(btn_export, btn_okay);

        vBox.getChildren().addAll(scrollPane, label_sum, vbox_sum, hbox);
        stage.show();
    }

    private ArrayList<TimeProCommentObject> seperateTimeWithComments() {
        ArrayList<TimeProCommentObject> timeProCommentObjects = new ArrayList<>();
        for(StorageObject store : storageObjects) {
            boolean hit = false;
            for(TimeProCommentObject timepro : timeProCommentObjects) {
                if(store.getComment().equals(timepro.getComment())) {
                    hit = true;
                    timepro.addTime(store.getMin());
                }
            }
            if(!hit){
                timeProCommentObjects.add(new TimeProCommentObject(store.getComment(), store.getMin()));
            }
        }
        return timeProCommentObjects;
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
