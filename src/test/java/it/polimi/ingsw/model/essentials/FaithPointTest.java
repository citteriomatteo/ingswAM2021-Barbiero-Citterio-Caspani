package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class FaithPointTest {
    private Player player = new Player("player1");

    @Test
    public void testCreate() {

        assertThrows(InvalidQuantityException.class, ()-> new FaithPoint(-2));
    }

    @Test
    public void testAdd() throws NegativeQuantityException, MatchEndedException, FileNotFoundException, WrongSettingException {
        Match match = new SingleMatch(player,"src/test/resources/StandardConfiguration.json");
        FaithPoint faithPoint = new FaithPoint(2);

        assertTrue(faithPoint.add(player));
        assertEquals(2,match.getCurrentPlayer().getPersonalBoard().getFaithPath().getPosition());
    }

}
