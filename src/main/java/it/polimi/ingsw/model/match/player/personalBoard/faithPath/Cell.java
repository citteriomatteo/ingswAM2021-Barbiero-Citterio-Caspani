package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.match.Comunicator;

public class Cell
{
    private int winPoints;
    private int vaticanReportSection;

    public Cell(int winPoints, int vaticanReportSection) throws FaithPathCreationException
    {
        if(winPoints <0)
            throw new FaithPathCreationException("Error in faith path creation: negative winPoints on a Cell.");
        this.winPoints = winPoints;
        this.vaticanReportSection = vaticanReportSection;
    }

    public boolean vaticanReport(Comunicator comunicator) {return false;}

    public boolean singleVaticanReport() { return false; }

    public int getReportSection() { return vaticanReportSection; }

    public int getWinPoints() {return winPoints;}

}
