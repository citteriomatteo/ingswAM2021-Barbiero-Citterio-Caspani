package it.polimi.ingsw.view.lightmodel;

import it.polimi.ingsw.gameLogic.model.essentials.Card;
import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.Production;
import it.polimi.ingsw.gameLogic.model.match.Summary;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.CLI.ColorCli;
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

    private static Map<String, Card> cardMap;
    private Production basicProd;
    private char[][] market;
    private char sideMarble;
    private List<String>[][] cardGrid;
    private List<String> faithPath;
    private int lorenzoMarker;  //stays "-1" in multi-player matches!

    private List<LightPlayer> lightPlayers;

    /**
     * Constructor: initialize a LightMatch as a pseudo-Summary.
     * Called at the beginning of the starting phase and after the reconnection.
     * @param summary the Summary to copy
     */
    public LightMatch(Summary summary, View view){
        setView(view);
        this.basicProd = summary.getBasicProd();
        setCardMap(summary.getCardMap());
        this.market = summary.getMarket();
        this.sideMarble = summary.getSideMarble();
        this.cardGrid = summary.getCardGrid();
        this.faithPath = summary.getFaithPath();
        this.lorenzoMarker = summary.getLorenzoMarker();

        lightPlayers = new ArrayList<>();
        for(int i = 0; i < summary.getPlayersSummary().size(); i++)
            lightPlayers.add(new LightPlayer(summary.getPlayersSummary().get(i), ColorCli.values()[i + 3].toString()));

        //starting update_call
        updateMatch(this);
    }

    /**
     * This method sets the map of cards' IDs.
     * @param cardMap a map that links ids with cards
     */
    public void setCardMap(Map<String, Card> cardMap) { LightMatch.cardMap = cardMap; }

    /**
     * This method sets the market and the slide marble.
     * Calls an update to the view.
     * @param market the market
     * @param sideMarble the slide marble
     */
    public void setMarket(char[][] market, char sideMarble) {
        this.market = market;
        this.sideMarble = sideMarble;
        updateMarket(this);

    }

    /**
     * This method discards a player's leader.
     * Calls an update to the view.
     * @param nickname the player who discards
     */
    public void leaderDiscard(String nickname){
        getLightPlayer(nickname).leaderDiscard();
        updateHandLeaders(nickname, this);
    }

    /**
     * This method sets the card grid.
     * Calls an update to the view.
     * @param cardGrid the grid
     */
    public void setCardGrid(List<String>[][] cardGrid) {
        this.cardGrid = cardGrid;
        updateCardGrid(this);
    }

    /**
     * This method sets the black marker position.
     * Calls an update to the view.
     * @param lorenzoMarker the new marker
     */
    public void setLorenzoMarker(int lorenzoMarker) {
        this.lorenzoMarker = lorenzoMarker;
        updateLorenzoMarker(this);
    }

    /**
     * This method sets the connection state of the player with this nickname to the 'connected' value.
     * @param nickname the nickname
     * @param connected the connection value
     */
    public void setConnected(String nickname, boolean connected) {
        getLightPlayer(nickname).setConnected(connected);
      //  updateMatch(this); removed because gave problems with gui, added a showAll inside the updateDisconnections
        updateDisconnections(nickname, connected);
    }

    /**
     * This method sets the warehouse.
     * Calls an update to the view.
     * @param warehouse the warehouse
     */
    public void setWarehouse(String nickname, List<PhysicalResource> warehouse) {
        getLightPlayer(nickname).setWarehouse(warehouse);
        updateWarehouse(nickname, this);
    }

    /**
     * This method sets the market buffer.
     * Calls an update to the view.
     * @param marketBuffer the buffer
     */
    public void setMarketBuffer(String nickname, List<PhysicalResource> marketBuffer) {
        getLightPlayer(nickname).setMarketBuffer(marketBuffer);
        updateMarketBuffer(nickname, this);
    }

    /**
     * This method sets the strongbox.
     * Calls an update to the view.
     * @param strongbox the strongbox
     */
    public void setStrongbox(String nickname, List<PhysicalResource> strongbox) {
        getLightPlayer(nickname).setStrongbox(strongbox);
        updateStrongbox(nickname, this);
    }

    /**
     * This method sets the player's faith marker.
     * Calls an update to the view.
     * @param faithMarker the new marker
     */
    public void setFaithMarker(String nickname, int faithMarker) {
        getLightPlayer(nickname).setFaithMarker(faithMarker);
        updateFaithMarker(nickname, this);
    }

    /**
     * This method sets the player's pope tiles.
     * Calls an update to the view.
     * @param popeTiles the player's tiles
     */
    public void setPopeTiles(String nickname, List<Integer> popeTiles) {
        getLightPlayer(nickname).setPopeTiles(popeTiles);
        updatePopeTiles(nickname, this);
    }

    /**
     * This method sets the player's dev card slots.
     * Calls an update to the view.
     * @param devCardSlots the slots
     */
    public void setDevCardSlots(String nickname, List<String>[] devCardSlots) {
        getLightPlayer(nickname).setDevCardSlots(devCardSlots);
        updateDevCardSlots(nickname, this);
    }

    /**
     * This method sets the player's hand leaders.
     * Calls an update to the view.
     * @param handLeaders the player's hand leaders
     */
    public void setHandLeaders(String nickname, List<String> handLeaders) {
        getLightPlayer(nickname).setHandLeaders(handLeaders);
        updateHandLeaders(nickname, this);
    }

    /**
     * This method sets the player's active leaders.
     * Calls an update to the view.
     * @param activeLeader the player's active leader
     */
    public void activateLeader(String nickname, String activeLeader) {
        getLightPlayer(nickname).setActiveLeader(activeLeader);
        leaderDiscard(nickname);
        updateActiveLeaders(nickname, this);
    }

    /**
     * This method sets the player's white marble conversions.
     * Calls an update to the view.
     * @param whiteMarbleConversion the player's conversions
     */
    public void setWhiteMarbleConversions(String nickname, PhysicalResource whiteMarbleConversion) {
        getLightPlayer(nickname).setWhiteMarbleConversions(whiteMarbleConversion);
        updateWhiteMarbleConversions(nickname, this);
    }

    /**
     * This method sets the player's discount map.
     * Calls an update to the view.
     * @param discountMap the player's discount map
     */
    public void setDiscountMap(String nickname, List<PhysicalResource> discountMap) {
        getLightPlayer(nickname).setDiscountMap(discountMap);
        updateDiscountMap(nickname, this);
    }

    /**
     * This method sets the player's temporary dev card.
     * Calls an update to the view.
     * @param tempDevCard the player's temporary dev card
     */
    public void setTempDevCard(String nickname, String tempDevCard) {
        getLightPlayer(nickname).setTempDevCard(tempDevCard);
        updateTempDevCard(nickname, this);
    }

    /**
     * This method sets the player's temporary production.
     * @param tempProduction the player's temporary production
     */
    public void setTempProduction(String nickname, Production tempProduction) {
        getLightPlayer(nickname).setTempProduction(tempProduction);
    }

    /**
     * Returns the position of the player inside the match
     * @param nickname nickname of the player you want to know the position
     * @return the position of the player in the match or -1 if there isn't a player with such a nickname inside the match
     */
    public int positionOf(String nickname){
        int i= 0;
        for(LightPlayer player : lightPlayers){
            i++;
            if(player.getNickname().equals(nickname))
                return i;
        }
        return -1;
    }

    /**
     * Returns the number of white marbles drawn during the last market draw.
     * @param row row/column boolean
     * @param num the number of the row/column
     * @return the number of white marbles
     */
    public int numWhiteDrawn(boolean row, int num){

        num--;

        int numWhiteDrawn = 0;

        if(sideMarble == 'w')
            numWhiteDrawn += 1;
        if(row) {
            for (int i = 0; i < 3; i++)
                if(market[num][i] == 'w')
                    numWhiteDrawn += 1;
        }
        else {
            for (int i=0; i < 2; i++)
                if(market[i][num] == 'w')
                    numWhiteDrawn += 1;
        }

        return numWhiteDrawn;
    }


    //GETTERS:

    /**
     * Getter
     * @return the card map
     */
    public static Map<String, Card> getCardMap() { return cardMap; }
    /**
     * Getter
     * @return the basic production
     */
    public Production getBasicProd() { return basicProd; }
    /**
     * Getter
     * @return the light market
     */
    public char[][] getMarket() { return market; }
    /**
     * Getter
     * @return the side marble of the market
     */
    public char getSideMarble() { return sideMarble; }
    /**
     * Getter
     * @return the light card grid, containing the top cards and the depths of every stack
     */
    public List<String>[][] getCardGrid() { return cardGrid; }
    /**
     * Getter
     * @return the light faith path
     */
    public List<String> getFaithPath() { return faithPath; }
    /**
     * Getter
     * @return the lorenzo's position (in single-player matches)
     */
    public int getLorenzoMarker() { return lorenzoMarker; }
    /**
     * Getter
     * @return the list of all light players instances
     */
    public List<LightPlayer> getLightPlayers() { return lightPlayers; }
    /**
     * Getter of a specific LightPlayer's instance.
     * @param nickname the requested player's nickname
     * @return the corresponding last player
     */
    public LightPlayer getLightPlayer(String nickname){
        for(LightPlayer p : lightPlayers)
            if(p.getNickname().equals(nickname))
                return p;
        return null;
    }
}