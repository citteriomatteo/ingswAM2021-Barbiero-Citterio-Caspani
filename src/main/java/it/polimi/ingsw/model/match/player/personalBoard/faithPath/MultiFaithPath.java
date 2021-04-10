package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;

import java.util.ArrayList;

public class MultiFaithPath extends FaithPath
{
    public MultiFaithPath(ArrayList<Cell>faithPath, int faithMarker) throws FaithPathCreationException
    {
        super(faithPath, faithMarker);
    }

    public boolean externalVaticanReport(int section)
    {
        if(getFaithPath().get(getPosition()).getReportSection()<section)
            setPopeTile(section-1,2);
        else
            setPopeTile(section-1, 1);
        return true;
    }
}