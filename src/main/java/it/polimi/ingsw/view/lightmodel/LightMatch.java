package it.polimi.ingsw.view.lightmodel;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.match.Summary;
import it.polimi.ingsw.model.match.player.PlayerSummary;
import it.polimi.ingsw.view.observer.ViewObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class implements a pseudo-Summary for client applications.
 * It contains the same information as the Summary, but they're overwritten here in order to implement a new
 * Observer-Observable paradigm specific for the View.
 */
public class LightMatch extends ViewObservable {

    private Map<String, Card> cardMap;
    private char[][] market;
    private char sideMarble;
    private List<String>[][] cardGrid;
    private int lorenzoMarker;  //stays "-1" in multi-player matches!

    private List<LightPlayer> playersSummary;

    /**
     * Constructor: initialize a LightMatch as a pseudo-Summary.
     * Called at the beginning of the starting phase and after the reconnection.
     * @param summary the Summary to copy
     */
    public LightMatch(Summary summary){

        this.cardMap = summary.getCardMap();
        this.market = summary.getMarket();
        this.sideMarble = summary.getSideMarble();
        this.cardGrid = summary.getCardGrid();

        playersSummary = new ArrayList<>();
        for(PlayerSummary ps : summary.getPlayersSummary())
            playersSummary.add(new LightPlayer(ps));

    }

    /**
     * This method sets the map of cards' IDs.
     * @param cardMap
     */
    public void setCardMap(Map<String, Card> cardMap) {
        this.cardMap = cardMap;
    }

    /**
     * This method sets the market and the slide marble.
     * Calls an update to the view.
     * @param market the market
     * @param sideMarble the slide marble
     */
    public void setMarket(char[][] market, char sideMarble) {
        this.market = market;
        this.sideMarble = sideMarble;
        updateMarket(market, sideMarble);
    }

    /**
     * This method sets the card grid.
     * Calls an update to the view.
     * @param cardGrid the grid
     */
    public void setCardGrid(List<String>[][] cardGrid) {
        this.cardGrid = cardGrid;
        updateCardGrid(cardGrid);
    }

    /**
     * This method sets the black marker position.
     * Calls an update to the view.
     * @param lorenzoMarker the new marker
     */
    public void setLorenzoMarker(int lorenzoMarker) {
        this.lorenzoMarker = lorenzoMarker;
        updateLorenzoMarker(lorenzoMarker);
    }

    /**
     * This method sets the warehouse.
     * Calls an update to the view.
     * @param warehouse the warehouse
     */
    public void setWarehouse(String nickname, List<PhysicalResource> warehouse) {
        getPlayerSummary(nickname).setWarehouse(warehouse);
        updateWarehouse(nickname, warehouse);
    }

    /**
     * This method sets the market buffer.
     * Calls an update to the view.
     * @param marketBuffer the buffer
     */
    public void setMarketBuffer(String nickname, List<PhysicalResource> marketBuffer) {
        getPlayerSummary(nickname).setMarketBuffer(marketBuffer);
        updateMarketBuffer(nickname, marketBuffer);
    }

    /**
     * This method sets the strongbox.
     * Calls an update to the view.
     * @param strongbox the strongbox
     */
    public void setStrongbox(String nickname, List<PhysicalResource> strongbox) {
        getPlayerSummary(nickname).setStrongbox(strongbox);
        updateStrongbox(nickname, strongbox);
    }

    /**
     * This method sets the player's faith marker.
     * Calls an update to the view.
     * @param faithMarker the new marker
     */
    public void setFaithMarker(String nickname, int faithMarker) {
        getPlayerSummary(nickname).setFaithMarker(faithMarker);
        updateFaithMarker(nickname, faithMarker);
    }

    /**
     * This method sets the player's pope tiles.
     * Calls an update to the view.
     * @param popeTiles the player's tiles
     */
    public void setPopeTiles(String nickname, List<Integer> popeTiles) {
        getPlayerSummary(nickname).setPopeTiles(popeTiles);
        updatePopeTiles(nickname, popeTiles);
    }

    /**
     * This method sets the player's dev card slots.
     * Calls an update to the view.
     * @param devCardSlots the slots
     */
    public void setDevCardSlots(String nickname, List<String>[] devCardSlots) {
        getPlayerSummary(nickname).setDevCardSlots(devCardSlots);
        updateDevCardSlots(nickname, devCardSlots);
    }

    /**
     * This method sets the player's hand leaders.
     * Calls an update to the view.
     * @param handLeaders the player's hand leaders
     */
    public void setHandLeaders(String nickname, List<String> handLeaders) {
        getPlayerSummary(nickname).setHandLeaders(handLeaders);
        updateHandLeaders(nickname, handLeaders);
    }

    /**
     * This method sets the player's active leaders.
     * Calls an update to the view.
     * @param activeLeaders the player's active leaders
     */
    public void setActiveLeaders(String nickname, List<String> activeLeaders) {
        getPlayerSummary(nickname).setActiveLeaders(activeLeaders);
        updateActiveLeaders(nickname, activeLeaders);
    }

    /**
     * This method sets the player's white marble conversions.
     * Calls an update to the view.
     * @param whiteMarbleConversions the player's conversions
     */
    public void setWhiteMarbleConversions(String nickname, List<PhysicalResource> whiteMarbleConversions) {
        getPlayerSummary(nickname).setWhiteMarbleConversions(whiteMarbleConversions);
        updateWhiteMarbleConversions(nickname, whiteMarbleConversions);
    }

    /**
     * This method sets the player's discount map.
     * Calls an update to the view.
     * @param discountMap the player's discount map
     */
    public void setDiscountMap(String nickname, List<PhysicalResource> discountMap) {
        getPlayerSummary(nickname).setDiscountMap(discountMap);
        updateDiscountMap(nickname, discountMap);
    }

    /**
     * This method sets the player's temporary dev card.
     * Calls an update to the view.
     * @param tempDevCard the player's temporary dev card
     */
    public void setTempDevCard(String nickname, String tempDevCard) {
        getPlayerSummary(nickname).setTempDevCard(tempDevCard);
        updateTempDevCard(nickname, tempDevCard);
    }

    /**
     * This method sets the player's temporary production.
     * @param tempProduction the player's temporary production
     */
    public void setTempProduction(String nickname, Production tempProduction) {
        getPlayerSummary(nickname).setTempProduction(tempProduction);
    }

    /**
     * This method sets the player's last used state.
     * @param lastUsedState the player's last used state
     */
    public void setLastUsedState(String nickname, StateName lastUsedState) {
        getPlayerSummary(nickname).setLastUsedState(lastUsedState);
    }

    //GETTERS:

    public Map<String, Card> getCardMap() { return cardMap; }
    public char[][] getMarket() { return market; }
    public char getSideMarble() { return sideMarble; }
    public List<String>[][] getCardGrid() { return cardGrid; }
    public int getLorenzoMarker() { return lorenzoMarker; }
    public List<LightPlayer> getPlayersSummary() { return playersSummary; }
    public LightPlayer getPlayerSummary(String nickname){
        for(LightPlayer p : playersSummary)
            if(p.getNickname().equals(nickname))
                return p;
        return null;
    }
}