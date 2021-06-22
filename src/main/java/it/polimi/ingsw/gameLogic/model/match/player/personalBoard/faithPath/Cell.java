package it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.gameLogic.exceptions.FaithPathCreationException;
import it.polimi.ingsw.gameLogic.model.match.Communicator;
import it.polimi.ingsw.gameLogic.model.match.player.Player;

/**
 * This class implements a cell.
 */
public class Cell
{
    private final int winPoints;
    private final int vaticanReportSection;

    /**
     * The constructor initializes the single basic Cell.
     * @param winPoints             sets the win points of the cell
     * @param vaticanReportSection  defines in which report section the Cell is (=0 if it's not in a section).
     * @throws FaithPathCreationException when a negative quantity of winPoints is received.
     */
    public Cell(int winPoints, int vaticanReportSection) throws FaithPathCreationException
    {
        if(winPoints <0)
            throw new FaithPathCreationException("Error in faith path creation: negative winPoints on a Cell.");
        this.winPoints = winPoints;
        this.vaticanReportSection = vaticanReportSection;
    }

    /**
     * This method specifies, in a Multiplayer match, that this Cell is not a VaticanReportCell.
     * @param communicator has no use in this method
     * @return            false
     * @see VaticanReportCell
     */
    public boolean vaticanReport(Communicator communicator) {return false;}

    /**
     * This method specifies, in a Singleplayer match, that this Cell is not a VaticanReportCell.
     * @return            false
     * @see VaticanReportCell
     */
    public boolean singleVaticanReport(Player player) { return false; }

    /** @return the vatican report section associated to this Cell */
    public int getReportSection() { return vaticanReportSection; }

    /** @return the winPoints value of the Cell */
    public int getWinPoints() { return winPoints; }

    /** @return true if it's a vatican report cell, else false */
    public boolean isVatican() { return false; }
}
