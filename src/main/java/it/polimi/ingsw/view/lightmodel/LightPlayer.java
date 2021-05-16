package it.polimi.ingsw.view.lightmodel;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.match.player.PlayerSummary;

import java.util.List;

public class LightPlayer {

    //player's general things
    private final String nickname;
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
    private StateName lastUsedState;

    /**
     * Constructor: takes a PlayerSummary (contained in the Summary received by message)
     * and overwrites every parameter here.
     * @param playerSummary the summary of the player
     */
    public LightPlayer(PlayerSummary playerSummary) {
        this.nickname = playerSummary.getNickname();
        setWarehouse(playerSummary.getWarehouse());
        setMarketBuffer(playerSummary.getMarketBuffer());
        setStrongbox(playerSummary.getStrongbox());
        setFaithMarker(playerSummary.getFaithMarker());
        setPopeTiles(playerSummary.getPopeTiles());
        setDevCardSlots(playerSummary.getDevCardSlots());
        setHandLeaders(playerSummary.getHandLeaders());
        setActiveLeaders(playerSummary.getActiveLeaders());
        setWhiteMarbleConversions(playerSummary.getWhiteMarbleConversions());
        setDiscountMap(playerSummary.getDiscountMap());
        setTempDevCard(playerSummary.getTempDevCard());
        setTempProduction(playerSummary.getTempProduction());
        setLastUsedState(playerSummary.getLastUsedState());
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
     * This method sets the active leaders.
     * @param activeLeaders the active leaders
     */
    public void setActiveLeaders(List<String> activeLeaders) {
        this.activeLeaders = activeLeaders;
    }

    /**
     * This method sets the white marble conversions.
     * @param whiteMarbleConversions the conversions
     */
    public void setWhiteMarbleConversions(List<PhysicalResource> whiteMarbleConversions) {
        this.whiteMarbleConversions = whiteMarbleConversions;
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
     * This method sets the player's last used state.
     * @param lastUsedState the last used state
     */
    public void setLastUsedState(StateName lastUsedState) {
        this.lastUsedState = lastUsedState;
    }

    //GETTERS:

    public String getNickname() { return nickname; }
    public List<PhysicalResource> getWarehouse() { return warehouse; }
    public List<PhysicalResource> getMarketBuffer() { return marketBuffer; }
    public List<PhysicalResource> getStrongbox() { return strongbox; }
    public int getFaithMarker() { return faithMarker; }
    public List<Integer> getPopeTiles() { return popeTiles; }
    public List<String>[] getDevCardSlots() { return devCardSlots; }
    public List<String> getHandLeaders() { return handLeaders; }
    public List<String> getActiveLeaders() { return activeLeaders; }
    public List<PhysicalResource> getWhiteMarbleConversions() { return whiteMarbleConversions; }
    public List<PhysicalResource> getDiscountMap() { return discountMap; }
    public String getTempDevCard() { return tempDevCard; }
    public Production getTempProduction() { return tempProduction; }
    public StateName getLastUsedState() { return lastUsedState; }
}