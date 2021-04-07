package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

public class FaithPointTest {
    private Player player = new Player();

    @Test
    public void testCreate() throws InvalidAddFaithException {

        assertThrows(InvalidAddFaithException.class, ()-> new FaithPoint(-2));
    }

    @Test
    public void testAdd() throws InvalidAddFaithException {
        FaithPoint faithPoint = new FaithPoint(2);

        assertTrue(faithPoint.add(player));
    }
}
