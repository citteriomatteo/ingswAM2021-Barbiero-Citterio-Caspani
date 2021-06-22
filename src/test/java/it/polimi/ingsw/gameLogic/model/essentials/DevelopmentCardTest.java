package it.polimi.ingsw.gameLogic.model.essentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.jsonUtilities.GsonHandler;
import it.polimi.ingsw.gameLogic.exceptions.InvalidQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.SingleMatchException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.*;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static it.polimi.ingsw.gameLogic.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

//Test for DevelopmentCard class
public class DevelopmentCardTest extends CommonThingsTest {
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

            assertEquals(extractedJson, devCard);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isBuyableTest() throws WrongSettingException, SingleMatchException, NegativeQuantityException {
        List<Player> players = new ArrayList<>(List.of(new Player("Joe"), new Player("Sara")));
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummaries(players, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match1 = new MultiMatch(players);

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
