package it.polimi.ingsw.model.essentials;


import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.exceptions.InvalidQuantityException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.*;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.observer.ModelObserver;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CardTypeTest extends CommonThingsTest {
    private Player player;

    @Test
    public void testEquals() throws InvalidQuantityException {
        CardType card1 = new CardType(CardColor.BLUE, 2, 3);
        CardType card2 = new CardType(CardColor.BLUE,2,5);
        CardType card3 = new CardType(CardColor.BLUE,1,2);
        CardType card4 = new CardType(CardColor.GREEN,1,2);

        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
        assertNotEquals(card1, card4);
        assertNotEquals(card3, card4);

    }

    @Test
    public void testCreate(){
        assertThrows(InvalidQuantityException.class,()-> new CardType(CardColor.GREEN,4,3));
        assertThrows(InvalidQuantityException.class,()-> new CardType(CardColor.GREEN,-2,3));

    }

    @Test
    public void testVerify() throws InvalidQuantityException, WrongSettingException, InvalidOperationException {
        CardType cardType = new CardType(CardColor.GREEN,1,1);
        CardType cardType1 = new CardType(CardColor.GREEN,2,1);
        CardType cardType2 = new CardType(CardColor.GREEN,1,2);
        CardType cardType3 = new CardType(CardColor.YELLOW,1,1);
        DevelopmentCard developmentCard = new DevelopmentCard(new CardType(CardColor.GREEN,1),new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        player = new Player("player1");
        setSummary(player);
        Match match = new SingleMatch(player);
        player.getPersonalBoard().getDevCardSlots().pushNewCard(1,developmentCard);


        assertTrue(cardType.verify(player));
        assertFalse(cardType1.verify(player));
        assertFalse(cardType2.verify(player));
        assertFalse(cardType3.verify(player));
    }
}
