package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class FaithPathTest
{
    //Generates 25-steps path, same report sections as the default path, but with random winpoints.
    public ArrayList<Cell> generatePath() throws FaithPathCreationException
    {
        Random rnd = new Random();
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

    //TODO
    @Test
    public void getWinPointsTest() throws FaithPathCreationException, MatchEndedException
    {
        FaithPath path = new FaithPath(generatePath(),0);
        assertEquals(70, path.getWinPoints());

        //null MUST BE SUBSTITUTED WITH A COMUNICATOR WHEN IT'LL BE AVAILABLE!
        path.addFaithPoints(17,null);
        //assertEquals(Arrays.asList(1,1,0),path.getPopeTiles());
        //assertEquals(75, path.getWinPoints());
    }

    /*
    Tests the correct type-cast of a VaticanReportCell to a Cell, when the relative vaticanReport is triggered.
    */
    @Test
    public void CellCollapseTest() throws FaithPathCreationException, MatchEndedException
    {
        FaithPath path = new FaithPath(generatePath(), 0);
        assertTrue(path.getFaithPath().get(8).singleVaticanReport());
        path.addFaithPoints(9, null);
        assertFalse(path.getFaithPath().get(8).singleVaticanReport());
    }

    //TODO
    @Test
    public void addFaithPointsTest()
    {

    }

}
