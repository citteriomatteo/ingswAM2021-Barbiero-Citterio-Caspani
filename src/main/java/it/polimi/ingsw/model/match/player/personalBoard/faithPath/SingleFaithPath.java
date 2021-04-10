package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;

import java.util.ArrayList;

public class SingleFaithPath extends FaithPath
{
    private int blackCrossMarker;

    public SingleFaithPath(ArrayList<Cell>faithPath, int faithMarker) throws FaithPathCreationException
    {
        super(faithPath, faithMarker);
        blackCrossMarker = 0;
    }

    //TESTED
    public int getBlackPosition() { return blackCrossMarker; }

    //TESTED
    public boolean addBlackPoints(int points) throws MatchEndedException, NegativeQuantityException
    {
        if(points<0)
            throw new NegativeQuantityException ("Error in Lorenzo's points: negative argument.");

        for(; points>0; points--)
        {
            blackCrossMarker++;
            if(blackVaticanReport())
                cellCollapse(blackCrossMarker);

            if(blackCrossMarker==24)
                throw new MatchEndedException("A player reached the end of the path.");
        }
        return true;
    }

    //This method checks, if Lorenzo is in a report cell, how to turn the player's pope tile depending on its position.
    //Then returns true (for Cell collapse), else returns false.
    //TESTED
    public boolean blackVaticanReport()
    {
        if(getFaithPath().get(blackCrossMarker).singleVaticanReport())
        {
            if (getFaithPath().get(getPosition()).getReportSection() < getFaithPath().get(blackCrossMarker).getReportSection())
                setPopeTile(getFaithPath().get(blackCrossMarker).getReportSection()-1, 2);
            else
                setPopeTile(getFaithPath().get(blackCrossMarker).getReportSection()-1, 1);
            return true;
        }
        return false;
    }


}