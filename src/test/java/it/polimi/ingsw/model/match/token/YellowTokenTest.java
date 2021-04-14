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

public class YellowTokenTest {
    private SingleMatch singleMatch;
    private YellowToken yellowToken = new YellowToken();

    @Test
    public void testOnDraw() throws NegativeQuantityException, FileNotFoundException, WrongSettingException, MatchEndedException {
        singleMatch = new SingleMatch(new Player("player1"),"src/test/resources/StandardConfiguration.json");
        yellowToken = new YellowToken();

        assertTrue(yellowToken.onDraw(singleMatch));

        CardType[] cards = singleMatch.getCardGrid().countRemaining();


        int count = 0;
        for(CardType type : cards){
            if (type.getColor().equals(CardColor.YELLOW))
                count = count + type.getQuantity();
        }
        assertEquals(10, count);

        for(int i=0; i<5; i++){
            assertTrue(yellowToken.onDraw(singleMatch));
        }

        assertThrows(MatchEndedException.class,()->yellowToken.onDraw(singleMatch));
    }
}
