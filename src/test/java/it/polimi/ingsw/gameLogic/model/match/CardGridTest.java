package it.polimi.ingsw.gameLogic.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.gameLogic.model.essentials.*;
import it.polimi.ingsw.gameLogic.exceptions.*;
import it.polimi.ingsw.jsonUtilities.GsonHandler;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static it.polimi.ingsw.gameLogic.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

public class CardGridTest extends CommonThingsTest {

    private CardGrid grid;

    @Test
    @BeforeEach
    public void ConstructorTest(){
        //Set the path where to find the file json
        String filePath = "src/test/resources/CardGridExample.json";

        //Build the json file parser for DevelopmentCard
        Gson g = GsonHandler.resourceConfig(new GsonBuilder()).create();

        //open the file
        try (FileReader reader = new FileReader(filePath)) {
            //extrapolate the type of the collection
            Type collectionType = new TypeToken<ArrayList<DevelopmentCard>>(){}.getType();
            //parse the developmentCard list
            ArrayList<DevelopmentCard> extractedJson = g.fromJson(reader, collectionType);

            //create the grid
            grid = new CardGrid(extractedJson);

            //verify that countRemaining returns an array of size 12
            assertEquals(grid.countRemaining().length, 12);

            //verify that every CardType have only one element left in the Stack
            Stream<CardType> stream = Arrays.stream(grid.countRemaining());
            stream.forEach(x -> assertEquals(1, x.getQuantity()));

        } catch (IOException | WrongSettingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dispositionTest(){
        DevelopmentCard[][] topGrid = grid.getTop();
        for (int i = 0; i < 3; i++) {
            for (CardColor color : CardColor.values()) {
                if(!topGrid[i][color.ordinal()].getType().getColor().equals(color) || !(topGrid[i][color.ordinal()].getType().getLevel() == i+1))
                    fail();
            }
        }
    }

    @Test
    public void isBuyableTest() throws NegativeQuantityException, InvalidCardRequestException, NoMoreCardsException, WrongSettingException {
        Player player = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/StandardConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        new SingleMatch(player);

        player.addToStrongBox(new PhysicalResource(ResType.SHIELD, 2));

        assertTrue(grid.isBuyable(player,1, CardColor.GREEN.getVal()));
    }


    @Test
    public void takeTest() throws InvalidCardRequestException, NoMoreCardsException, InvalidQuantityException, LastRoundException {
        DevelopmentCard card = grid.take(1,CardColor.GREEN.getVal());
        DevelopmentCard cardExpected = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        assertEquals(cardExpected, card);

    }

    @Test
    public void takeTooMuchCardsTest() throws LastRoundException {
        try {
            grid.take(1,1);
            grid.take(1,1);
            fail();
        } catch (InvalidCardRequestException e) {
            e.printStackTrace();
        } catch (NoMoreCardsException e) {
            assertTrue(true);
        }

    }

    @Test
    public void controlCardOutOfRangeTest()
    {
        Player player = new Player("player1");

        assertThrows(InvalidCardRequestException.class, () -> grid.isBuyable(player,3,5));

    }
}
