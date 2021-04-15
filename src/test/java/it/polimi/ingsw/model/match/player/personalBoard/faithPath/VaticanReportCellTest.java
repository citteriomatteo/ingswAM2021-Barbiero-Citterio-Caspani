package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

public class VaticanReportCellTest
{
    @Test
    public void generalInstructionCoverageTest() throws FileNotFoundException, WrongSettingException, MatchEndedException
    {
        Player player = new Player("player1");
        Match match = new SingleMatch(player,"src/test/resources/StandardConfiguration.json");
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
