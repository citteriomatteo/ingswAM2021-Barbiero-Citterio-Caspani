package it.polimi.ingsw.gameLogic.model.match.token;

import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.CommonThingsTest;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class TokenStackTest extends CommonThingsTest {
    private SingleMatch match;
    @Test
    public void testDraw() throws WrongSettingException, LastRoundException {
        Player p = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/StandardConfiguration.json");
        setSummary(p, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        match = new SingleMatch(p);

        for(int i=0; i < 10; i++)
            assertTrue(match.getTokenStack().draw(match));

        for(int i=0; i < 100; i++)
            assertTrue(match.getTokenStack().getStack().size() > 0);
    }

    @Test
    public void testShuffle() throws WrongSettingException {
        Player p = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/StandardConfiguration.json");
        setSummary(p, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        match = new SingleMatch(p);

        Stack<Token> stack = match.getTokenStack().getStack();

        match.getTokenStack().shuffle();

        assertNotEquals(stack,match.getTokenStack().getStack());
    }
}
