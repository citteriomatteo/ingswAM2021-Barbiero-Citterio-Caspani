package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class TokenStackTest {
    private SingleMatch match;
    @Test
    public void testDraw() throws FileNotFoundException, WrongSettingException, LastRoundException {
        match = new SingleMatch(new Player("player1"),"src/test/resources/StandardConfiguration.json");

        for(int i=0; i < 10; i++)
            assertTrue(match.getTokenStack().draw(match));

        for(int i=0; i < 100; i++)
            assertTrue(match.getTokenStack().getStack().size() > 0);
    }

    @Test
    public void testShuffle() throws WrongSettingException, FileNotFoundException {
        match = new SingleMatch(new Player("player1"),"src/test/resources/StandardConfiguration.json");

        Stack<Token> stack = match.getTokenStack().getStack();

        match.getTokenStack().shuffle();

        assertNotEquals(stack,match.getTokenStack().getStack());
    }
}
