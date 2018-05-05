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

import java.io.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Eike on 28.05.2017.
 */



public class CSV_ProjectHandler {

    //schreibt die CSV Datei indem alle StorageOBjekte aus jedem Projekt iteriert und in eine Zeile geschrieben werden
    public static void csvWriter() throws IOException{
        CSVWriter writer = new CSVWriter(new FileWriter("data/trackingData.csv"), ';');

        for(CTR_Project_Module projects : Manager.projectList) {
            String header = "1" + ";" + projects.getClient().getName() + ";" + projects.getName() + ";" + projects.getMaxTimeHours() + ";" + projects.getProjectpath();
            String[] headerEntries = header.split(";");
            writer.writeNext(headerEntries);
            for(StorageObject storage : projects.getStorageObjects()) {
                String temp = "0" + ";" + storage.getDate() + ";" + storage.getSec() + ";" + storage.getComment();
                        String[] entries = temp.split(";");
                writer.writeNext(entries);
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
            } else System.out.println("unbekannter Fehler beim Laden der Projekte");
        }
        createProjects();
    }

    /*
    //Schritt 1: Die CSV Datei von der Festplatte in eine umfassende StorageObektliste überführen
    public void csvLoader1() throws IOException {
        ArrayList<StorageObject> storageObjects = new ArrayList<>();
        CSVReader reader = new CSVReader(new FileReader("data/trackingData.csv"), ';');

        List<String[]> data = reader.readAll();

        for(String[] row : data) {
            String name = row[2];
            int sec = Integer.parseInt(row[3]);
            LocalDate date = LocalDate.parse(row[0]);
            String client = row[1];
            int maxTime = Integer.parseInt(row[4]);
            String comment = row[5];
            storageObjects.add(new StorageObject(name, sec, date, client, maxTime, comment));
        }
        createProjectsFromData(storageObjects);
    }

    //Schritt 2: Die StorageObjekte werden iteriert und bezüglich des Namens entschieden ob ein Projekt erstellt
    // werden muss, oder ob nur das StorageObjekt in ein bestehendes Projekt hinzugefügt wird
    private void createProjectsFromData(ArrayList<StorageObject> storageObjects) throws IOException {
        Manager.projectList.clear();
        for(StorageObject store : storageObjects) {
            //Wenn noch kein Projekt existiert, lege eines an
            if (Manager.projectList.size() == 0) {
                Manager.projectList.add(new CTR_Project_Module(store.getName(), store.getClient(), store.getMaxTime(), Manager.projectList.size()));
                Manager.projectList.get(Manager.projectList.size()-1).addStorageObject(store);
                System.out.println("Erstes Projekt erstellt");

            //Wenn der Name des erstellen Projektes ungleich dem des StorageObjektes ist, lege ein Neues Projekt an
            } else if(!Manager.projectList.get(Manager.projectList.size()-1).getName().equals(store.getName())) {
                Manager.projectList.add(new CTR_Project_Module(store.getName(), store.getClient(), store.getMaxTime(), Manager.projectList.size()));
                Manager.projectList.get(Manager.projectList.size()-1).addStorageObject(store);
                System.out.println("Neues PRojekt erstellt, weil Name ungleich");
            }

            //Wenn das alles nicht der Fall ist, dann füge das StorageObjekt dem letzten Projekt hinzu
            else {
                Manager.projectList.get(Manager.projectList.size()-1).addStorageObject(store);
                System.out.println("StorageObjekt zu aktuellem Projekt hinzugefügt");
            }
        }

        createProjects();
    }
    */

    //Schritt 3: Die erstellten Projekte als FXML Element im Dashboard platzieren und mit dem enstprechenden Controller
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
