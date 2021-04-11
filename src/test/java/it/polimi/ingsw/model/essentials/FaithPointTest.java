package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

public class FaithPointTest {
    private Player player = new Player("player1",null);

    public FaithPointTest() throws NegativeQuantityException {
    }

    @Test
    public void testCreate() {

        assertThrows(InvalidAddFaithException.class, ()-> new FaithPoint(-2));
    }

    @Test
    public void testAdd() throws InvalidAddFaithException, FaithPathCreationException, MatchEndedException {
        FaithPoint faithPoint = new FaithPoint(2);

        assertTrue(faithPoint.add(player));
    }
}
