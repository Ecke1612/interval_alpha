package updater;

import gui.controller.CTR_Config;
import handling.File_Handler;
import main.Main_Application;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.Date;

/**
 * Created by Eike on 08.06.2017.
 */
public class FTP_Handler {

    private FTPClient ftp = null;

    public FTP_Handler(String host, String user, String pwd) throws Exception {
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(host);
        reply = ftp.getReplyCode();
        if(!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Konnte keine Verbindung zum Server herstellen");
        }
        ftp.login(user, pwd);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
    }

    public void downloadFile(String remoteFile, String localFilePath) {
        try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
            this.ftp.retrieveFile(remoteFile, fos);
            System.out.println(remoteFile + " erfolgreich geladen");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileIntervalData() {
        File file1 = new File("data/archiv.csv");
        File file2 = new File("data/clients.csv");
        File file3 = new File("data/trackingData.csv");
        Date date = new Date();
        String[] userInfo = new String[3];
        try {
            userInfo[0] = date.toString() + "\n";
            userInfo[1] = "version: " + Main_Application.build + "\n";
            userInfo[2] = "Name: " + CTR_Config.configObject.getUsername() + "\n";
        } catch (Exception eInfo){
            eInfo.printStackTrace();
        }
        try {
            File_Handler.fileWriter("log.txt", userInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file4 = new File("log.txt");
        try {
            ftp.changeWorkingDirectory("intervalRemoteData");
            ftp.makeDirectory(System.getProperty("user.name"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String trackingDataFile = "/intervalRemoteData/"+ System.getProperty("user.name") +"/trackingData.csv";
        System.out.println("path1: " + trackingDataFile);
        String clientsFile = "/intervalRemoteData/"+ System.getProperty("user.name") +"/clients.csv";
        String archivFile = "/intervalRemoteData/"+ System.getProperty("user.name") +"/archiv.csv";
        String userFile = "/intervalRemoteData/" + System.getProperty("user.name") + "/log.txt";
        try {
            InputStream inputStream1 = new FileInputStream(file1);
            InputStream inputStream2 = new FileInputStream(file2);
            InputStream inputStream3 = new FileInputStream(file3);
            InputStream inputStream4 = new FileInputStream(file4);
            System.out.println("uploading File");

            ftp.storeFile(clientsFile, inputStream2);
            ftp.storeFile(trackingDataFile, inputStream3);
            ftp.storeFile(userFile, inputStream4);
            boolean done = ftp.storeFile(archivFile, inputStream1);
            inputStream1.close();
            inputStream2.close();
            inputStream3.close();
            inputStream4.close();
            File_Handler.deleteFile("log.txt");
            if(done) {
                System.out.println("data uploaded");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException e) {
                //Mache gar nichts
                System.out.println("Mache nichts");
            }
        }
    }

}
