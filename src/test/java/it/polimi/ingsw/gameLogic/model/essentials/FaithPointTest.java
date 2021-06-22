package it.polimi.ingsw.gameLogic.model.essentials;

import it.polimi.ingsw.gameLogic.exceptions.InvalidQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.CommonThingsTest;
import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.gameLogic.model.match.MatchConfiguration.assignConfiguration;
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
