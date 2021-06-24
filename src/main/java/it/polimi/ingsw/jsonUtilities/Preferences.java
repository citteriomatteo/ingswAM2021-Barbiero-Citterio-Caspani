package it.polimi.ingsw.jsonUtilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class that contains static methods for to read preferences from a json file
 */
public class Preferences {
    private static final String filePath = "/it/polimi/ingsw/json/Preferences.json";
    private static JsonObject object;

    /**
     * Reads the default number of port from the file Preferences.json
     * @return the default number of port
     */
    static public int readPortFromJSON() {
        try {
            return getObject().get("ServerPort").getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Application shutdown due to an internal error, cannot find " + filePath);
            System.exit(1);
            return -1;
        }
    }

    /**
     * Reads the default IP address from the file Preferences.json
     * @return the default IP address
     */
    static public String readHostFromJSON(){
        try {
            return getObject().get("ServerIP").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Application shutdown due to an internal error, cannot find " + filePath);
            System.exit(1);
            return null;
        }
    }

    /**
     * Reads the default view choice from the file Preferences.json
     * @return the default view choice, true if CLI, false if GUI
     */
    static public boolean readViewFromJSON() {
        try {
            return getObject().get("CliChoice").getAsBoolean();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Application shutdown due to an internal error, cannot find " + filePath);
            System.exit(1);
            return false;
        }
    }

    /**
     * Returns the navigable jsonObject relative to the Preferences.json file
     * @return the navigable jsonObject relative to the Preferences.json file
     */
    static private JsonObject getObject(){
        if(object != null)
            return object;

        JsonParser jsonParser = new JsonParser();
        BufferedReader file = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Preferences.class.getResourceAsStream(filePath))));
        String jsonString = file.lines().collect(Collectors.joining("\n"));
        object = (JsonObject) jsonParser.parse(jsonString);
        return object;
    }
}
