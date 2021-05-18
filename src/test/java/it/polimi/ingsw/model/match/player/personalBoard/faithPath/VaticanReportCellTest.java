package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.CommonThingsTest;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MatchConfiguration;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

public class VaticanReportCellTest extends CommonThingsTest
{
    @Test
    public void generalInstructionCoverageTest() throws WrongSettingException, LastRoundException
    {
        Player player = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath());
        Match match = new SingleMatch(player);

        SingleFaithPath path = (SingleFaithPath) match.getCurrentPlayer().getPersonalBoard().getFaithPath();
        for(int i=0; i<24; i++)
            System.out.println(path.getFaithPath().get(i).singleVaticanReport());
        assertTrue(path.getFaithPath().get(8).singleVaticanReport());
        path.addFaithPoints(9, match);
        System.out.println("AFTER:");
        for(int i=0; i<25; i++)
            System.out.println(path.getFaithPath().get(i).singleVaticanReport());
        assertFalse(path.getFaithPath().get(8).singleVaticanReport());
    }
}
