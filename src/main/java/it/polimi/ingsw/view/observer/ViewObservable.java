package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.essentials.PhysicalResource;

import java.util.ArrayList;
import java.util.List;


/**
 * This class implements the Observable-side of the paradigm.
 * Extended by the LightMatch class which calls these update methods in a certain way, it calls
 * the mirrored methods on the Observer interface an so it will change the View
 *  (in position 0 of the observers' array) state each move.
 */
public class ViewObservable {

    private final List<ViewObserver> observers = new ArrayList<>();

    /**
     * This method adds the View (Observer) always in the first position of the list.
     * @param view the view instance of the match
     */
    public void setView(ViewObserver view) {
        if(observers.size()>0)
            observers.remove(0);
        observers.add(view);
    }

    /**
     * This method calls an update on the Observer's class.
     * @param market the market
     * @param sideMarble the slide marble
     */
    public void updateMarket(char[][] market, char sideMarble){
        observers.get(0).updateMarket(market, sideMarble);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param cardGrid the card grid
     */
    public void updateCardGrid(List<String>[][] cardGrid){
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
     * This method calls an update on the Observer's class.
     * @param warehouse the warehouse
     */
    public void updateWarehouse(String nickname, List<PhysicalResource> warehouse){
        observers.get(0).updateWarehouse(nickname, warehouse);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param marketBuffer the market buffer
     */
    public void updateMarketBuffer(String nickname, List<PhysicalResource> marketBuffer){
        observers.get(0).updateMarketBuffer(nickname, marketBuffer);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param strongbox the strongbox
     */
    public void updateStrongbox(String nickname, List<PhysicalResource> strongbox){
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
     * @param popeTiles the pope tiles
     */
    public void updatePopeTiles(String nickname, List<Integer> popeTiles){
        observers.get(0).updatePopeTiles(nickname, popeTiles);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param devCardSlots the dev card slots
     */
    public void updateDevCardSlots(String nickname, List<String>[] devCardSlots){
        observers.get(0).updateDevCardSlots(nickname, devCardSlots);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param handLeaders the han leaders
     */
    public void updateHandLeaders(String nickname, List<String> handLeaders){
        observers.get(0).updateHandLeaders(nickname, handLeaders);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param activeLeaders the active leaders
     */
    public void updateActiveLeaders(String nickname, List<String> activeLeaders){
        observers.get(0).updateActiveLeaders(nickname, activeLeaders);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param whiteMarbleConversions the conversions
     */
    public void updateWhiteMarbleConversions(String nickname, List<PhysicalResource> whiteMarbleConversions){
        observers.get(0).updateWhiteMarbleConversions(nickname, whiteMarbleConversions);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param discountMap the discount map
     */
    public void updateDiscountMap(String nickname, List<PhysicalResource> discountMap){
        observers.get(0).updateDiscountMap(nickname, discountMap);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param tempDevCard the temporary dev card
     */
    public void updateTempDevCard(String nickname, String tempDevCard){
        observers.get(0).updateTempDevCard(nickname, tempDevCard);
    }
}
