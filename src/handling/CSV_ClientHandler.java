package handling;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.scene.paint.Color;
import object.ClientStorageObject;

import java.io.*;
import java.util.List;

/**
 * Created by Eike on 29.05.2017.
 */
public class CSV_ClientHandler {

    public static void csvWriter() throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter("data/clients.csv"));

        for(ClientStorageObject client : Manager.clients) {
            String[] entries = {client.getName(), client.getColor().toString()};
            writer.writeNext(entries);
        }
        writer.close();
    }

    public void csvLoader() throws IOException{
        Manager.clients.clear();

        CSVReader reader = new CSVReader(new FileReader("data/clients.csv"));
        List<String[]> data = reader.readAll();
        for(String[] client : data) {
            if(client.length == 1) {
                Manager.clients.add(new ClientStorageObject(client[0], new Color(0.5,0.5,0.5,0)));
            } else {
                Manager.clients.add(new ClientStorageObject(client[0], Color.web(client[1])));
            }
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
