package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.Comunicator;
import java.util.*;

public abstract class FaithPath
{
    private int faithMarker;
    private final List<Integer> popeTiles; //Tiles values -> 0: downside (not reached yet), 1: upside, 2:rejected.
    private final List<Cell> faithPath;

    /**
     * This is the constructor of the abstract FaithPath.
     * @param faithPath                   is the list of the Cells
     * @param faithMarker                 is the starting point of the player on the path
     * @throws FaithPathCreationException for faithMarker invalid values
     */
    public FaithPath(ArrayList<Cell>faithPath, int faithMarker) throws FaithPathCreationException
    {
        if(faithMarker<0 || faithMarker>1)
            throw new FaithPathCreationException ("Error in faith path creation: invalid marker argument.");
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
     * @param comunicator   is the interface with all the usable methods
     * @return              true
     * @throws MatchEndedException          if a player ends the path journey
     */
    public boolean addFaithPoints(int steps, Comunicator comunicator) throws MatchEndedException
    {
        for(; steps>0; steps--)
        {
            faithMarker++;
            if(comunicator.getPlayers().size()==1 && faithPath.get(faithMarker).singleVaticanReport())
                cellCollapse(faithMarker);
            else
                if(comunicator.getPlayers().size()>1 && faithPath.get(faithMarker).vaticanReport(comunicator))
                    cellCollapse(faithMarker);

            if(faithMarker==24)
                throw new MatchEndedException("A player reached the end of the path.");
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
     * This method casts a VaticanReportCell at position "pos" to a normal Cell, in order to 'disable'
     * the report procedure for the next players who pass.
     * @param pos is the position of the cell that has to be casted.
     * @return    true
     */
    public boolean cellCollapse(int pos)
    {
        try{faithPath.set(pos, new Cell(faithPath.get(pos).getWinPoints(), faithPath.get(pos).getReportSection()));}
        catch(FaithPathCreationException e) { e.printStackTrace(); System.err.println("Application shutdown due to an internal error."); }
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
        points = getFaithPath().stream().filter((x)->faithPath.indexOf(x)<=faithMarker).map(Cell::getWinPoints).reduce(points, Integer::sum);
        for (int i=0; i<3; i++)
                points += (i + 2) * ((popeTiles.get(i) == 1) ? 1 : 0);
        return points;
    }
}