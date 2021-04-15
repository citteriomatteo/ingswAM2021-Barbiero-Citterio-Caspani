package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.token.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class SingleMatchTest {

    @Test
    public void testNextTurn() throws WrongSettingException, FileNotFoundException, MatchEndedException {
        int dim;
        SingleMatch match = new SingleMatch(new Player("player1"), "src/test/resources/StandardConfiguration.json");


        dim = match.getTokenStack().getStack().size();

        if (match.getTokenStack().getStack().get(dim-1) instanceof PlusOneShuffleToken) {
            match.nextTurn();

            assertEquals(match.getTokenStack().getStack().size(), 7);
        } else {
            match.nextTurn();

            assertEquals(match.getTokenStack().getStack().size(), dim - 1);

        }
    }
}
