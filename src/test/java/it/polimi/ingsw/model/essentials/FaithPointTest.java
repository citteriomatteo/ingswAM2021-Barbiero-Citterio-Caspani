package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.exceptions.InvalidQuantityException;
import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.WrongSettingException;

import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.match.*;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.observer.ModelObserver;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class FaithPointTest extends CommonThingsTest {
    private Player player = new Player("player1");

    @Test
    public void testCreate() {

        assertThrows(InvalidQuantityException.class, ()-> new FaithPoint(-2));
    }

    @Test
    public void testAdd() throws NegativeQuantityException, LastRoundException, WrongSettingException {
        setSummary(player, getCardMap(assignConfiguration("src/test/resources/PartialFreeConfiguration.json")));
        Match match = new SingleMatch(player);

        FaithPoint faithPoint = new FaithPoint(2);

        assertTrue(faithPoint.add(player));
        assertEquals(2,match.getCurrentPlayer().getPersonalBoard().getFaithPath().getPosition());
    }

}
