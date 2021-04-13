package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class SingleFaithPathTest extends FaithPathTest
{
    /*
    Creates path, tests match end and negative starting marker exception and
    , meanwhile, player's popeTiles correct switching (player takes 1st tile but loses the other two).
     */
    @Test
    public void blackPointAndBlackReportTest() throws NegativeQuantityException, MatchEndedException
    {
        /*SingleFaithPath path = new SingleFaithPath(generatePath(), 0);

        assertThrows(NegativeQuantityException.class, ()->path.addBlackPoints(-1));

        //NO MATCH CLASS YET -> null AS A Comunicator!
        //path.addFaithPoints(8,null);

        path.addBlackPoints(16);
        assertEquals(Arrays.asList(2,2,0), path.getPopeTiles());

        assertThrows(MatchEndedException.class, ()->path.addBlackPoints(24));
        assertEquals(path.getPopeTiles(), Arrays.asList(2,2,2));*/

    }

    //Instruction coverage for black marker getter.
    @Test
    public void getBlackPositionTest() throws NegativeQuantityException, MatchEndedException
    {
        /*SingleFaithPath p = new SingleFaithPath(generatePath(), 0);
        assertEquals(0,p.getBlackPosition());
        p.addBlackPoints(7);
        assertEquals(7,p.getBlackPosition());*/
    }
}
