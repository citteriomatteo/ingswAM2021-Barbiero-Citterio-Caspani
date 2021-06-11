package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.match.Communicator;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.PlayerSummary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class extends the normal Cell, adding vatican report features to it.
 */
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
            FaithPath path = p.getPersonalBoard().getFaithPath();
            path.externalVaticanReport(getReportSection());
        }

        //update_call
        Map<String, List<Integer>> popeMap = new HashMap<>();
        for(Player p : communicator.getPlayers())
            popeMap.put(p.getNickname(), p.getPersonalBoard().getFaithPath().getPopeTiles());
        communicator.getPlayers().get(0).updatePopeTiles(communicator.getPlayers().get(0).getNickname(), popeMap);

        return true;
    }

    /**
     * This method specifies, in a Singleplayer match, that this Cell is a VaticanReportCell.
     * @return true
     * @see Cell
     */
    @Override
    public boolean singleVaticanReport(Player player) {
        player.getPersonalBoard().getFaithPath().externalVaticanReport(getReportSection());

        //update_call
        Map<String, List<Integer>> popeMap = new HashMap<>();
        popeMap.put(player.getNickname(), player.getPersonalBoard().getFaithPath().getPopeTiles());
        player.updatePopeTiles(player.getNickname(), popeMap);

        return true;
    }

    /** @return true if it's a vatican report cell, else false */
    @Override
    public boolean isVatican() { return true; }
}