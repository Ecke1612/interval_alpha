package gui.controller;

import gui.controller.project_module_objects.Project_Todos;
import handling.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main_Application;
import object.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by Eike on 20.05.2017.
 */
public class CTR_Project_Module {

    @FXML
    public Label label_time;
    @FXML
    Label label_projName;
    @FXML
    private ToggleButton btn_timer;
    @FXML
    public Label label_client;
    @FXML
    public Label label_time_today;
    @FXML
    private TextField textArea_comment;
    @FXML
    private Label label_firstChar;
    @FXML
    private VBox vbox_detail;
    @FXML
    TitledPane titledPane;
    @FXML
    public MenuItem menu_goToDir;
    @FXML
    ImageView image_reminder;
    @FXML
    Button btn_folder;
    @FXML
    public Button btn_addTodo;
    @FXML
    public Button btn_addNote;
    @FXML
    public VBox vbox_todos;
    @FXML
    public VBox vbox_todo_parent;

    private String name;
    private ClientStorageObject client;
    private int mainSec = 0;
    private int timeToday = 0;
    private int newSec = 0;
    private int index;
    private LocalDate date = LocalDate.now();
    private int maxTimeHours = 0;
    private boolean running = false;
    private Timeline mainTime = new Timeline();
    public Timeline autoStopTimeline = new Timeline();
    private String projectpath = "";
    public int autoStopOffset = 0;
    private ReminderObject reminderObject;
    private ArrayList<TodoStorage> todos = new ArrayList<>();
    private Project_Todos project_todos;

    private ArrayList<StorageObject> storageObjects = new ArrayList<>();

    public CTR_Project_Module(ClientStorageObject client, String name, int maxTimeHours, int index, String projectpath) {
        this.name = name;
        this.client = client;
        this.maxTimeHours = maxTimeHours;
        this.index = index;
        this.projectpath = projectpath;
    }

    public void initialize() {
        label_projName.setText(name);
        label_client.setText(client.getName());
        if(projectpath.equals("")) btn_folder.setVisible(false);
        else btn_folder.setVisible(true);
        char p = client.getName().charAt(0);
        char pUpper = Character.toUpperCase(p);

        label_firstChar.setText(String.valueOf(pUpper));
        label_firstChar.setStyle("-fx-background-color: " + Manager.getHexColorString(client.getColor()) + ";" + Manager.getCSSTextColorByBrightness(client.getColor(),false));
        initTrackingData();
        //wenn StorageObjekte vorhanden sind, errechne die gesamtzeit daraus
        if(storageObjects.size() != 0) {
            getWholeTime();
            getTimeToday();
            textArea_comment.setText(storageObjects.get(storageObjects.size()-1).getComment());
        }
        project_todos = new Project_Todos(vbox_todos, this);
        for(TodoStorage todoStorage : todos) {
            if(todoStorage.getType().equals("note")){
                executeAddNote(todoStorage.getNotes(), todoStorage.getRowCount());
            }else if(todoStorage.getType().equals("todo")) {
                executeAddTodo(todoStorage.getText(), todoStorage.isCheck());
            }
        }
        //Wenn heute noch keine Zeit getrackt wurde, dann wird die Zeit nur schwach angezeigt
        if(timeToday == 0) {
            //titledPane.setStyle("-fx-background-color: rgb(48, 148, 44);");
            label_time_today.setStyle("-fx-text-fill: rgb(190, 190, 190);");
            label_time.setStyle("-fx-text-fill: rgb(190, 190, 190);");
            btn_timer.setStyle("-fx-text-fill: rgb(190, 190, 190);");
        }

        //Wenn Heute getrackte Zeit mit Gesamtzeit übereinstimmt, braucht gesamtzeit nicht angezeigt zu werden
        if(timeToday == mainSec) {
            label_time.setVisible(false);
            label_time.setManaged(false);
        }
        label_time.setText(Manager.printTime(mainSec));
        label_time_today.setText(Manager.printTime(timeToday));
        if(projectpath.equals("")) {
            menu_goToDir.setDisable(true);
        }

    }


