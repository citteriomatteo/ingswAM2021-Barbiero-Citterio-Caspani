package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MatchConfiguration;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.Summary;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.observer.ModelObserver;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SingleFaithPathTest extends FaithPathTest
{


    /*
    Creates path, tests match end and negative starting marker exception and
    , meanwhile, player's popeTiles correct switching when blackReport is called.
     */
    @Test
    public void blackPointAndBlackReportTest() throws LastRoundException, WrongSettingException
    {
        Player player = new Player("player1");
        setSummary(player);
        Match match = new SingleMatch(player);

        SingleFaithPath path = (SingleFaithPath) match.getCurrentPlayer().getPersonalBoard().getFaithPath();
        path.addBlackPoints(match.getCurrentPlayer(),10);
        assertEquals(10, path.getBlackPosition()); assertEquals(0, path.getPosition());
        assertEquals(2, path.getPopeTiles().get(0));
        assertFalse(path.getFaithPath().get(9).singleVaticanReport());
    }

    //Instruction coverage for black marker getter.
    @Test
    public void getBlackPositionTest() throws WrongSettingException
    {
        Player player = new Player("player1");
        setSummary(player);
        Match match = new SingleMatch(player);

        FaithPath path = match.getCurrentPlayer().getPersonalBoard().getFaithPath();
    }
}
