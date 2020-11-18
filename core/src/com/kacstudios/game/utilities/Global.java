package com.kacstudios.game.utilities;
//global variables

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Global {
    public static int GameVolume = 50;
    public static int MusicVolume = 50;

    private static Scanner fileScanner;
    private static FileWriter fileWriter;

    /**
     * Overrides default static global variables for settings only if an options file is found
     */
    public static void loadGlobalSettingsFromFile() {
        File settingsFile = new File("core/assets/saves/options.mcconnell");
        if (settingsFile.exists()) {
            String fileLine;
            String[] splitFileLine;

            try {
                fileScanner = new Scanner(settingsFile);

                fileLine = fileScanner.nextLine();
                splitFileLine = fileLine.split(",");
                GameVolume = Integer.parseInt( splitFileLine[1] );

                fileLine = fileScanner.nextLine();
                splitFileLine = fileLine.split(",");
                MusicVolume = Integer.parseInt( splitFileLine[1] );

            }
            catch (FileNotFoundException e) { e.printStackTrace(); }
            finally { fileScanner.close(); }
        }
    }

    public static void saveGlobalSettingsToFile() {
        File settingsFile = new File("core/assets/saves/options.mcconnell");
        try {
            fileWriter = new FileWriter(settingsFile);
            fileWriter.write("GameVolume,"+String.valueOf(GameVolume));
            fileWriter.write("\nMusicVolume,"+String.valueOf(MusicVolume));
            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
