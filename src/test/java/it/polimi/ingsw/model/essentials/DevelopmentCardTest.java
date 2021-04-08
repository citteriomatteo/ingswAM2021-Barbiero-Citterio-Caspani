package it.polimi.ingsw.model.essentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.gsonUtilities.DevelopmentCardDeserializer;
import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Test for DevelopmentCard class
public class DevelopmentCardTest {
    DevelopmentCard devCard;

    //Tests the constructor and the gson builder
    @Test
    @BeforeEach
    public void ConstructorTest() throws InvalidAddFaithException, NegativeQuantityException, InvalidQuantityException {

        //Create a gsonBuilder used for generate the effective Gson object that do the deserialization of the json string
        GsonBuilder gsonBuilder = new GsonBuilder();
        //modify the builder adding the particular method for creating complex objects
        gsonBuilder.registerTypeAdapter(DevelopmentCard.class, new DevelopmentCardDeserializer());

        Gson gson = gsonBuilder.create();

        devCard = gson.fromJson("{ \"type\": { \"color\": \"GREEN\", \"level\": 1 },\"price\": " +
                "[{ \"type\": \"SHIELD\", \"quantity\": 2 }],\"production\": { \"sentCost\": [{ \"type\": \"COIN\", \"quantity\": 1 }],\"earnings\": " +
                "[{ \"faithPoint\": true, \"quantity\": 1 }]},\"winPoints\": 1 }", DevelopmentCard.class);

        DevelopmentCard devCard2 = new DevelopmentCard(new CardType(CardColor.GREEN,1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1 );

        assertEquals(devCard2, devCard);
    }

    @Test
    public void isBuyableTest(){
        //TODO isBuyableTest after implementing verify methods
    }
}
