package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.CommonThingsTest;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;

public class RedMarbleTest extends CommonThingsTest {
    private Player player = new Player("player1");
    private Match match;

    @Test
    public void testOnDraw() throws NegativeQuantityException, LastRoundException, FileNotFoundException, WrongSettingException {
        setSummary(player, getCardMap(assignConfiguration("src/test/resources/PartialFreeConfiguration.json")));
        match = new SingleMatch(player);

        Marble redMarble = new RedMarble();
        Assertions.assertFalse(redMarble.onDraw(this.player));

        Assertions.assertEquals(1,match.getCurrentPlayer().getPersonalBoard().getFaithPath().getPosition());
    }
}
