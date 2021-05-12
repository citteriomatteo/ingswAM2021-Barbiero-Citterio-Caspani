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
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;

import java.util.List;

public interface ModelObserver {

    public void updateMarket(Market market);
    public void updateCardGrid(CardGrid cardGrid);
    public void updateLorenzoMarker(int lorenzoMarker);

    public void updatePersonalBoard(String nickname, PersonalBoard personalBoard);
    public void updateMarketBuffer(String nickname, Warehouse warehouse);
    public void updateWarehouse(String nickname, Warehouse warehouse);
    public void updateStrongbox(String nickname, StrongBox strongbox);
    public void updateFaithMarker(String nickname, int faithMarker);
    public void updatePopeTiles(String nickname, int tileNumber, List<Integer> popeTiles);
    public void updateDevCardSlots(String nickname, DevCardSlots devCardSlots);
    public void updateHandLeaders(String nickname, List<LeaderCard> handLeaders);
    public void updateActiveLeaders(String nickname, LeaderCard activeLeader);
    public void updateWhiteMarbleConversions(String nickname, PhysicalResource whiteMarbleConversion);
    public void updateDiscountMap(String nickname, DiscountMap discountMap);
    public void updateTempDevCard(String nickname, DevelopmentCard tempDevCard);
    public void updateTempProduction(String nickname, Production tempProduction);
    public void updateLastUsedState(String nickname, StateName lastUsedState);

}
