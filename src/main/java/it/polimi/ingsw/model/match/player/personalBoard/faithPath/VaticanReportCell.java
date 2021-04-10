package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.match.Comunicator;
import it.polimi.ingsw.model.match.player.Player;

public class VaticanReportCell extends Cell
{
    public VaticanReportCell(int winpoints, int vaticanReportSection) throws FaithPathCreationException
    {
        super(winpoints, vaticanReportSection);
    }

    //TODO
    @Override
    public boolean vaticanReport(Comunicator comunicator)
    {
        /*
        for(Player p : comunicator.getPlayers())
            p.getPersonalBoard().getFaithPath().externalVaticanReport(getReportSection());
         */

        return true;
    }

    @Override
    public boolean singleVaticanReport() {return true;}

}
