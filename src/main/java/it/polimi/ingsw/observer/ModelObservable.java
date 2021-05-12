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

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the Observer-side of the paradigm.
 * Extended by the Player class which calls these update methods in a certain way, it calls
 * the mirrored methods on the Observer interface and so it will change the Summary
 *  (in position 0 of the observers' array) state each move.
 *  The observer is added here at the beginning of the Turn controller phase (in its constructor).
 */
public class ModelObservable {

    private final List<ModelObserver> observers = new ArrayList<>();

    public void setSummary(ModelObserver summary) {
        if(observers.size()>0)
            observers.remove(0);
        observers.add(summary);
    }
    public void updateMarket(Market market){
        observers.get(0).updateMarket(market);
    }
    public void updateCardGrid(CardGrid cardGrid){
        observers.get(0).updateCardGrid(cardGrid);
    }
    public void updateLorenzoMarker(int lorenzoMarker){
        observers.get(0).updateLorenzoMarker(lorenzoMarker);
    }

    public void updatePersonalBoard(String nickname, PersonalBoard personalBoard){
        observers.get(0).updatePersonalBoard(nickname, personalBoard);
    }
    public void updateMarketBuffer(String nickname, Warehouse warehouse){
        observers.get(0).updateMarketBuffer(nickname, warehouse);
    }
    public void updateWarehouse(String nickname, Warehouse warehouse){
        observers.get(0).updateWarehouse(nickname, warehouse);
    }
    public void updateStrongbox(String nickname, StrongBox strongbox){
        observers.get(0).updateStrongbox(nickname, strongbox);
    }
    public void updateFaithMarker(String nickname, int faithMarker){
        observers.get(0).updateFaithMarker(nickname, faithMarker);
    }
    public void updatePopeTiles(String nickname, int tileNumber, List<Integer> popeTiles){
        observers.get(0).updatePopeTiles(nickname, tileNumber, popeTiles);
    }
    public void updateDevCardSlots(String nickname, DevCardSlots devCardSlots){
        observers.get(0).updateDevCardSlots(nickname, devCardSlots);
    }
    public void updateHandLeaders(String nickname, List<LeaderCard> handLeaders){
        observers.get(0).updateHandLeaders(nickname, handLeaders);
    }
    public void updateHandLeadersDiscard(String nickname, LeaderCard handLeader){
        observers.get(0).updateHandLeadersDiscard(nickname, handLeader);
    }
    public void updateActiveLeaders(String nickname, LeaderCard activeLeader){
        observers.get(0).updateActiveLeaders(nickname, activeLeader);
    }
    public void updateWhiteMarbleConversions(String nickname, PhysicalResource whiteMarbleConversion){
        observers.get(0).updateWhiteMarbleConversions(nickname, whiteMarbleConversion);
    }
    public void updateDiscountMap(String nickname, DiscountMap discountMap){
        observers.get(0).updateDiscountMap(nickname, discountMap);
    }
    public void updateTempDevCard(String nickname, DevelopmentCard tempDevCard){
        observers.get(0).updateTempDevCard(nickname, tempDevCard);
    }
    public void updateTempProduction(String nickname, Production tempProduction){
        observers.get(0).updateTempProduction(nickname, tempProduction);
    }
    public void updateLastUsedState(String nickname, StateName lastUsedState){
        observers.get(0).updateLastUsedState(nickname, lastUsedState);
    }
}
