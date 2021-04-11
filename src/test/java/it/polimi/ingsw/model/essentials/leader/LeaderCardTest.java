package it.polimi.ingsw.model.essentials.leader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LeaderCardTest {

    private LeaderCard slotLeader;

    //Tests the constructor
    @Test
    @BeforeEach
    public void ConstructorTest() throws NegativeQuantityException, InvalidQuantityException {

        slotLeader = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2),
                new CardType(CardColor.GREEN, 1, 2))), 5,
                new SlotEffect(new PhysicalResource(ResType.SHIELD, 2)));
    }



    //Test the writing on json file for LeaderCard and instantiation of LeaderCard via json file
    @Test
    public void JsonTest() {

        //Set the path where to find the file json
        String filePath = "src/test/resources/SlotLeaderExample.json";

        //Build the parser for json file
        Gson g = requirableConfig(effectConfig(new GsonBuilder())).setPrettyPrinting().create();

        //%%%%%%%%%%%%%%% Writing on json file %%%%%%%%%%%%%%%%%%
        try (FileWriter writer = new FileWriter(filePath)) {
            g.toJson(slotLeader, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //%%%%%%%%%%%%% Reading from json file &&&&&&&&&&&&&&&&&&
        //open the file
        try (FileReader reader = new FileReader(filePath)) {
            //parse the object and instantiate it
            LeaderCard extractedJson = g.fromJson(reader, LeaderCard.class);

//            System.out.println(extractedJson);
            assertEquals(extractedJson, slotLeader);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void slotLeaderActivation(){
        //TODO
    }

    @Test
    public void isActivableTest(){
        //TODO
    }

    //TODO all the other leader's effect tests
}
