package handling;

import gui.controller.CTR_Project_Module;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import object.ClientStorageObject;

import java.util.ArrayList;

/**
 * Created by Eike on 20.05.2017.
 */
public class Manager {

    public static ArrayList<CTR_Project_Module> projectList = new ArrayList<>();
    public static ArrayList<Parent> projectUIList = new ArrayList<>();

    public static ArrayList<ClientStorageObject> clients = new ArrayList<>();

    public static ArrayList<String> configList = new ArrayList<>();

    public static Color darkblue = Color.rgb(34,83,120); //#225378
    public static Color turkys = Color.rgb(22,149,163); //#1695a3
    public static Color lightTurkys = Color.rgb(172,240,242); //#acf0f2
    public static Color beige = Color.rgb(243,255,226); //#f3ffe2
    public static Color orange = Color.rgb(235,127,0); //#eb7f00

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

    public static String getCSSTextColorByBrightness(Color color) {
        if(color.getBrightness() < 0.81) {
            return ("-fx-text-fill: rgb(255,255,255)");
        } else {
            return ("-fx-text-fill: rgb(0,0,0)");
        }
    }

    public static String getCSSPrompTextColorByBrightness(Color color) {
        if(color.getBrightness() < 0.81) {
            return ("-fx-prompt-text-fill: rgb(255,255,255)");
        } else {
            return ("-fx-prompt-text-fill: rgb(0,0,0)");
        }
    }

    public static String getHexColorString(Color c) {
        String hex = String.format( "#%02X%02X%02X",
                (int)( c.getRed() * 255 ),
                (int)( c.getGreen() * 255 ),
                (int)( c.getBlue() * 255 ) );
        return hex;
    }

    public static String getCSSPath(int index) {
        String path = "";
        switch(index) {
            case 0:
                path = "/css/ui_view.css";
                break;
            case 1:
                path = "/css/ui_view_green.css";
                break;
            case 2:
                path = "/css/ui_view_soft.css";
                break;
        }
        return path;
    }

}
