package it.polimi.ingsw.model.match;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.token.PlusOneShuffleToken;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class SingleMatchTest {
    private SingleMatch match;

    @Test
    public void testNextTurn() throws WrongSettingException, LastRoundException {
        int dim;
        match = new SingleMatch(new Player("player1"));


        dim = match.getTokenStack().getStack().size();

        if (match.getTokenStack().getStack().get(dim-1) instanceof PlusOneShuffleToken) {
            match.nextTurn();

            assertEquals(match.getTokenStack().getStack().size(), 7);
        } else {
            match.nextTurn();

            assertEquals(match.getTokenStack().getStack().size(), dim - 1);

        }
    }

    @Test
    public void testGetPlayer() throws WrongSettingException {
        Player player = new Player("player1");
        match = new SingleMatch(player);

        assertEquals(player,match.getPlayer("player1"));
        assertNull(match.getPlayer("player2"));

    }
}
