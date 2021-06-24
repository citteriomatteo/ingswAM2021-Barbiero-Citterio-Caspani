package it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.gameLogic.exceptions.FaithPathCreationException;
import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.model.match.Communicator;

import java.util.*;

/**
 * This abstract class implements the common FaithPath things.
 */
public abstract class FaithPath
{
    private int faithMarker;
    private final List<Integer> popeTiles; //Tiles values -> 0: downside (not reached yet), 1: upside, 2:rejected.
    private final List<Cell> faithPath;

    /**
     * This is the constructor of the abstract FaithPath.
     * Shutdowns the application for a negative startingPos.
     * @param faithPath                   is the list of the Cells
     * @param faithMarker                 is the starting point of the player on the path
     */
    public FaithPath(ArrayList<Cell>faithPath, int faithMarker)
    {
        if(faithMarker<0 || faithMarker>1)
        {
            System.err.println("Error in faith path creation: invalid marker argument.");
            System.exit(1);
        }
        this.faithMarker = faithMarker;
        this.faithPath = faithPath;
        //pope tiles initialization:
        popeTiles = new ArrayList<>();
        popeTiles.add(0); popeTiles.add(0); popeTiles.add(0);
    }

    /** @return player's marker position */
    public int getPosition() { return faithMarker; }

    /** @return the faith Path */
    public List<Cell> getFaithPath() { return faithPath; }

    /**
     * This method adds "steps" points, one by one. For every cell passed,
     * it checks for a vatican report: if a report is effectively called, it collapses vaticanReportCell to Cell.
     * @param steps         is the number of steps to do
     * @param communicator   is the interface with all the usable methods
     * @return              true
     * @throws LastRoundException          if a player ends the path journey
     */
    public boolean addFaithPoints(int steps, Communicator communicator) throws LastRoundException
    {
        for(; steps>0 && faithMarker<faithPath.size()-1; steps--)
        {
            faithMarker++;
            if(communicator.getPlayers().size()==1 && faithPath.get(faithMarker).singleVaticanReport(communicator.getPlayers().get(0)))
                cellCollapse(faithMarker);
            else
                if(communicator.getPlayers().size()>1 && faithPath.get(faithMarker).vaticanReport(communicator))
                    cellCollapse(faithMarker);

            if(faithMarker==faithPath.size()-1)
                throw new LastRoundException("A player reached the end of the path.");
        }
        return true;
    }

    /**
     * This method allows to change the state of each pope tile.
     * @param section indicates, simultaneously, the report section and the tile to turn.
     * @param value   is the turn value
     * @return        true
     */
    public boolean setPopeTile(int section, int value)
    {
        popeTiles.set(section, value);
        return true;
    }

    /** @return the tiles list */
    public List<Integer> getPopeTiles() { return popeTiles; }

    /**
     * This method casts the VaticanReportCell at position "pos" to a normal Cell, in order to 'disable'
     * the Report procedure for the next players who pass.
     * @param pos is the position of the cell that has to be casted.
     * @return    true
     */
    public boolean cellCollapse(int pos)
    {
        try {
            faithPath.set(pos, new Cell(faithPath.get(pos).getWinPoints(), faithPath.get(pos).getReportSection()));
        }
        catch(FaithPathCreationException e) { e.printStackTrace(); System.err.println("Application shutdown due to an internal error."); }
        return true;
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
            setPopeTile(section - 1, 2);

        else
            setPopeTile(section - 1, 1);

        return true;
    }

    /**
     * Counts and sums points coming from the player's path placement and all the upside pope tiles (=1),
     * considering that the points that a tile offers are always two points more than its position in the Array
     *                                                                             (PopeTiles[0] gives 2 points, ...).
     * @return the obtained points
     */
    public int getWinPoints()
    {
        int points = 0;
        for(int i = faithMarker; i>=0 && points==0; i--)
            points = faithPath.get(i).getWinPoints();

        for (int i=0; i<3; i++)
                points += (i + 2) * ((popeTiles.get(i) == 1) ? 1 : 0);
        return points;
    }
}