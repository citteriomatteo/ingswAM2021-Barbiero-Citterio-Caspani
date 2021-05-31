package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.view.lightmodel.LightMatch;

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
     * This method calls a total update on the Observer's class.
     * @param match the match to show
     */
    public void updateMatch(LightMatch match) { observers.get(0).updateMatch(match); }

    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the market into
     */
    public void updateMarket(LightMatch match){
        observers.get(0).updateMarket(match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the card grid into
     */
    public void updateCardGrid(LightMatch match){
        observers.get(0).updateCardGrid(match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the black marker into
     */
    public void updateLorenzoMarker(LightMatch match){
        observers.get(0).updateLorenzoMarker(match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the player's warehouse into
     */
    public void updateWarehouse(String nickname, LightMatch match){
        observers.get(0).updateWarehouse(nickname, match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the player's market buffer into
     */
    public void updateMarketBuffer(String nickname, LightMatch match){
        observers.get(0).updateMarketBuffer(nickname, match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the player's strongbox into
     */
    public void updateStrongbox(String nickname, LightMatch match){
        observers.get(0).updateStrongbox(nickname, match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the player's faith marker into
     */
    public void updateFaithMarker(String nickname, LightMatch match){
        observers.get(0).updateFaithMarker(nickname, match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the player's pope tiles into
     */
    public void updatePopeTiles(String nickname, LightMatch match){
        observers.get(0).updatePopeTiles(nickname, match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the player's dev card slots into
     */
    public void updateDevCardSlots(String nickname, LightMatch match){
        observers.get(0).updateDevCardSlots(nickname, match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the player's hand leaders into
     */
    public void updateHandLeaders(String nickname, LightMatch match){
        observers.get(0).updateHandLeaders(nickname, match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the player's active leaders into
     */
    public void updateActiveLeaders(String nickname, LightMatch match){
        observers.get(0).updateActiveLeaders(nickname, match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the player's white marble conversions into
     */
    public void updateWhiteMarbleConversions(String nickname, LightMatch match){
        observers.get(0).updateWhiteMarbleConversions(nickname, match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the player's discount map into
     */
    public void updateDiscountMap(String nickname, LightMatch match){
        observers.get(0).updateDiscountMap(nickname, match);
    }
    /**
     * This method calls an update on the Observer's class.
     * @param match the match to get the player's temporary dev card into
     */
    public void updateTempDevCard(String nickname, LightMatch match){
        observers.get(0).updateTempDevCard(nickname, match);
    }
}
