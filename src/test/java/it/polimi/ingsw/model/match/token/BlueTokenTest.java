package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

public class BlueTokenTest {
    private SingleMatch singleMatch;
    private BlueToken blueToken;

    @Test
    public void testOnDraw() throws NegativeQuantityException, MatchEndedException, FileNotFoundException, WrongSettingException {
        singleMatch = new SingleMatch(new Player("player1"),"src/test/resources/StandardConfiguration.json");
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

        assertThrows(MatchEndedException.class,()->blueToken.onDraw(singleMatch));

    }
}