    public void initTrackingData() {
        vbox_detail.getChildren().clear();
        for(StorageObject store : storageObjects) {
            if(LocalDate.now().equals(store.getDate())){
                Label label = new Label();
                label.setText(Manager.printTime(store.getSec()) + "   -   " + store.getComment());
                vbox_detail.getChildren().add(label);;
            }
        }
    }

    public void startClock() throws IOException {
        //Uhr wird gestoppt
        if(running) {
            clockStop();
            autoStopTimeline.stop();
        }
        //Uhr wird gestartet
        else {
            //überprüfen ob andere Uhre noch laufen und diese stoppen
            if(!CTR_Config.configObject.isMultiClock()) {
                for (CTR_Project_Module projects : Manager.projectList) {
                    if (projects.running) {
                        projects.clockStop();
                    }
                }
            }
            newSec = 0;
            mainTime.setCycleCount(Timeline.INDEFINITE);

            KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {
                mainSec++;
                newSec++;
                timeToday++;
                label_time.setText(Manager.printTime(mainSec));
                label_time_today.setText(Manager.printTime(timeToday));
            });
            mainTime.getKeyFrames().add(frame);
            mainTime.play();

            running = true;
            btn_timer.setText("\uF00E");
            btn_timer.setStyle("-fx-text-fill: rgb(65, 63, 84);");
            //Zeiten werden farblich kräftiger, für den Fall das sie vorher 0 und somit schwach waren
            label_time_today.setStyle("-fx-text-fill: rgb(0, 0, 0);");
            label_time.setStyle("-fx-text-fill: rgb(0, 0, 0);");

            //AutoDetectionStop---------------------------------------------------------------------------------
            if(CTR_Config.configObject.isAutostop()) {
                autoStopTimeline.setCycleCount(Timeline.INDEFINITE);
                KeyFrame autoStopFrame = new KeyFrame(Duration.seconds(CTR_Config.configObject.getAutostopinterval()*60), event -> {
                    System.out.println("check for activity");
                    checkAutoStop();
                });
                autoStopTimeline.getKeyFrames().add(autoStopFrame);
                autoStopTimeline.play();
            }
        }
    }

    public void clockStop() throws IOException {
        mainTime.stop();
        running = false;
        mainTime.getKeyFrames().clear();
        autoStopTimeline.getKeyFrames().clear();
        autoStopTimeline.stop();
        autoStopOffset = 0;
        saveData();
        initTrackingData();

        CSV_ProjectHandler.csvWriter();
        btn_timer.setText("\uF00F");
        btn_timer.setStyle("-fx-text-fill: rgb(235,127,0)");
    }

    //Speichert die getrackted Zeit entsprechend in einem StorageObjekt
    private void saveData() {
        StorageObject lastObj = storageObjects.get(storageObjects.size() - 1);
        String comment = textArea_comment.getText();

        //wenn noch kein Eintrag vorhanden ist, lege einen an
        if(storageObjects.size() == 0){
            storageObjects.add(new StorageObject(LocalDate.now(), newSec, comment));
            System.out.println("erster Eintrag");
        }
        //Wenn Datum ungleich zum letzten Eintrag ist, einen neuen Anlegen
        else if (!lastObj.getDate().equals(LocalDate.now())) {
            storageObjects.add(new StorageObject(LocalDate.now(), newSec, comment));
            System.out.println("Datum ungleich");
        }
        //Wenn das Datum gleich ist, aber der Kommentar unterschiedlich, lege auch einen neuen Eintrag an, aber keine neue Gruppe
        else if(lastObj.getDate().equals(LocalDate.now()) && !lastObj.getComment().equals(comment)) {
            storageObjects.add(new StorageObject(LocalDate.now(), newSec, comment));
            System.out.println("Datum gleich aber Kommentar ungleich");
        }
        // Wenn Datum als auch Kommentar gleich sind keinen neuen Eintrag sondern Zeit addieren
        else {
            lastObj.setSeconds(lastObj.getSec() + newSec);
            System.out.println("Zeit hinzugefügt");
        }
    }

    //Iteriert durch alle StorageObjekte und addiert die Zeit
    private void getWholeTime() {
        mainSec = 0;
        for(StorageObject store : storageObjects) {
            if(store.getComment().equals("Zeit auf Null gesetzt")) {
                mainSec = 0;
                System.out.println("mainsec auf null gesetzt");
            }
            mainSec = mainSec + store.getSec();
        }
    }

    private int getTimeToday() {
        timeToday = 0;
        for(StorageObject store : storageObjects) {
            if(store.getComment().equals("Zeit auf Null gesetzt")) {
                timeToday = 0;
            } else if(store.getDate().equals(LocalDate.now())) {
                timeToday += store.getSec();
            }
        }
        return timeToday;
    }

    public void deleteProject() throws IOException {
        if(Alert_Windows.confirmDialog("Projekt löschen", "Das Projekt wird gelöscht", "Möchtest du das Projekt wirklich löschen?")) {
            Manager.projectList.remove(index);
            Manager.projectUIList.remove(index);
            Main_Application.ctr_dashboard.removeProject(index);
            for(int i=0; i<Manager.projectList.size(); i++) {
                Manager.projectList.get(i).setIndex(i);
            }
            CSV_ProjectHandler.csvWriter();
        }
    }

    public void resetTime() {
        if(Alert_Windows.confirmDialog("Zeit auf Null setzen", "Die Gesamtzeit auf Null zurücksetzen", "Möchtest du das wirklich die Gesamtzeit auf Null setzen?")) {
            mainSec = 0;
            timeToday = 0;
            addStorageObject(new StorageObject(date, mainSec,"Zeit auf Null gesetzt"));
            label_time.setText(Manager.printTime(mainSec));
            label_time_today.setText(Manager.printTime(timeToday));
        }
    }

    public void goToDir() {
        if(projectpath.equals("")) {
            System.out.println("kein Pfad gewählt");
        } else {
            try {
                Desktop.getDesktop().open(new File(projectpath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void toArchiv() throws IOException {
        if(Alert_Windows.confirmDialog("Projekt abschließen", "Das Projekt wird in das Archiv verschoben", "Möchtest du das Projekt wirklich abschließen?")) {
            Archiv_Handler.writeToArchiv(this);
            Manager.projectList.remove(index);
            Manager.projectUIList.remove(index);
            Main_Application.ctr_dashboard.removeProject(index);
            for(int i=0; i<Manager.projectList.size(); i++) {
                Manager.projectList.get(i).setIndex(i);
            }
            CSV_ProjectHandler.csvWriter();
            Archiv_Handler.loadArchiv();
        }
    }

    public void showProjectEdit() throws IOException {
        Stage stageProjectEdit = new Stage();
        stageProjectEdit.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/project_edit.fxml"));
        CTR_Project_Edit ctr_project_edit = new CTR_Project_Edit(this, stageProjectEdit);
        fxmlLoader.setController(ctr_project_edit);
        Parent projectEdit = fxmlLoader.load();

        Scene projectEditScene = new Scene(projectEdit);

        stageProjectEdit.setTitle("Projekt bearbeiten");
        stageProjectEdit.initOwner(Main_Application.primaryStage);
        stageProjectEdit.setScene(projectEditScene);
        stageProjectEdit.showAndWait();
    }

    public ArrayList<LocalDate> getAllDates() {
        ArrayList<LocalDate> dates = new ArrayList<>();
        for(StorageObject store : storageObjects) {
            dates.add(store.getDate());
        }
        return dates;
    }

    private void checkAutoStop() {
        int minTime = CTR_Config.configObject.getAutostopMinTime()*3600;
        int rushhour = CTR_Config.configObject.getAutostopRushHour();
        int offset = newSec - autoStopOffset;
        System.out.println("newSec: " + newSec + "; autostopoffset: " + autoStopOffset + " = " + offset);

        //Wenn Die Uhr mehr als ... Minuten läuft UND es nach 18 Uhr ist
        if((offset >= minTime) && (LocalTime.now().isAfter(LocalTime.of(rushhour,0)))) {
            autoStopTimeline.stop();
            AutoStopAlert autoStopAlert = new AutoStopAlert(newSec, this);
        }

    }

    public void reminder() {
        Stage remindStage = new Stage();
        remindStage.setTitle("Erinnerung einfügen");
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10));
        Scene scene = new Scene(vBox);

        HBox hbox1 = new HBox(5);

        HBox hBox2 = new HBox(5);

        HBox hbox3 = new HBox(5);

        Label text = new Label("Füge eine Erinnerung für den für heute hinzu.");
        Label text3 = new Label("");

        Button btn_delete = new Button("löschen");
        btn_delete.setOnAction(event -> {
            deleteReminder();
            reminder();
            remindStage.close();
        });

        if(reminderObject != null) {
            text3 = new Label("Erinnerung bereits gesetzt: " +reminderObject.getHour() + ":" + reminderObject.getMin());

        }

        TextField textField = new TextField();

        ComboBox boxHours = new ComboBox();
        boxHours.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24");

        ComboBox boxMin = new ComboBox();
        boxMin.getItems().addAll("0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30",
                "31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59");

        try {
            boxHours.getSelectionModel().select(LocalTime.now().getHour() - 1);
            boxMin.getSelectionModel().select(LocalTime.now().getMinute());
        }catch (Exception e) {
            boxHours.getSelectionModel().select(7);
            boxMin.getSelectionModel().selectFirst();
        }

        hBox2.getChildren().addAll(textField, boxHours, boxMin);

        Button btn_abort = new Button("Abbrechen");
        btn_abort.setOnAction(event -> {
            remindStage.close();
        });

        Button btn_save = new Button("Speichern");
        btn_save.setOnAction(event -> {
            createReminder(textField.getText(), boxHours.getSelectionModel().getSelectedIndex()+1, boxMin.getSelectionModel().getSelectedIndex());
            remindStage.close();
        });

        if(reminderObject == null) hbox3.getChildren().addAll(btn_abort, btn_save);
        else hbox3.getChildren().addAll(btn_delete,btn_abort, btn_save);

        vBox.getChildren().addAll(text, text3, hbox1,hBox2, hbox3);
        remindStage.setScene(scene);
        remindStage.showAndWait();

    }

    private void createReminder(String text, int hour, int min) {
        image_reminder.setVisible(true);
        reminderObject = new ReminderObject(text, hour, min);
    }

    public void deleteReminder() {
        image_reminder.setVisible(false);
        reminderObject = null;
    }

    public void add_todo() {
        executeAddTodo("", false);
    }

    public void executeAddTodo(String text, boolean checked) {
        project_todos.executeAddTodo(text, checked);
    }

    public void add_noteFXML() {
        executeAddNote("", 1);
    }

    private void executeAddNote(String text, int rowCount) {
        project_todos.executeAddNote(text, rowCount);
    }

    public void saveTodos() {
        project_todos.saveTodos();
    }


    public ArrayList<TodoStorage> getTodos() {
        return todos;
    }

    public void setTodos(ArrayList<TodoStorage> todos) {
        this.todos = todos;
    }

    public ReminderObject getReminderObject() {
        return reminderObject;
    }

    public String getName() { return name; }

    public String getClientName() { return client.getName(); }

    public ClientStorageObject getClient() {
        return client;
    }

    public int getMainSec() {
        return mainSec;
    }

    public int getNewSec() { return newSec; }

    public void setNewSec(int newSec) {
        this.newSec = newSec;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMaxTimeHours() {
        return maxTimeHours;
    }

    public void addStorageObject(StorageObject storageObject) {
        storageObjects.add(storageObject);
        initTrackingData();
    }

    public int getSecondsByDate(LocalDate date) {
        int time = 0;
        for(StorageObject store : storageObjects) {
            if(store.getDate().equals(date)) {
                time += store.getSec();
            }
        }
        return time;
    }

    public boolean isRunning() {
        return running;
    }

    public ArrayList<StorageObject> getStorageObjects() {
        return storageObjects;
    }

    public void setStorageObjects(ArrayList<StorageObject> storageObjects) {
        this.storageObjects = storageObjects;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClient(ClientStorageObject client) {
        this.client = client;
    }

    public void setMainSec(int mainSec) {
        this.mainSec = mainSec;
    }

    public void setMaxTimeHours(int maxTimeHours) {
        this.maxTimeHours = maxTimeHours;
    }

    public String getProjectpath() {
        return projectpath;
    }

    public void setProjectpath(String projectpath) {
        this.projectpath = projectpath;
    }
}
