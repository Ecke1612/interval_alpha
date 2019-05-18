package handling;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import gui.controller.CTR_Project_Module;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import main.Main_Application;
import object.ClientStorageObject;
import object.StorageObject;
import object.TodoStorage;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Eike on 28.05.2017.
 */


public class CSV_ProjectHandler {

    //schreibt die CSV Datei indem alle StorageOBjekte aus jedem Projekt iteriert und in eine Zeile geschrieben werden
    public static void csvWriter() throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter("data/trackingData.csv"), ';');
        for(CTR_Project_Module projects : Manager.projectList) {
            String projPath = projects.getProjectpath().replace("\\", "/");
            String header = "1" + ";" + projects.getClient().getName() + ";" + projects.getName() + ";" + projects.getMaxTimeHours() + ";" + projPath;
            String[] headerEntries = header.split(";");
            writer.writeNext(headerEntries);
            for(StorageObject storage : projects.getStorageObjects()) {
                String temp = "0" + ";" + storage.getDate() + ";" + storage.getSec() + ";" + storage.getComment();
                        String[] entries = temp.split(";");
                writer.writeNext(entries);
            }
            for(TodoStorage todo : projects.getTodos()) {
                if(todo.getType().equals("note")) {
                    String temp = "2;" + todo.getNotes() + ";" + todo.getRowCount();
                    String[] entries = temp.split(";");
                    writer.writeNext(entries);
                } else if(todo.getType().equals("todo")) {
                    String temp = "3;" + todo.getText() + ";" + Boolean.toString(todo.isCheck());
                    String[] entries = temp.split(";");
                    writer.writeNext(entries);
                }
            }
        }
        writer.close();
    }

    public void csvLoader() throws IOException {
        Manager.projectList.clear();
        CSVReader reader = new CSVReader(new FileReader("data/trackingData.csv"), ';');
        List<String[]> data = reader.readAll();

        for(String[] line : data) {
            int header = 0;
            try {
                header = Integer.parseInt(line[0]);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("header Fehlerhaft");
            }
            if(header == 1) {
                try {
                    //suche das richtige Kundenobjekt:
                    ClientStorageObject clientObject = new ClientStorageObject("Kein Kunde", new Color(0.3,0.3,0.3,0));
                    for(ClientStorageObject client : Manager.clients) {
                        if(client.getName().equals(line[1])) {
                            clientObject = client;
                        }
                    }
                    String projpath = "";
                    try {
                        projpath = line[4];
                    } catch (Exception e){
                        System.out.println("kein pfad vergeben");
                    }
                    //
                    Manager.projectList.add(new CTR_Project_Module(clientObject, line[2], Integer.parseInt(line[3]), Manager.projectList.size(), projpath));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Projekt konnte nicht erstellt werden");
                }
            } else if(header == 0) {
                try {
                    Manager.projectList.get(Manager.projectList.size() - 1).getStorageObjects().add((new StorageObject(LocalDate.parse(line[1]), Integer.parseInt(line[2]), line[3])));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Fehler beim erstellen der StorageObjekte");
                }
            } else if(header == 2) {
                try {
                    Manager.projectList.get(Manager.projectList.size() - 1).getTodos().add(new TodoStorage("note", line[1], Integer.parseInt(line[2])));
                }catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Fehler beim erstellen der Todos note");
                }
            } else if(header == 3) {
                try {
                    Manager.projectList.get(Manager.projectList.size() - 1).getTodos().add(new TodoStorage("todo", Boolean.parseBoolean(line[2]), line[1]));
                }catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Fehler beim erstellen der Todos todo");
                }
            }else System.out.println("unbekannter Fehler beim Laden der Projekte");
        }
        createProjects();
    }


    //Schritt 3: Die erstellten Projekte als FXML Element im Dashboard platzieren und mit dem enstprechenden CTR_StartScreen
    // aus der Projektliste verbinden
    private void createProjects() throws IOException {
        Main_Application.ctr_dashboard.vbox_projList.getChildren().clear();
        for(CTR_Project_Module project : Manager.projectList) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/project_module.fxml"));
            fxmlLoader.setController(project);

            Parent projectUI = fxmlLoader.load();
            Manager.projectUIList.add(projectUI);
            Main_Application.ctr_dashboard.vbox_projList.getChildren().add(projectUI);
        }
    }

    public boolean fileExist(String path) {
        File file = new File(path);
        if(file.exists()) {
            return true;
        }
        return false;
    }

    public void deleteFile(String fileName){
        File f = new File(fileName);
        if(f.exists()){
            f.delete();
        }
    }

}
