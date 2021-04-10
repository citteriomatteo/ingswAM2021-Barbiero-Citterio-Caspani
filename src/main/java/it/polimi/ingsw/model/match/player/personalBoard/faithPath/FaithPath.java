package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.Comunicator;

import java.util.*;

public class FaithPath
{
    private int faithMarker;
    private List<Integer> popeTiles; //Tiles values -> 0: downside (not reached yet), 1: upside, 2:rejected.
    private List<Cell> faithPath;

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

    public int getPosition() { return faithMarker; }

    public List<Cell> getFaithPath() { return faithPath; }

    /*
    This method adds "steps" points, one by one.
    For every cell passed it checks for a vatican report: if a report is effectively called, it collapses
    vaticanReportCell to Cell.
    TO TEST
     */
    public boolean addFaithPoints(int steps, Comunicator comunicator) throws MatchEndedException, FaithPathCreationException
    {
        for(; steps>0; steps--)
        {
            faithMarker++;
            if(faithPath.get(faithMarker).vaticanReport(comunicator))
                cellCollapse(faithMarker);

            if(faithMarker==24)
                throw new MatchEndedException("A player reached the end of the path.");
        }
        return true;
    }

    //This method allows to change the state of each pope tile.
    //TESTED
    public boolean setPopeTile(int section, int value)
    {
        popeTiles.set(section, value);
        return true;
    }
    public List<Integer> getPopeTiles() { return popeTiles; }

    /*
    Casts a VaticanReportCell at position "pos" to a normal Cell, in order to 'disable'
    the report procedure for the next players who pass.
    TESTED
     */
    public boolean cellCollapse(int pos) throws FaithPathCreationException
    {
        faithPath.set(pos, new Cell(faithPath.get(pos).getWinPoints(), faithPath.get(pos).getReportSection()));
        return true;
    }

    /*
    Counts and sums points coming from the player's path placement and all the upside pope tiles (=1),
    considering that the points that a tile offers are always two points more than its position in the Array
                                                                            (PopeTiles[0] gives 2 points, ...).
    TESTED
     */
    public int getWinPoints() {
        int points = 0;
        points = getFaithPath().stream().map(x -> x.getWinPoints()).reduce(points, (x, y) -> x + y);
        for (int tile : popeTiles)
            points += (tile + 2) * ((popeTiles.get(tile) == 1) ? 1 : 0);
        return points;
    }
}