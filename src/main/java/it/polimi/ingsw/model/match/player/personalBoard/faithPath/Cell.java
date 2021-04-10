package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.match.Comunicator;

public class Cell
{
    private int winpoints;
    private int vaticanReportSection;

    public Cell(int winpoints, int vaticanReportSection) throws FaithPathCreationException
    {
        if(winpoints<0)
            throw new FaithPathCreationException("Error in faith path creation: negative winpoints on a Cell.");
        this.winpoints = winpoints;
        this.vaticanReportSection = vaticanReportSection;
    }

    public boolean vaticanReport(Comunicator comunicator) {return false;}

    public boolean singleVaticanReport() { return false; }

    public int getReportSection() { return vaticanReportSection; }

    public int getWinPoints() {return winpoints;}

}
