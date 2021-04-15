package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;

import java.util.ArrayList;
import java.util.List;

public class SingleFaithPath extends FaithPath
{
    private int blackCrossMarker;

    /**
     * This is the constructor of the Singleplayer FaithPath.
     * @param faithPath                   is the list of the Cells
     * @param faithMarker                 is the starting point of the player on the path
     * @throws FaithPathCreationException (propagated)
     */
    public SingleFaithPath(ArrayList<Cell>faithPath, int faithMarker) throws FaithPathCreationException
    {
        super(faithPath, faithMarker);
        blackCrossMarker = 0;
    }

    /** @return Lorenzo's position on the path */
    public int getBlackPosition() { return blackCrossMarker; }

    /**
     * This method adds "steps" points, one by one. For every cell passed,
     * it checks for a vatican report: if a report is effectively called, it collapses vaticanReportCell to Cell.
     * @param points        represents the points that Lorenzo has done
     * @return              true
     * @throws MatchEndedException          if Lorenzo ends the path journey
     */
    public boolean addBlackPoints(int points) throws MatchEndedException
    {
        //critical error: wrong tokens!
        if(points<0)
        {
            System.err.println("Application shutdown due to an internal error.");
            System.exit(1);
        }
        for(; points>0; points--)
        {
            blackCrossMarker++;
            if(blackVaticanReport())
                cellCollapse(blackCrossMarker);

            if(blackCrossMarker==24)
                throw new MatchEndedException("Lorenzo has reached the end of the path.");
        }
        return true;
    }

    /**
     * This method checks, if Lorenzo is in a report cell, how to turn the player's pope tile depending on its position.
     * @return true (for Cell collapse), else false.
     */
    public boolean blackVaticanReport()
    {
        if(getFaithPath().get(blackCrossMarker).singleVaticanReport())
        {
            List<Cell> fp = getFaithPath();
            if ((fp.get(getPosition()).getReportSection()>0 && fp.get(getPosition()).getReportSection() < fp.get(blackCrossMarker).getReportSection())
                || (fp.get(getPosition()).getReportSection()==0 && getPosition()<blackCrossMarker))
                setPopeTile(fp.get(blackCrossMarker).getReportSection()-1, 2);
            else
                setPopeTile(fp.get(blackCrossMarker).getReportSection()-1, 1);
            return true;
        }
        return false;
    }


}