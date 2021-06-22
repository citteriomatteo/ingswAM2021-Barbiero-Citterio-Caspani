package it.polimi.ingsw.view.lightmodel;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.Production;
import it.polimi.ingsw.gameLogic.model.match.player.PlayerSummary;

import java.util.List;
import java.util.ArrayList;

/**
 * This class implements a summarized version of the Player class present in the server.
 * It contains the same information as the Summary, but they're overwritten here in order to implement a new
 * Observer-Observable paradigm specific for the View.
 */
public class LightPlayer {

    //player's general things
    private final String nickname;
    private final String color;
    private boolean connected;
    private List<PhysicalResource> warehouse;
    private List<PhysicalResource> marketBuffer;
    private List<PhysicalResource> strongbox;
    private int faithMarker;
    private List<Integer> popeTiles;
    private List<String>[] devCardSlots;
    private List<String> handLeaders;
    private List<String> activeLeaders;
    private List<PhysicalResource> whiteMarbleConversions;
    private List<PhysicalResource> discountMap;

    //stuff for multiple states' operations
    private String tempDevCard;
    private Production tempProduction;

    /**
     * Constructor: takes a PlayerSummary (contained in the Summary received by message)
     * and overwrites every parameter here.
     * @param playerSummary the summary of the player
     * @param color his representing color on the match
     */
    public LightPlayer(PlayerSummary playerSummary, String color) {
        this.connected = playerSummary.isConnected();
        this.color = color;
        this.nickname = playerSummary.getNickname();
        setWarehouse(playerSummary.getWarehouse());
        setMarketBuffer(playerSummary.getMarketBuffer());
        setStrongbox(playerSummary.getStrongbox());
        setFaithMarker(playerSummary.getFaithMarker());
        setPopeTiles(playerSummary.getPopeTiles());
        setDevCardSlots(playerSummary.getDevCardSlots());
        setHandLeaders(playerSummary.getHandLeaders());
        activeLeaders = new ArrayList<>();
        for(String l : playerSummary.getActiveLeaders())
            setActiveLeader(l);
        whiteMarbleConversions = new ArrayList<>();
        for(PhysicalResource conv : playerSummary.getWhiteMarbleConversions())
            setWhiteMarbleConversions(conv);
        setDiscountMap(playerSummary.getDiscountMap());
        setTempDevCard(playerSummary.getTempDevCard());
        setTempProduction(playerSummary.getTempProduction());
    }

    /**
     * This method sets the warehouse.
     * @param warehouse the warehouse
     */
    public void setWarehouse(List<PhysicalResource> warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * This method sets the market buffer.
     * @param marketBuffer the buffer
     */
    public void setMarketBuffer(List<PhysicalResource> marketBuffer) {

        this.marketBuffer = marketBuffer;
        if(this.marketBuffer == null)
            this.marketBuffer = new ArrayList<>();
    }

    /**
     * This method sets the strongbox.
     * @param strongbox the strongbox
     */
    public void setStrongbox(List<PhysicalResource> strongbox) {
        this.strongbox = strongbox;
    }

    /**
     * This method sets the faith marker.
     * @param faithMarker the position
     */
    public void setFaithMarker(int faithMarker) {
        this.faithMarker = faithMarker;
    }

    /**
     * This method sets the pope tiles.
     * @param popeTiles the pope tiles
     */
    public void setPopeTiles(List<Integer> popeTiles) {
        this.popeTiles = popeTiles;
    }

    /**
     * This method sets the dev card slots.
     * @param devCardSlots the dev card slots
     */
    public void setDevCardSlots(List<String>[] devCardSlots) {
        this.devCardSlots = devCardSlots;
    }

    /**
     * This method sets the hand leaders.
     * @param handLeaders the hand leaders
     */
    public void setHandLeaders(List<String> handLeaders) {
        this.handLeaders = handLeaders;
    }

    /**
     * This method discards an hand leader.
     */
    public void leaderDiscard() { handLeaders.remove(0); }

    /**
     * This method sets the active leaders.
     * @param activeLeader the active leader
     */
    public void setActiveLeader(String activeLeader) {
        this.activeLeaders.add(activeLeader);
    }

    /**
     * This method sets the white marble conversions.
     * @param whiteMarbleConversion the conversions
     */
    public void setWhiteMarbleConversions(PhysicalResource whiteMarbleConversion) {
        this.whiteMarbleConversions.add(whiteMarbleConversion);
    }

    /**
     * This method sets the discount map.
     * @param discountMap the discount map
     */
    public void setDiscountMap(List<PhysicalResource> discountMap) {
        this.discountMap = discountMap;
    }

    /**
     * This method sets the temporary dev card.
     * @param tempDevCard the temporary dev card
     */
    public void setTempDevCard(String tempDevCard) {
        this.tempDevCard = tempDevCard;
    }

    /**
     * This method sets the temporary production.
     * @param tempProduction the temporary production
     */
    public void setTempProduction(Production tempProduction) {
        this.tempProduction = tempProduction;
    }

    /**
     * This method sets the player's connection status.
     * @param connected the new status
     */
    public void setConnected(boolean connected) { this.connected = connected; }


    //GETTERS:

    /**
     * Getter
     * @return the color
     */
    public String getColor() { return color; }
    /**
     * Getter
     * @return the nickname
     */
    public String getNickname() { return nickname; }
    /**
     * Getter
     * @return the light warehouse
     */
    public List<PhysicalResource> getWarehouse() { return warehouse; }
    /**
     * Getter
     * @return the light market buffer
     */
    public List<PhysicalResource> getMarketBuffer() { return marketBuffer; }
    /**
     * Getter
     * @return the light strongbox
     */
    public List<PhysicalResource> getStrongbox() { return strongbox; }
    /**
     * Getter
     * @return the faith marker
     */
    public int getFaithMarker() { return faithMarker; }
    /**
     * Getter
     * @return the light pope tiles' list
     */
    public List<Integer> getPopeTiles() { return popeTiles; }
    /**
     * Getter
     * @return the light dev card slots
     */
    public List<String>[] getDevCardSlots() { return devCardSlots; }
    /**
     * Getter
     * @return the light hand leaders' list
     */
    public List<String> getHandLeaders() { return handLeaders; }
    /**
     * Getter
     * @return the light active leaders' list
     */
    public List<String> getActiveLeaders() { return activeLeaders; }
    /**
     * Getter
     * @return the white marble conversions
     */
    public List<PhysicalResource> getWhiteMarbleConversions() { return whiteMarbleConversions; }
    /**
     * Getter
     * @return the discount map
     */
    public List<PhysicalResource> getDiscountMap() { return discountMap; }
    /**
     * Getter
     * @return the temporary dev card
     */
    public String getTempDevCard() { return tempDevCard; }
    /**
     * Getter
     * @return the temporary production
     */
    public Production getTempProduction() { return tempProduction; }
    /**
     * Getter
     * @return the connection state of the player
     */
    public boolean isConnected() { return connected; }
}
