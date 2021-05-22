package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.exceptions.FaithPathCreationException;
import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.SingleMatchException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.CommonThingsTest;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MatchConfiguration;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.*;

import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

public class FaithPathTest extends CommonThingsTest
{
    //Generates 25-steps path, same report sections as the default path.
    public static ArrayList<Cell> generatePath() throws FaithPathCreationException
    {
        ArrayList<Cell> path = new ArrayList<>();
        List<Integer> wp = Arrays.asList(0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,20);
        List<Integer> vrs = Arrays.asList(0,0,0,0,0,1,1,1,1,0,0,0,2,2,2,2,2,0,0,3,3,3,3,3,3);
        for(int i=0; i<25; i++)
            if(i==8 || i==16 || i==24)
                path.add(new VaticanReportCell(wp.get(i),vrs.get(i)));
            else
                path.add(new Cell(wp.get(i) ,vrs.get(i)));

        return path;
    }

    @Test
    public void getWinPointsTest() throws LastRoundException, WrongSettingException, SingleMatchException {
        Player player = new Player("player1");
        Player player1 = new Player("player2");
        List<Player> players = new ArrayList<>();
        players.add(player); players.add(player1);
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummaries(players, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new MultiMatch(players);
        FaithPath path = match.getCurrentPlayer().getPersonalBoard().getFaithPath();

        path.addFaithPoints(17,match);
        assertEquals(Arrays.asList(1,1,0),path.getPopeTiles());
        assertEquals(27, path.getWinPoints());
    }

    @Test
    public void addFaithPointsTest() throws SingleMatchException, WrongSettingException, LastRoundException
    {
        Player player = new Player("player1");
        Player player1 = new Player("player2");
        List<Player> players = new ArrayList<>();
        players.add(player); players.add(player1);
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummaries(players, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        MultiMatch match = new MultiMatch(players);

        FaithPath path = match.getCurrentPlayer().getPersonalBoard().getFaithPath();
        path.addFaithPoints(9,match);
        assertEquals(path.getPosition(), 9);
    }

}
