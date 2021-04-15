package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class GreenTokenTest {
    private SingleMatch singleMatch;
    private GreenToken greenToken = new GreenToken();

    @Test
    public void testOnDraw() throws NegativeQuantityException, FileNotFoundException, WrongSettingException, MatchEndedException {
        singleMatch = new SingleMatch(new Player("player1"),"src/test/resources/StandardConfiguration.json");
        greenToken = new GreenToken();

        assertTrue(greenToken.onDraw(singleMatch));

        CardType[] cards = singleMatch.getCardGrid().countRemaining();

        int count = 0;
        for(CardType type : cards){
            if (type.getColor().equals(CardColor.GREEN))
                count = count + type.getQuantity();
        }
        assertEquals(10, count);

        for(int i=0; i<4; i++){
            assertTrue(greenToken.onDraw(singleMatch));
        }

        assertThrows(MatchEndedException.class,()->greenToken.onDraw(singleMatch));
    }
}
