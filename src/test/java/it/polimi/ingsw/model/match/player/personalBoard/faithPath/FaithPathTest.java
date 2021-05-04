package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.exceptions.FaithPathCreationException;
import it.polimi.ingsw.exceptions.MatchEndedException;
import it.polimi.ingsw.exceptions.SingleMatchException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class FaithPathTest
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
    public void getWinPointsTest() throws MatchEndedException, FileNotFoundException, WrongSettingException, SingleMatchException {
        Player player = new Player("player1");
        Player player1 = new Player("player2");
        Match match = new MultiMatch(List.of(player,player1),"src/test/resources/StandardConfiguration.json");
        FaithPath path = match.getCurrentPlayer().getPersonalBoard().getFaithPath();

        path.addFaithPoints(17,match);
        assertEquals(Arrays.asList(1,1,0),path.getPopeTiles());
        assertEquals(27, path.getWinPoints());
    }

    @Test
    public void addFaithPointsTest() throws FileNotFoundException, SingleMatchException, WrongSettingException, MatchEndedException
    {
        Player player = new Player("player1");
        Player player1 = new Player("player2");
        MultiMatch match = new MultiMatch(List.of(player,player1),"src/test/resources/StandardConfiguration.json");
        FaithPath path = match.getCurrentPlayer().getPersonalBoard().getFaithPath();
        path.addFaithPoints(9,match);
        assertEquals(path.getPosition(), 9);
    }

}
