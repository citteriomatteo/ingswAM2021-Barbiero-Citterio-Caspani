package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.match.player.Verificator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTypeTest {

    @Test
    public void testEquals() throws InvalidQuantityException {
        CardType card1 = new CardType(CardColor.BLUE, 2, 3);
        CardType card2 = new CardType(CardColor.BLUE,2,5);
        CardType card3 = new CardType(CardColor.BLUE,1,2);
        CardType card4 = new CardType(CardColor.GREEN,1,2);

        assertTrue(card1.equals(card2));
        assertFalse(card1.equals(card3));
        assertFalse(card1.equals(card4));
        assertFalse(card3.equals(card4));

    }

    @Test
    public void testCreate(){
        assertThrows(InvalidQuantityException.class,()-> new CardType(CardColor.GREEN,4,3));
        assertThrows(InvalidQuantityException.class,()-> new CardType(CardColor.GREEN,-2,3));

    }

    @Test
    public void testVerify(Verificator verificator) throws InvalidQuantityException {
        CardType cardType = new CardType(CardColor.GREEN,2,1);

        assertTrue(cardType.verify(verificator));
    }
}
