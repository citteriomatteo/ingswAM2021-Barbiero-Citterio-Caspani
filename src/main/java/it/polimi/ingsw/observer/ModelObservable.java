package it.polimi.ingsw.observer;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.match.CardGrid;
import it.polimi.ingsw.model.match.Summary;
import it.polimi.ingsw.model.match.market.Market;
import it.polimi.ingsw.model.match.player.personalBoard.DevCardSlots;
import it.polimi.ingsw.model.match.player.personalBoard.DiscountMap;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class implements the Observable-side of the paradigm.
 * Extended by the Player class which calls these update methods in a certain way, it calls
 * the mirrored methods on the Observer interface an so it will change the Summary
 *  (in position 0 of the observers' array) state each move.
 *  The observer is added here at the beginning of the Turn controller phase (in its constructor).
 */
public class ModelObservable {

    private final List<ModelObserver> observers = new ArrayList<>();

    /**
     * This method adds the Summary (Observer) always in the first position of the list.
     * @param summary the view instance of the match
     */
    public void setSummary(ModelObserver summary) {
        if(observers.size()>0)
            observers.remove(0);
        observers.add(summary);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param market the market
     */
    public void updateMarket(Market market){
        observers.get(0).updateMarket(market);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param cardGrid the card grid
     */
    public void updateCardGrid(CardGrid cardGrid){
        observers.get(0).updateCardGrid(cardGrid);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param lorenzoMarker the black marker
     */
    public void updateLorenzoMarker(int lorenzoMarker){
        observers.get(0).updateLorenzoMarker(lorenzoMarker);
    }
    /**
     * This method calls an update on the Observer's class (NOT USED NOW).
     * @param personalBoard the personalBoard
     */
    public void updatePersonalBoard(String nickname, PersonalBoard personalBoard){
        observers.get(0).updatePersonalBoard(nickname, personalBoard);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param warehouse the warehouse
     */
    public void updateMarketBuffer(String nickname, Warehouse warehouse){
        observers.get(0).updateMarketBuffer(nickname, warehouse);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param warehouse the warehouse
     */
    public void updateWarehouse(String nickname, Warehouse warehouse){
        observers.get(0).updateWarehouse(nickname, warehouse);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param strongbox the strongbox
     */
    public void updateStrongbox(String nickname, StrongBox strongbox){
        observers.get(0).updateStrongbox(nickname, strongbox);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param faithMarker the faith marker
     */
    public void updateFaithMarker(String nickname, int faithMarker){
        observers.get(0).updateFaithMarker(nickname, faithMarker);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param path the faith path
     */
    public void updateFaithPath(List<Cell> path){
        observers.get(0).updateFaithPath(path);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param popeTiles the pope tiles
     */
    public void updatePopeTiles(String nickname, Map<String, List<Integer>> popeTiles){
        observers.get(0).updatePopeTiles(nickname, popeTiles);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param devCardSlots the dev card slots
     */
    public void updateDevCardSlots(String nickname, DevCardSlots devCardSlots){
        observers.get(0).updateDevCardSlots(nickname, devCardSlots);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param handLeaders the han leaders
     */
    public void updateHandLeaders(String nickname, List<LeaderCard> handLeaders){
        observers.get(0).updateHandLeaders(nickname, handLeaders);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param handLeader the discarded han leader
     */
    public void updateHandLeadersDiscard(String nickname, LeaderCard handLeader){
        observers.get(0).updateHandLeadersDiscard(nickname, handLeader);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param activeLeader the activated leader
     */
    public void updateActiveLeaders(String nickname, LeaderCard activeLeader){
        observers.get(0).updateActiveLeaders(nickname, activeLeader);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param whiteMarbleConversion the white marble conversions
     */
    public void updateWhiteMarbleConversions(String nickname, PhysicalResource whiteMarbleConversion){
        observers.get(0).updateWhiteMarbleConversions(nickname, whiteMarbleConversion);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param discountMap the discount map
     */
    public void updateDiscountMap(String nickname, DiscountMap discountMap){
        observers.get(0).updateDiscountMap(nickname, discountMap);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param tempDevCard the temporary dev card
     */
    public void updateTempDevCard(String nickname, DevelopmentCard tempDevCard){
        observers.get(0).updateTempDevCard(nickname, tempDevCard);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param tempProduction the temporary production
     */
    public void updateTempProduction(String nickname, Production tempProduction){
        observers.get(0).updateTempProduction(nickname, tempProduction);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param lastUsedState the player's last used state
     */
    public void updateLastUsedState(String nickname, StateName lastUsedState){
        observers.get(0).updateLastUsedState(nickname, lastUsedState);
    }

    //returns the observer
    public Summary getSummary() {
        return (Summary) observers.get(0);
    }
}
