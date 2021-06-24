package it.polimi.ingsw.gameLogic.model.match.market;

import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.CommonThingsTest;
import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class RedMarbleTest extends CommonThingsTest {
    private Player player = new Player("player1");
    private Match match;

    @Test
    public void testOnDraw() throws NegativeQuantityException, LastRoundException, FileNotFoundException, WrongSettingException {
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(),matchConfiguration.getBasicProduction());
        match = new SingleMatch(player);

        Marble redMarble = new RedMarble();
        Assertions.assertFalse(redMarble.onDraw(this.player));

        Assertions.assertEquals(1,match.getCurrentPlayer().getPersonalBoard().getFaithPath().getPosition());
    }
}
