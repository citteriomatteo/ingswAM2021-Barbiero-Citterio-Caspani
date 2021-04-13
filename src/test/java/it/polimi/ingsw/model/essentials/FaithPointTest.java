package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

public class FaithPointTest {
    private Player player = new Player("player1");

    public FaithPointTest() throws NegativeQuantityException {
    }

    @Test
    public void testCreate() {

        assertThrows(InvalidAddFaithException.class, ()-> new FaithPoint(-2));
    }

    @Test
    public void testAdd() throws InvalidAddFaithException, NegativeQuantityException, MatchEndedException, FileNotFoundException, WrongSettingException, SingleMatchException {
        Match match = new SingleMatch(player,"src/test/resources/StandardConfiguration.json");
        FaithPoint faithPoint = new FaithPoint(2);

        assertTrue(faithPoint.add(player));
    }
}
