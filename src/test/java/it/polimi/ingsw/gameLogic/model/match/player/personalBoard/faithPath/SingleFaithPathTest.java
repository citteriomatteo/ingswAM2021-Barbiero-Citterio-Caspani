package it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SingleFaithPathTest extends FaithPathTest {

    /*
    Creates path, tests match end and negative starting marker exception and
    , meanwhile, player's popeTiles correct switching when blackReport is called.
     */
    @Test
    public void blackPointAndBlackReportTest() throws LastRoundException, WrongSettingException {
        Player player = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new SingleMatch(player);

        SingleFaithPath path = (SingleFaithPath) match.getCurrentPlayer().getPersonalBoard().getFaithPath();
        path.addBlackPoints(10, player);
        assertEquals(10, path.getBlackPosition()); assertEquals(0, path.getPosition());
        assertEquals(2, path.getPopeTiles().get(0));
        assertFalse(path.getFaithPath().get(9).singleVaticanReport(player));
    }

    //Instruction coverage for black marker getter.
    @Test
    public void getBlackPositionTest() throws WrongSettingException {
        Player player = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new SingleMatch(player);

        FaithPath path = match.getCurrentPlayer().getPersonalBoard().getFaithPath();
    }
}
