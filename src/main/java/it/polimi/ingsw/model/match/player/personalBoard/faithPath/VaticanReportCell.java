package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.match.Communicator;
import it.polimi.ingsw.model.match.player.Player;

public class VaticanReportCell extends Cell
{
    /**
     * This constructor calls the super-class method.
     * @param winPoints            defines the winpoints of the cell
     * @param vaticanReportSection defines the number of the report section the Cell is in.
     * @throws FaithPathCreationException (propagated)
     */
    public VaticanReportCell(int winPoints, int vaticanReportSection) throws FaithPathCreationException
    {
        super(winPoints, vaticanReportSection);
    }

    /**
     * This method, when called, causes an externalVaticanReport() invocation on every player's multiFaithPath.
     * @param communicator is the interface responsible of the available functions to call
     * @return true
     */
    @Override
    public boolean vaticanReport(Communicator communicator)
    {
        for(Player p : communicator.getPlayers())
        {
            MultiFaithPath path = (MultiFaithPath) p.getPersonalBoard().getFaithPath();
            path.externalVaticanReport(getReportSection());
        }
        return true;
    }

    /**
     * This method specifies, in a Singleplayer match, that this Cell is a VaticanReportCell.
     * @return            true
     * @see Cell
     */
    @Override
    public boolean singleVaticanReport() {return true;}
}