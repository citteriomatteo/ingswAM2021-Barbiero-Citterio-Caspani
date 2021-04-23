package it.polimi.ingsw.model.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.ctosmessage.MarketDrawMessage;
import it.polimi.ingsw.network.message.ctosmessage.SwitchShelfMessage;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {
    @Test
    public void parsingTest() {
        //Set the path where to find the file json
        String filePath = "src/test/resources/MessageExample.json";

        //Build the parser for json file
        Gson g = cToSMessageConfig(new GsonBuilder()).setPrettyPrinting().create();

        Message msg = new SwitchShelfMessage("giorgio", 1,2);
        Message msg2 = new MarketDrawMessage("carlo", true,1);
        List<Message> messages = new ArrayList<>();
        messages.add(msg);
        messages.add(msg2);

        //extrapolate the type of the collection
        Type collectionType = new TypeToken<ArrayList<Message>>(){}.getType();

        //%%%%%%%%%%%%%%% Writing on json file %%%%%%%%%%%%%%%%%%
        try (FileWriter writer = new FileWriter(filePath)) {

            g.toJson(messages, collectionType, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //%%%%%%%%%%%%% Reading from json file &&&&&&&&&&&&&&&&&&
        //open the file
        try (FileReader reader = new FileReader(filePath)) {
            //parse the object and instantiate it
            ArrayList<Message> extractedJson = g.fromJson(reader, collectionType);

            System.out.println(extractedJson);
            assertEquals(extractedJson.get(0).getNickname(), msg.getNickname());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
