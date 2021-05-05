package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import static org.junit.jupiter.api.Assertions.*;

public class SingleFaithPathTest extends FaithPathTest
{
    /*
    Creates path, tests match end and negative starting marker exception and
    , meanwhile, player's popeTiles correct switching when blackReport is called.
     */
    @Test
    public void blackPointAndBlackReportTest() throws LastRoundException, FileNotFoundException, WrongSettingException
    {
        Player player = new Player("player1");
        Match match = new SingleMatch(player,"src/test/resources/StandardConfiguration.json");
        SingleFaithPath path = (SingleFaithPath) match.getCurrentPlayer().getPersonalBoard().getFaithPath();
        path.addBlackPoints(10);
        assertEquals(10, path.getBlackPosition()); assertEquals(0, path.getPosition());
        assertEquals(2, path.getPopeTiles().get(0));
        assertFalse(path.getFaithPath().get(9).singleVaticanReport());
    }

    //Instruction coverage for black marker getter.
    @Test
    public void getBlackPositionTest() throws FileNotFoundException, WrongSettingException
    {
        Player player = new Player("player1");
        Match match = new SingleMatch(player,"src/test/resources/StandardConfiguration.json");
        FaithPath path = match.getCurrentPlayer().getPersonalBoard().getFaithPath();
    }
}
