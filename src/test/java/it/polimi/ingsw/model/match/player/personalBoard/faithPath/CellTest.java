package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.match.Comunicator;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CellTest
{
    /*
    Creates a new cell and calls every available function, looking for a full instruction coverage.
    MISSING VATICANREPORT INSTRUCTION COVERAGE: NO COMUNICATOR YET!
     */
    @Test
    public void basicCellOperationsTest() throws FaithPathCreationException
    {
        Random rnd = new Random();
        int wp = rnd.nextInt(50), vrs = rnd.nextInt(50);
        Cell cell = new Cell(wp, vrs);
        assertEquals(cell.getReportSection(), vrs);
        assertEquals(cell.getWinPoints(), wp);
    }

    //Tries creation of a Cell with negative win points.
   @Test
   public void invalidCellTest()
   {
       assertThrows(FaithPathCreationException.class, ()->new Cell(-1, 0));
   }
}
