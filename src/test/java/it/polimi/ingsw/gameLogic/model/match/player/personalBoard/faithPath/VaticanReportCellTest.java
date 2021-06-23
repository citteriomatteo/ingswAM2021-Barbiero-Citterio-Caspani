package it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.CommonThingsTest;
import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.gameLogic.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

public class VaticanReportCellTest extends CommonThingsTest {

    @Test
    public void generalInstructionCoverageTest() throws WrongSettingException, LastRoundException {
        muteOutput();

        Player player = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new SingleMatch(player);

        SingleFaithPath path = (SingleFaithPath) match.getCurrentPlayer().getPersonalBoard().getFaithPath();
        for(int i=0; i<24; i++)
            System.out.println(path.getFaithPath().get(i).singleVaticanReport(player));
        assertTrue(path.getFaithPath().get(8).singleVaticanReport(player));
        path.addFaithPoints(9, match);
        System.out.println("AFTER:");
        for(int i=0; i<25; i++)
            System.out.println(path.getFaithPath().get(i).singleVaticanReport(player));
        assertFalse(path.getFaithPath().get(8).singleVaticanReport(player));
    }

}
