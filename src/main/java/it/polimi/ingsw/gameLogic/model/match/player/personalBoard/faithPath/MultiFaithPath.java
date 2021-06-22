package it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath;

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
     */
    public MultiFaithPath(ArrayList<Cell>faithPath, int faithMarker)
    {
        super(faithPath, faithMarker);
    }

}