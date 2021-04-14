package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class SingleMatchTest {
    private SingleMatch match;

    @Test
    public void testNextTurn() throws WrongSettingException, FileNotFoundException, MatchEndedException {
        int dim;
        match = new SingleMatch(new Player("player1"),"src/test/resources/StandardConfiguration.json");

        dim = match.getTokenStack().getStack().size();

        match.nextTurn();

        assertEquals(match.getTokenStack().getStack().size(),dim-1);
    }
}
