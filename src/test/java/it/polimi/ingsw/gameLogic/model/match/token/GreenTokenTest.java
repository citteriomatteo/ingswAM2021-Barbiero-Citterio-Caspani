package it.polimi.ingsw.gameLogic.model.match.token;

import it.polimi.ingsw.gameLogic.model.essentials.CardColor;
import it.polimi.ingsw.gameLogic.model.essentials.CardType;
import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.CommonThingsTest;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class GreenTokenTest extends CommonThingsTest {
    private SingleMatch singleMatch;
    private GreenToken greenToken = new GreenToken();

    @Test
    public void testOnDraw() throws NegativeQuantityException, FileNotFoundException, WrongSettingException, LastRoundException {
        Player p = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/StandardConfiguration.json");
        setSummary(p, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        singleMatch = new SingleMatch(p);

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

        assertThrows(LastRoundException.class,()->greenToken.onDraw(singleMatch));
    }

    @Test
    public void testToString(){
        assertEquals(greenToken.getClass().getSimpleName(), greenToken.toString());
    }
}
