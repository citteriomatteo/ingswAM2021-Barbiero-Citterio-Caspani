package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.CommonThingsTest;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Stack;

import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

public class TokenStackTest extends CommonThingsTest {
    private SingleMatch match;
    @Test
    public void testDraw() throws WrongSettingException, LastRoundException {
        Player p = new Player("player1");
        setSummary(p, getCardMap(assignConfiguration("src/test/resources/PartialFreeConfiguration.json")));
        match = new SingleMatch(p);

        for(int i=0; i < 10; i++)
            assertTrue(match.getTokenStack().draw(match));

        for(int i=0; i < 100; i++)
            assertTrue(match.getTokenStack().getStack().size() > 0);
    }

    @Test
    public void testShuffle() throws WrongSettingException {
        Player p = new Player("player1");
        setSummary(p, getCardMap(assignConfiguration("src/test/resources/PartialFreeConfiguration.json")));
        match = new SingleMatch(p);

        Stack<Token> stack = match.getTokenStack().getStack();

        match.getTokenStack().shuffle();

        assertNotEquals(stack,match.getTokenStack().getStack());
    }
}
