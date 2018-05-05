package handling;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import gui.controller.CTR_Project_Module;
import gui.controller.CTR_Report;
import javafx.scene.paint.Color;
import object.ClientStorageObject;
import object.Report_Object;
import object.StorageObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eike on 06.06.2017.
 */
public class Archiv_Handler {

    public static ArrayList<Report_Object> archivObjects = new ArrayList<>();


    public static void writeToArchiv(CTR_Project_Module project) throws IOException {
        FileWriter pw = new FileWriter("data/archiv.csv", true);
        CSVWriter writer = new CSVWriter(pw, ';');

        String header = "1" + ";" + project.getClient().getName() + ";" + project.getName() + ";" + project.getMaxTimeHours();
        String[] headerEntries = header.split(";");
        writer.writeNext(headerEntries);
        for(StorageObject storage : project.getStorageObjects()) {
            String temp = "0" + ";" + storage.getDate() + ";" + storage.getSec() + ";" + storage.getComment();
            String[] entries = temp.split(";");
            writer.writeNext(entries);
        }
        writer.close();
    }

    public static void writeWholeArchiv() throws IOException {
        FileWriter pw = new FileWriter("data/archiv.csv");
        CSVWriter writer = new CSVWriter(pw, ';');

        for(Report_Object report : archivObjects) {
            String header = "1" + ";" + report.getClient().getName() + ";" + report.getName() + ";" + report.getMaxTime();
            String[] headerEntries = header.split(";");
            writer.writeNext(headerEntries);
            for(StorageObject storage : report.getStorageObjects()) {
                String temp = "0" + ";" + storage.getDate() + ";" + storage.getSec() + ";" + storage.getComment();
                String[] entries = temp.split(";");
                writer.writeNext(entries);
            }
        }
        writer.close();
    }

    public static void loadArchiv() throws IOException {
        archivObjects.clear();
        CSVReader reader = new CSVReader(new FileReader("data/archiv.csv"), ';');
        List<String[]> data = reader.readAll();

        for(String[] line : data) {
            int header = Integer.parseInt(line[0]);
            if(header == 1) {
                try {
                    //suche das richtige Kundenobjekt:
                    ClientStorageObject clientObject = new ClientStorageObject(line[1], new Color(0,0,0,0));
                    for(ClientStorageObject client : Manager.clients) {
                        if(client.getName().equals(line[1])) {
                            clientObject = client;
                        }
                    }
                    //
                    archivObjects.add(new Report_Object(line[2], clientObject, Integer.parseInt(line[3]), archivObjects.size()));
                    archivObjects.get(archivObjects.size()-1).setClosed(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Archivobjekt konnte nicht erstellt werden");
                }
            } else if(header == 0) {
                archivObjects.get(archivObjects.size()-1).addStorageObject(new StorageObject(LocalDate.parse(line[1]), Integer.parseInt(line[2]), line[3]));
            } else System.out.println("unbekannter Fehler beim Laden des Archivs");
        }
    }

    /*
    public static void loadArchiv1() throws IOException {
        archivObjects.clear();
        List<String[]> tempList = loader();
        for(String[] line : tempList) {
            try {
                archivObjects.add(new StorageObject(LocalDate.parse(line[1]), Integer.parseInt(line[2]), line[3]));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Fehler beim laden des Archivs");
            }
        }
    }

    private static List<String[]> loader() throws IOException {
        if(File_Handler.fileExist("data/archiv.csv")) {
            try {
                CSVReader reader = new CSVReader(new FileReader("data/archiv.csv"), ';');
                List<String[]> archivList = reader.readAll();
                return archivList;
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("Fehler beim laden des Archivs");
                return null;
            }
        }else {
            System.out.println("Datei nicht vorhanden");
            return null;
        }
    }*/

    public static ArrayList<Report_Object> getArchivObjects() {
        return archivObjects;
    }
}
