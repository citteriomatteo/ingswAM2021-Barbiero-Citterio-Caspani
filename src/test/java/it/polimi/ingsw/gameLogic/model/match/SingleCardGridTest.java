package it.polimi.ingsw.gameLogic.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.gameLogic.exceptions.*;
import it.polimi.ingsw.gameLogic.model.essentials.*;
import it.polimi.ingsw.jsonUtilities.GsonHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SingleCardGridTest {

    private SingleCardGrid grid;

    @Test
    @BeforeEach
    public void ConstructorTest(){
        //Set the path where to find the file json
        String filePath = "src/test/resources/CardGridExample.json";

        //Build the json file parser for DevelopmentCard -> need to parse resource
        Gson g = GsonHandler.resourceConfig(new GsonBuilder()).create();

        //open the file
        try (FileReader reader = new FileReader(filePath)) {
            //extrapolate the type of the collection
            Type collectionType = new TypeToken<ArrayList<DevelopmentCard>>(){}.getType();
            //parse the developmentCard list
            ArrayList<DevelopmentCard> extractedJson = g.fromJson(reader, collectionType);

            //create the grid
            grid = new SingleCardGrid(extractedJson);

        } catch (IOException | WrongSettingException e) {
            fail();
        }
    }

    //there are 3 green cards, the first call to discard should work, the second one should return exception
    @Test
    public void discardEndGameTest(){
        try {
            grid.discard(CardColor.GREEN);
        } catch (LastRoundException e) {
            fail();
        }

        assertThrows(LastRoundException.class, ()->grid.discard(CardColor.GREEN));
    }

    @Test
    public void takeTest() throws InvalidCardRequestException, NoMoreCardsException, InvalidQuantityException, LastRoundException {
        DevelopmentCard card = grid.take(1,CardColor.GREEN.getVal());
        DevelopmentCard cardExpected = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        assertEquals(cardExpected, card);

        grid.take(2,CardColor.GREEN.getVal());
        assertThrows(LastRoundException.class, ()-> grid.take(3, CardColor.GREEN.getVal()));

    }

    @Test
    public void discardTest(){
        int count = 0;
        CardType[] inventories = grid.countRemaining();
        for(CardType type : inventories){
            if (type.getColor().equals(CardColor.PURPLE))
                count = count + type.getQuantity();
        }
        assertEquals(3, count);

        try {
            grid.discard(CardColor.PURPLE);
        } catch (LastRoundException e) {
            fail();
        }

        count = 0;
        inventories = grid.countRemaining();
        for(CardType type : inventories){
            if (type.getColor().equals(CardColor.PURPLE))
                count = count + type.getQuantity();
        }

        assertEquals(1, count);

    }
}
