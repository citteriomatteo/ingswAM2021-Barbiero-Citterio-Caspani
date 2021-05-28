package it.polimi.ingsw.jsonUtilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class that contains static methods for to read preferences from a json file
 */
public class Preferences {
    static final Path filePath = Path.of("src/main/resources/json/Preferences.json");

    /**
     * Reads the default number of port from the file Preferences.json
     * @return the default number of port
     */
    static public int ReadPortFromJSON() {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject object = (JsonObject) jsonParser.parse(Files.readString(filePath));
            return  object.get("ServerPort").getAsInt();
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
    static public String ReadHostFromJSON(){
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject object = (JsonObject) jsonParser.parse(Files.readString(filePath));
            return object.get("ServerIP").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Application shutdown due to an internal error, cannot find " + filePath);
            System.exit(1);
            return null;
        }
    }
}
