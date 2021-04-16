package it.polimi.ingsw.model.essentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.gsonUtilities.GsonHandler;
import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.SingleMatchException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Test for DevelopmentCard class
public class DevelopmentCardTest {
    DevelopmentCard devCard;

    //Tests the constructor
    @Test
    @BeforeEach
    public void ConstructorTest() throws InvalidQuantityException {

        devCard = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        assertEquals(devCard.getProduction().getCost().size(), 1);
    }

    //Test the instantiation of DevelopmentCard via json file
    @Test
    public void JsonTest(){
        //Set the path where to find the file json
        String filePath = "src/test/resources/DevelopmentCardExample.json";

        //Build the parser for json file
        Gson g = GsonHandler.resourceConfig(new GsonBuilder()).create();

        //open the file
        try (FileReader reader = new FileReader(filePath)) {
            //parse the object and instantiate it
            DevelopmentCard extractedJson = g.fromJson(reader, DevelopmentCard.class);

//            System.out.println(extractedJson);
            assertEquals(extractedJson, devCard);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isBuyableTest() throws WrongSettingException, FileNotFoundException, SingleMatchException, NegativeQuantityException {
        Match match1 = new MultiMatch(Arrays.asList(new Player("Joe"), new Player("Sara")), "src/test/resources/StandardConfiguration.json");
        Player joe = match1.getPlayer("Joe");
        assertFalse(devCard.isBuyable(joe));
        joe.addToStrongBox(new PhysicalResource(ResType.COIN, 2));
        assertFalse(devCard.isBuyable(joe));
        joe.addToStrongBox(new PhysicalResource(ResType.SHIELD, 1));
        assertFalse(devCard.isBuyable(joe));
        joe.addToStrongBox(new PhysicalResource(ResType.SHIELD, 1));
        assertTrue(devCard.isBuyable(joe));

    }
}
