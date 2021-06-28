package it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.model.match.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class extends common faith path things with single player features.
 */
public class SingleFaithPath extends FaithPath {
    private int blackCrossMarker;

    /**
     * This is the constructor of the SinglePlayer FaithPath.
     * @param faithPath                   is the list of the Cells
     * @param faithMarker                 is the starting point of the player on the path
     */
    public SingleFaithPath(ArrayList<Cell>faithPath, int faithMarker) {
        super(faithPath, faithMarker);
        blackCrossMarker = 0;
    }

    /** @return Lorenzo's position on the path */
    public int getBlackPosition() { return blackCrossMarker; }

    /**
     * This method adds "steps" points, one by one. For every cell passed,
     * it checks for a vatican report: if a report is effectively called, it collapses vaticanReportCell to Cell.
     * @param points        represents the points that Lorenzo has done
     * @return              true
     * @throws LastRoundException          if Lorenzo ends the path journey
     */
    public boolean addBlackPoints(int points, Player player) throws LastRoundException {
        if(points<0)
        {
            System.err.println("Application shutdown due to an internal error.");
            System.exit(1);
        }
        for(; points>0; points--)
        {
            blackCrossMarker++;
            if(blackVaticanReport(player))
                cellCollapse(blackCrossMarker);

            if(blackCrossMarker==24)
                throw new LastRoundException("Lorenzo has reached the end of the path.");
        }
        return true;
    }

    /**
     * This method checks, if Lorenzo is in a report cell, how to turn the player's pope tile depending on its position.
     * @return true (for Cell collapse), else false.
     */
    public boolean blackVaticanReport(Player player) {
        if(getFaithPath().get(blackCrossMarker).singleVaticanReport(player))
        {
            List<Cell> fp = getFaithPath();
            if ((fp.get(getPosition()).getReportSection()>0 && fp.get(getPosition()).getReportSection() < fp.get(blackCrossMarker).getReportSection())
                || (fp.get(getPosition()).getReportSection()==0 && getPosition()<blackCrossMarker))
                setPopeTile(fp.get(blackCrossMarker).getReportSection()-1, 2);
            else
                setPopeTile(fp.get(blackCrossMarker).getReportSection()-1, 1);
            Map<String, List<Integer>> singlePopeMap = new HashMap<>();
            singlePopeMap.put(player.getNickname(), player.getPersonalBoard().getFaithPath().getPopeTiles());
            player.getSummary().updatePopeTiles(player.getNickname(), singlePopeMap);

            return true;
        }
        return false;
    }

}