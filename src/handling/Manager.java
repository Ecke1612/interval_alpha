package handling;

import gui.controller.CTR_Project_Module;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import object.ClientStorageObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Eike on 20.05.2017.
 */
public class Manager {

    public static ArrayList<CTR_Project_Module> projectList = new ArrayList<>();
    public static ArrayList<Parent> projectUIList = new ArrayList<>();

    public static ArrayList<ClientStorageObject> clients = new ArrayList<>();

    public static FileInputStream reminder_image; { loadImage("/res/images/reminder.png"); }



    private void loadImage(String path) {
        try {
            reminder_image = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String printTime(int seconds) {
        int sec = seconds%60;
        int min = (seconds/60)%60;
        int hour = seconds/3600;
        String zeroHour = "";
        String zeroMin = "";
        String zeroSec = "";

        if(hour < 10 && hour >= 0) zeroHour = "0";
        if(min < 10 && min >= 0) zeroMin = "0";
        if(sec < 10 && sec >= 0) zeroSec = "0";

        String text = zeroHour + hour + ":" + zeroMin + min + ":" + zeroSec + sec;
        return text;
    }

    public static String printTimeWithoutSec(int seconds) {
        int sec = seconds%60;
        int min = (seconds/60)%60;
        int hour = seconds/3600;
        String zeroHour = "";
        String zeroMin = "";

        if(hour < 10) zeroHour = "0";
        if(min < 10) zeroMin = "0";

        String text = zeroHour + hour + ":" + zeroMin + min;
        return text;
    }

    public static String getCSSTextColorByBrightness(Color color, boolean b) {
        if(color.getBrightness() < 0.81) {
            if(b) {
                return ("-fx-text-fill: rgb(255,255,255);" +
                        "-fx-border-color: rgba(255,255,255,0.4);");
            } else {
                return "-fx-text-fill: rgb(255,255,255);";
            }
        } else {
            if(b) {
                return ("-fx-text-fill: rgb(0,0,0);" +
                        "-fx-border-color: rgba(0,0,0,0.2);");
            }else {
                return "-fx-text-fill: rgb(0,0,0);";
            }
        }
    }

    public static String getCSSComboBoxColorByBrightness(Color color) {
        if(color.getBrightness() < 0.81) {
            return ("comboboxWhite");
        } else {
            return ("comboboxBlack");
        }
    }

    public static String getHexColorString(Color c) {
        String hex = String.format( "#%02X%02X%02X",
                (int)( c.getRed() * 255 ),
                (int)( c.getGreen() * 255 ),
                (int)( c.getBlue() * 255 ) );
        return hex;
    }

    public static String getCSSPath1(int index) {
        String path = "";
        switch(index) {
            case 0:
                path = "/css/ui_view_grey.css";
                break;
            case 1:
                path = "/css/ui_view_green.css";
                break;
            case 2:
                path = "/css/ui_view_blue.css";
                break;
            case 3:
                path = "/css/ui_view_vanilla.css";
                break;
        }
        return path;
    }

}
