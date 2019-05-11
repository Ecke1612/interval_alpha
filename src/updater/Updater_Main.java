package updater;

import main.launcher.CTR_StartScreen;

/**
 * Created by Eike on 17.06.2017.
 */
public class Updater_Main {

    public static String build = "0";

    public boolean start(String version, CTR_StartScreen ctr_startScreen) throws Exception {
        Updater_Main.build = version;

        System.out.println("old build main: " + build);

            Updater updater = new Updater();
            boolean update = updater.checkForUpdate();
            if (update) {
                updater.showChangeLog();
                return true;
            } else {
                System.out.println("altes Programm gestartet");
                Thread.sleep(1000);
                return false;
            }
        }

}
