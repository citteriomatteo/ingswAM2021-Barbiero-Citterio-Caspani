package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;

import java.util.ArrayList;

public class MultiFaithPath extends FaithPath
{
    /**
     * This is the constructor of the Multiplayer FaithPath.
     * @param faithPath                   is the list of the Cells
     * @param faithMarker                 is the starting point of the player on the path
     * @throws FaithPathCreationException (propagated)
     */
    public MultiFaithPath(ArrayList<Cell>faithPath, int faithMarker)
    {
        super(faithPath, faithMarker);
    }

    /**
     * This method is called when a player steps onto a VaticanReportCell.
     * It checks if the current player is in the same report section of the report Cell or before and turns the
     * relative pope tile upside or downside.
     * @param section   the stepped report section
     * @return          true
     * @see VaticanReportCell
     */
    public boolean externalVaticanReport(int section)
    {
        if(getFaithPath().get(getPosition()).getReportSection()<section)
            setPopeTile(section-1,2);
        else
            setPopeTile(section-1, 1);
        return true;
    }
}