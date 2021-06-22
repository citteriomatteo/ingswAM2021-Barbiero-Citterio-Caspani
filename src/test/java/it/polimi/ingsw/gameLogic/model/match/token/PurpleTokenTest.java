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

public class PurpleTokenTest extends CommonThingsTest {
    private SingleMatch singleMatch;
    private PurpleToken purpleToken = new PurpleToken();

    @Test
    public void testOnDraw() throws NegativeQuantityException, FileNotFoundException, WrongSettingException, LastRoundException {
        Player p = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/StandardConfiguration.json");
        setSummary(p, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        singleMatch = new SingleMatch(p);

        purpleToken = new PurpleToken();

        assertTrue(purpleToken.onDraw(singleMatch));

        CardType[] cards = singleMatch.getCardGrid().countRemaining();


        int count = 0;
        for(CardType type : cards){
            if (type.getColor().equals(CardColor.PURPLE))
                count = count + type.getQuantity();
        }
        assertEquals(10, count);

        for(int i=0; i<4; i++){
            assertTrue(purpleToken.onDraw(singleMatch));
        }

        assertThrows(LastRoundException.class,()->purpleToken.onDraw(singleMatch));
    }
}
