package it.polimi.ingsw.gameLogic.model.observer;

import it.polimi.ingsw.gameLogic.controller.StateName;
import it.polimi.ingsw.gameLogic.model.essentials.DevelopmentCard;
import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.Production;
import it.polimi.ingsw.gameLogic.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.gameLogic.model.match.CardGrid;
import it.polimi.ingsw.gameLogic.model.match.market.Market;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.DevCardSlots;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.DiscountMap;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.warehouse.Warehouse;

import java.util.List;
import java.util.Map;

/**
 * This interface implements the Observer-side of the paradigm.
 * The class that implements it, has to redefine each method adding Summary-update procedures.
 */
public interface ModelObserver {

    /**
     * This method, when called, updates the market in the summary.
     * @param market the market
     */
    void updateMarket(Market market);
    /**
     * This method, when called, updates the card grid in the summary.
     * It inserts into every slot the id of the top card and the depth of the
     * stack (got from cardGrid.getGrid()[i][j].size()).
     * @param cardGrid the card grid
     */
    void updateCardGrid(CardGrid cardGrid);
    /**
     * This method, when called, updates the lorenzo marker (if single-player) in the summary.
     * @param lorenzoMarker the black marker
     */
    void updateLorenzoMarker(int lorenzoMarker);
    /**
     * This method, when called, updates the faith path in the summary.
     * @param faithPath the faith path
     */
    void updateFaithPath(List<Cell> faithPath);

    /**
     * This method, when called, updates the personal board of the requested player in the summary.
     * @param nickname  the requested player
     * @param connected the new state
     */
    void updateConnectionState(String nickname, boolean connected);
    /**

    /**
     * This method, when called, updates the market buffer of the requested player in the summary.
     * @param nickname  the requested player
     * @param warehouse the warehouse
     */
    void updateMarketBuffer(String nickname, Warehouse warehouse);
    /**
     * This method, when called, updates the warehouse of the requested player in the summary.
     * @param nickname  the requested player
     * @param warehouse the warehouse
     */
    void updateWarehouse(String nickname, Warehouse warehouse);
    /**
     * This method, when called, updates the strongbox of the requested player in the summary.
     * @param nickname  the requested player
     * @param strongbox the strongbox
     */
    void updateStrongbox(String nickname, StrongBox strongbox);
    /**
     * This method, when called, updates the faith marker of the requested player in the summary.
     * @param nickname  the requested player
     * @param faithMarker the faith marker
     */
    void updateFaithMarker(String nickname, int faithMarker);
    /**
     * This method, when called, updates the pope tiles' state of the requested player in the summary.
     * @param nickname  the requested player
     * @param popeTiles the pope tiles array
     */
    void updatePopeTiles(String nickname, Map<String, List<Integer>> popeTiles);
    /**
     * This method, when called, updates the dev card slots of the requested player in the summary.
     * @param nickname  the requested player
     * @param devCardSlots the dev card slots array
     */
    void updateDevCardSlots(String nickname, DevCardSlots devCardSlots);
    /**
     * This method, when called, updates the hand leaders' state of the requested player in the summary.
     * Used for the starting choice of leaders (this will be sent ONLY to the interested player).
     * @param nickname  the requested player
     * @param handLeaders the hand leaders
     */
    void updateHandLeaders(String nickname, List<LeaderCard> handLeaders);
    /**
     This method, when called, updates the hand leaders' state of the requested player in the summary.
     @param nickname  the requested player
     @param handLeader the hand leader discarded
     */
    void updateHandLeadersDiscard(String nickname, LeaderCard handLeader);
    /**
     * This method, when called, updates the active leaders' state of the requested player in the summary.
     * @param nickname  the requested player
     * @param activeLeader the activated leader
     */
    void updateActiveLeaders(String nickname, LeaderCard activeLeader);
    /**
     * This update method receives the new conversion to add in the list of the others.
     * @param nickname              the player who activated the conversion leader
     * @param whiteMarbleConversion the related conversion
     */
    void updateWhiteMarbleConversions(String nickname, PhysicalResource whiteMarbleConversion);
    /**
     * This method, when called, updates the discount map of the requested player in the summary.
     * @param nickname  the requested player
     * @param discountMap the discount map
     */
    void updateDiscountMap(String nickname, DiscountMap discountMap);
    /**
     * This method, when called, updates the temporary dev card of the requested player in the summary.
     * @param nickname  the requested player
     * @param tempDevCard the temporary dev card
     */
    void updateTempDevCard(String nickname, DevelopmentCard tempDevCard);
    /**
     * This method, when called, updates the temporary production of the requested player in the summary.
     * @param nickname  the requested player
     * @param tempProduction the temporary production
     */
    void updateTempProduction(String nickname, Production tempProduction);
    /**
     * This method, when called, updates the last used state in this player's summary, useful for disconnection
     * in the middle of a complex operation ( such as Dev card buy, production, etc. ).
     * @param lastUsedState the last used state
     */
    void updateLastUsedState(String nickname, StateName lastUsedState);

}
