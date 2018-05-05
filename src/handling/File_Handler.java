package handling;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Eike on 03.06.2017.
 */
public class File_Handler {

    public static void fileWriter(String StringPath, String[] content) throws IOException {
        Path path = Paths.get(StringPath);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for(String line : content) {
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fileWriter(String StringPath, ArrayList<String> content) throws IOException {
        Path path = Paths.get(StringPath);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for(String line : content) {
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFile(String stringPath) throws IOException {
        //Path path = Paths.get(stringPath);
        File file = new File(stringPath);
        BufferedWriter output = new BufferedWriter(new FileWriter(file));
        output.write("");
    }

    public static ArrayList<String> fileLoader(String stringPath) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        try {
            FileReader fr = new FileReader(stringPath);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null) {
                data.add(line);
            }
            fr.close();
            br.close();

            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void writeObject(Object object, String path){
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try{
            fout = new FileOutputStream(path);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(object);
            fout.close();
            oos.close();
            System.out.println("Objekt geschrieben");

        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
            System.out.println("Error Writing AccountObjects");
        }
    }

    public static Object loadObjects(String path){
        FileInputStream fin = null;
        ObjectInputStream ois = null;
        Object object;
        try{
            fin = new FileInputStream(path);
            ois = new ObjectInputStream(fin);
            object = ois.readObject();
            fin.close();
            ois.close();
            System.out.println("Object geladen: ");
            return object;
        }catch(Exception e){
            System.out.println("Error Loading AccountObjects");
            System.out.println(e);
            return null;
        }
    }

    public static boolean fileExist(String path) {
        File file = new File(path);
        if(file.exists()) {
            return true;
        }
        return false;
    }

    public static void deleteFile(String fileName){
        File f = new File(fileName);
        if(f.exists()){
            f.delete();
        }
    }

    public static void createDir(String name) {
        File dir = new File(name);
        dir.mkdir();
    }

}
