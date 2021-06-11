package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.exceptions.FaithPathCreationException;

import java.util.ArrayList;

/**
 * This class extends the common faith path things with multiplayer features.
 */
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

}