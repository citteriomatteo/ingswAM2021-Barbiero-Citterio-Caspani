package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.exceptions.InvalidQuantityException;
import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.CommonThingsTest;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MatchConfiguration;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

public class FaithPointTest extends CommonThingsTest {
    private Player player = new Player("player1");

    @Test
    public void testCreate() {

        assertThrows(InvalidQuantityException.class, ()-> new FaithPoint(-2));
    }

    @Test
    public void testAdd() throws NegativeQuantityException, LastRoundException, WrongSettingException {
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new SingleMatch(player);

        FaithPoint faithPoint = new FaithPoint(2);

        assertTrue(faithPoint.add(player));
        assertEquals(2,match.getCurrentPlayer().getPersonalBoard().getFaithPath().getPosition());
    }

}
