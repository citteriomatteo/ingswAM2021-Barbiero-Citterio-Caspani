package it.polimi.ingsw.gameLogic.model.essentials;

import it.polimi.ingsw.gameLogic.exceptions.InvalidOperationException;
import it.polimi.ingsw.gameLogic.exceptions.InvalidQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.CommonThingsTest;
import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardTypeTest extends CommonThingsTest {

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
    public void testIsResource(){
        Requirable card = new CardType(CardColor.BLUE, 2, 3);
        assertFalse(card.isAResource());
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

        Player player = new Player("player1");
        MatchConfiguration configuration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(configuration), configuration.getCustomPath(), configuration.getBasicProduction());
        Match match = new SingleMatch(player);
        player.getPersonalBoard().getDevCardSlots().pushNewCard(1,developmentCard);


        assertTrue(cardType.verify(player));
        assertFalse(cardType1.verify(player));
        assertFalse(cardType2.verify(player));
        assertFalse(cardType3.verify(player));
    }
}
