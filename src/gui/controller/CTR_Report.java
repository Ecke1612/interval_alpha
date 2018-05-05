package gui.controller;

import handling.Archiv_Handler;
import handling.Manager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.Main_Application;
import object.Report_Object;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Eike on 11.06.2017.
 */
public class CTR_Report {

    private DatePicker datePicker = new DatePicker(LocalDate.now());
    private ArrayList<Report_Object> reports;
    private ScrollPane scrollPane = new ScrollPane();
    private VBox mainVBox;
    private HBox headerHBox;
    private VBox entryVBox;
    private Label label_dayTime;
    private Button btn_plusDay;
    private Button btn_minusDay;
    private ToggleButton expandButton;

    public CTR_Report() {

    }

    public void initialize() {
        mainVBox = new VBox();
        headerHBox = new HBox();
        entryVBox = new VBox();
        label_dayTime = new Label();
        btn_minusDay = new Button("<");
        btn_plusDay = new Button(">");
        expandButton = new ToggleButton("alle ausklappen");
        label_dayTime.setStyle("-fx-font-size: 18px;" +
                "-f-font-weight: bold;");
        headerHBox.setSpacing(5);
        headerHBox.getChildren().addAll(btn_minusDay, datePicker, btn_plusDay, expandButton);
        mainVBox.getChildren().addAll(headerHBox, entryVBox, label_dayTime);

        btn_minusDay.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: rgba(98, 124, 140, 1);"
        );

        btn_minusDay.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            date = date.minusDays(1);
            datePicker.setValue(date);
        });

        btn_plusDay.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            date = date.plusDays(1);
            datePicker.setValue(date);
        });

        datePicker.setOnAction(event -> {
            LocalDate date = datePicker.getValue();
            //datePicker2.setValue(date);
            updateView(date);
        });

        expandButton.setOnAction(event -> {
            expandAll(expandButton);
        });

        updateView(datePicker.getValue());

        scrollPane.setContent(mainVBox);
        Main_Application.ctr_dashboard.borderpane.setCenter(scrollPane);
    }

    private void updateView(LocalDate date) {
        int dayTimeSeconds = 0;
        reports = new ArrayList<>();
        for(CTR_Project_Module project : Manager.projectList) {
            if(project.getAllDates().contains(date)){
                reports.add(new Report_Object(project.getName(), project.getClient(), project.getMaxTimeHours(), project.getStorageObjects(), reports.size(), this));
                dayTimeSeconds += project.getSecondsByDate(date);
            }
        }

        for(Report_Object report : Archiv_Handler.archivObjects) {
            if(report.getAllDates().contains(date)){
                reports.add(report);
                report.setCtr_report(this);
                dayTimeSeconds += report.getSecondsByDate(date);
            }
        }
        label_dayTime.setText("Du hast heute " + String.valueOf(Manager.printTimeWithoutSec(dayTimeSeconds)) + " gearbeitet.");

        try {
            createUI(reports, date);
        } catch (IOException e) {
            e.printStackTrace();
        }
        expandButton.setSelected(false);
    }

    private void updatePeriodView(LocalDate date1, LocalDate date2) {

    }

    private void createUI(ArrayList<Report_Object> reports, LocalDate date) throws IOException {
        entryVBox.getChildren().clear();
        for(Report_Object report : reports) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/report_module.fxml"));
            report.setDate(date);
            fxmlLoader.setController(report);
            Parent reportUI = fxmlLoader.load();
            //Manager.projectUIList.add(projectUI);
            entryVBox.getChildren().add(reportUI);
        }
    }

    private void expandAll(ToggleButton btn) {

        for(Report_Object report : reports) {
            report.titlepane.setExpanded(btn.isSelected());
        }
    }

}
