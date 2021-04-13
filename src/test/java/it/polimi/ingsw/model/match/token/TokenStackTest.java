package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.SingleMatchException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenStackTest {
    private TokenStack tokenStack = new TokenStack();
    @Test
    public void testDraw() throws FileNotFoundException, NegativeQuantityException, WrongSettingException, MatchEndedException, SingleMatchException {
        SingleMatch match = new SingleMatch(new Player("player1"),"src/test/resources/StandardConfiguration.json");
        assertTrue(tokenStack.draw(match));
    }
}
