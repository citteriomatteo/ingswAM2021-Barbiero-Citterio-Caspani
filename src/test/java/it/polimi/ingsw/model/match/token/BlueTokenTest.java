package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.CommonThingsTest;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

public class BlueTokenTest extends CommonThingsTest {
    private SingleMatch singleMatch;
    private BlueToken blueToken;

    @Test
    public void testOnDraw() throws NegativeQuantityException, LastRoundException, FileNotFoundException, WrongSettingException {
        Player p = new Player("player1");
        setSummary(p, getCardMap(assignConfiguration("src/test/resources/StandardConfiguration.json")));
        singleMatch = new SingleMatch(p);

        blueToken = new BlueToken();

        assertTrue(blueToken.onDraw(singleMatch));

        CardType[] cards = singleMatch.getCardGrid().countRemaining();

        int count = 0;
        for(CardType type : cards){
            if (type.getColor().equals(CardColor.BLUE))
                count = count + type.getQuantity();
        }
        assertEquals(10, count);

        for(int i=0; i<4; i++){
            assertTrue(blueToken.onDraw(singleMatch));
        }

        assertThrows(LastRoundException.class,()->blueToken.onDraw(singleMatch));

    }
}
