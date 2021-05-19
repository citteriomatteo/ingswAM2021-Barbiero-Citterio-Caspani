package it.polimi.ingsw.observer;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.match.CardGrid;
import it.polimi.ingsw.model.match.market.Market;
import it.polimi.ingsw.model.match.player.personalBoard.DevCardSlots;
import it.polimi.ingsw.model.match.player.personalBoard.DiscountMap;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;

import java.util.List;
import java.util.Map;

/**
 * This interface implements the Observer-side of the paradigm.
 * The class that implements it, has to redefine each method adding Summary-update procedures.
 */
public interface ModelObserver {

    void updateMarket(Market market);
    void updateCardGrid(CardGrid cardGrid);
    void updateLorenzoMarker(int lorenzoMarker);
    void updateFaithPath(List<Cell> path);

    void updatePersonalBoard(String nickname, PersonalBoard personalBoard);
    void updateMarketBuffer(String nickname, Warehouse warehouse);
    void updateWarehouse(String nickname, Warehouse warehouse);
    void updateStrongbox(String nickname, StrongBox strongbox);
    void updateFaithMarker(String nickname, int faithMarker);
    void updatePopeTiles(String nickname, Map<String, List<Integer>> popeTiles);
    void updateDevCardSlots(String nickname, DevCardSlots devCardSlots);
    void updateHandLeaders(String nickname, List<LeaderCard> handLeaders);
    void updateHandLeadersDiscard(String nickname, LeaderCard handLeader);
    void updateActiveLeaders(String nickname, LeaderCard activeLeader);
    void updateWhiteMarbleConversions(String nickname, PhysicalResource whiteMarbleConversion);
    void updateDiscountMap(String nickname, DiscountMap discountMap);
    void updateTempDevCard(String nickname, DevelopmentCard tempDevCard);
    void updateTempProduction(String nickname, Production tempProduction);
    void updateLastUsedState(String nickname, StateName lastUsedState);

}
