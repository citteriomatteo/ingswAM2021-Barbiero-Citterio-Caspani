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

import static it.polimi.ingsw.gameLogic.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

public class YellowTokenTest extends CommonThingsTest {
    private SingleMatch singleMatch;
    private YellowToken yellowToken = new YellowToken();

    @Test
    public void testOnDraw() throws NegativeQuantityException, FileNotFoundException, WrongSettingException, LastRoundException {
        Player p = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/StandardConfiguration.json");
        setSummary(p, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        singleMatch = new SingleMatch(p);

        yellowToken = new YellowToken();

        assertTrue(yellowToken.onDraw(singleMatch));

        CardType[] cards = singleMatch.getCardGrid().countRemaining();


        int count = 0;
        for(CardType type : cards){
            if (type.getColor().equals(CardColor.YELLOW))
                count = count + type.getQuantity();
        }
        assertEquals(10, count);

        for(int i=0; i<4; i++){
            assertTrue(yellowToken.onDraw(singleMatch));
        }

        assertThrows(LastRoundException.class,()->yellowToken.onDraw(singleMatch));
    }

    @Test
    public void testToString(){
        assertEquals(yellowToken.getClass().getSimpleName(), yellowToken.toString());
    }
}
