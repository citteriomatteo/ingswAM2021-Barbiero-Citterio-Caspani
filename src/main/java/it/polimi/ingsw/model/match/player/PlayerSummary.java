package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.match.player.personalBoard.DevCardSlots;
import it.polimi.ingsw.model.match.player.personalBoard.DiscountMap;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import static it.polimi.ingsw.controller.MatchController.getKeyByValue;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class implements the summary of a single player.
 * It contains whatever the client can and needs to see in its view during the game.
 */
public class PlayerSummary
{
    //player's general things
    private boolean connected;
    private String nickname;
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
     * This constructor is called once for every player, at the beginning of the starting phase.
     * It initializes its Summary in an "empty everything" state.
     */
    public PlayerSummary(Player player){
        this.connected = player.isConnected();
        System.out.println("player "+player.getNickname()+" set to "+player.isConnected()+" in summary");
        this.nickname = player.getNickname();

        //last used state init to the first state after the creation of the match
        this.lastUsedState = StateName.WAITING_LEADERS;
        //warehouse init
        warehouse = new ArrayList<>();
        //strongbox init
        strongbox = new ArrayList<>();
        //player's faith marker init
        faithMarker = -1;
        //pope tiles init
        popeTiles = new ArrayList<>();
        //dev card slots init
        this.devCardSlots = new List[3];
        for(int i = 0; i<3; i++)
            this.devCardSlots[i] = new ArrayList<>();
        //hand leaders init
        handLeaders = new ArrayList<>();
        //active leaders init
        activeLeaders = new ArrayList<>();
        //white marble conversions init
        this.whiteMarbleConversions = new ArrayList<>();
        //discount map init
        this.discountMap = new ArrayList<>();
        //temp dev card init
        tempDevCard = null;
        //temp production init
        tempProduction = null;
    }

    /**
     * Copy constructor, returns a shallow copy of the PlayerSummary passed, if obscured is set to true replaces the hand leader with a set of -1
     * @param toCopy the player summary you want to copy
     * @param obscured if true hide the hand leaders
     */
    public PlayerSummary(PlayerSummary toCopy, boolean obscured) {
        this.connected = toCopy.connected;
        this.nickname = toCopy.nickname;
        this.warehouse = toCopy.warehouse;
        this.marketBuffer = toCopy.marketBuffer;
        this.strongbox = toCopy.strongbox;
        this.faithMarker = toCopy.faithMarker;
        this.popeTiles = toCopy.popeTiles;
        this.devCardSlots = toCopy.devCardSlots;

        if(obscured)
            this.handLeaders = toCopy.handLeaders.stream().map((x)->"-1").collect(Collectors.toList());
        else
            this.handLeaders = toCopy.handLeaders;

        this.activeLeaders = toCopy.activeLeaders;
        this.whiteMarbleConversions = toCopy.whiteMarbleConversions;
        this.discountMap = toCopy.discountMap;
        this.tempDevCard = toCopy.tempDevCard;
        this.tempProduction = toCopy.tempProduction;
        this.lastUsedState = toCopy.lastUsedState;
    }

    /**
     * This constructor is called once for every player, at the beginning of the match.
     * It initializes its Summary basing on its actual (starting) situation.
     * (cardMap is passed only because here we need to use it!)
     */
    public PlayerSummary(Player player, Map<String, Card> cardMap){
        this.nickname = player.getNickname();
        this.connected = player.isConnected();

        //last used state init to the first state after the creation of the match
        updateLastUsedState(StateName.WAITING_FOR_TURN);

        //warehouse init
        updateWarehouse(player.getPersonalBoard().getWarehouse());

        //strongbox init
        updateStrongbox(player.getPersonalBoard().getStrongBox());

        //player's faith marker init
        updateFaithMarker(player.getPersonalBoard().getFaithPath().getPosition());

        //pope tiles init
        updatePopeTiles(player.getPersonalBoard().getFaithPath().getPopeTiles());

        //dev card slots init
        this.devCardSlots = new List[3];
        updateDevCardSlots(player.getPersonalBoard().getDevCardSlots(), cardMap);

        //hand leaders init
        this.handLeaders = player.getHandLeaders().stream().map((x) -> "-1").collect(Collectors.toList());

        //active leaders init
        this.activeLeaders = player.getPersonalBoard().getActiveLeaders().stream().map((x)->getKeyByValue(cardMap,x)).collect(Collectors.toList());

        //white marble conversions init
        this.whiteMarbleConversions = player.getWhiteMarbleConversions();

        //discount map init
        discountMap = new ArrayList<>();
        updateDiscountMap(player.getPersonalBoard().getDiscountMap());

        //temp dev card init
        updateTempDevCard(player.getTempDevCard(), cardMap);

        //temp production init
        updateTempProduction(player.getTempProduction());



    }

    //UPDATE METHODS ( CALLED BY THE SUMMARY ) :

    /**
     * This method, when called, updates the connections state of this player.
     * @param connected the new state
     */
    public void updateConnectionState(boolean connected){
        this.connected = connected;
    }

    /**
     * This method, when called, updates the personal board in this player's summary.
     * @param personalBoard the personal board
     * @param cardMap       the card map
     */
    public void updatePersonalBoard(PersonalBoard personalBoard, Map<String, Card> cardMap){

    }

    /**
     * This method, when called, updates the market buffer in this player's summary.
     * @param warehouse the warehouse
     */
    public void updateMarketBuffer(Warehouse warehouse){
        this.marketBuffer = warehouse.getBuffer();
    }

    /**
     * This method, when called, updates the warehouse in this player's summary.
     * @param warehouse the warehouse
     */
    public void updateWarehouse(Warehouse warehouse){
        this.warehouse = warehouse.getWarehouseDisposition();
    }

    /**
     * This method, when called, updates the strongbox in this player's summary.
     * @param strongbox the strong box
     */
    public void updateStrongbox(StrongBox strongbox){
        this.strongbox = new ArrayList<>();
        try {
            for (ResType type : strongbox.getResources().keySet())
                this.strongbox.add(new PhysicalResource(type, strongbox.getNumberOf(type)));
        }
        catch(NegativeQuantityException e){ System.exit(1); }

    }

    /**
     * This method, when called, updates the faith marker in this player's summary.
     * @param faithMarker the faith marker
     */
    public void updateFaithMarker(int faithMarker){
        this.faithMarker = faithMarker;
    }

    /**
     * This method, when called, updates the pope tiles' state in this player's summary.
     * @param popeTiles the pope tiles' array
     */
    public void updatePopeTiles(List<Integer> popeTiles){
        this.popeTiles = popeTiles;
    }

    /**
     * This method, when called, updates the dev card slots in this player's summary.
     * @param devCardSlots the dev card slots
     */
    public void updateDevCardSlots(DevCardSlots devCardSlots, Map<String, Card> cardMap){
        for(int i = 0; i<devCardSlots.getSlots().length; i++)
            this.devCardSlots[i] = devCardSlots.getSlots()[i].stream().map((x) -> getKeyByValue(cardMap, x)).collect(Collectors.toList());

    }

    /**
     * This method, when called, updates the hand leaders in this player's summary.
     * @param handLeaders the hand leaders
     */
    public void updateHandLeaders(List<LeaderCard> handLeaders, Map<String, Card> cardMap){
        this.handLeaders = handLeaders.stream().map((x) -> getKeyByValue(cardMap, x)).collect(Collectors.toList());
    }

    /**
     * This method, when called, updates the hand leaders in this player's summary, discarding the passed one.
     * @param handLeader the hand leader discarded
     */
    public void updateHandLeadersDiscard(LeaderCard handLeader, Map<String, Card> cardMap){
        this.handLeaders.remove(getKeyByValue(cardMap,handLeader));
    }

    /**
     * This method, when called, updates the active leaders in this player's summary, adding them in the list.
     * It clears the list before re-inserting everything, to avoid duplicates.
     * @param activeLeader the activated leader
     */
    public boolean updateActiveLeaders(LeaderCard activeLeader, Map<String, Card> cardMap){
        if(!this.activeLeaders.contains(getKeyByValue(cardMap,activeLeader))) {
            this.activeLeaders.add(getKeyByValue(cardMap, activeLeader));
            return true;
        }
        return false;
    }

    /**
     * This method, when called, updates the white marble conversions in this player's summary, adding the new one.
     * @param whiteMarbleConversion the white marble conversions
     */
    public void updateWhiteMarbleConversions(PhysicalResource whiteMarbleConversion){
        this.whiteMarbleConversions.add(whiteMarbleConversion);
    }

    /**
     * This method, when called, updates the discount map in this player's summary.
     * It clears the list before re-inserting everything, to avoid duplicates.
     * @param discountMap the discount map
     */
    public void updateDiscountMap(DiscountMap discountMap){
        this.discountMap.clear();
        try {
            for (ResType type : discountMap.getDiscountMap().keySet())
                this.discountMap.add(new PhysicalResource(type, discountMap.getDiscountMap().get(type)));
        }
        catch(NegativeQuantityException e){ System.exit(1); }
    }

    /**
     * This method, when called, updates the temporary development card in this player's summary.
     * @param tempDevCard the temporary dev card
     */
    public void updateTempDevCard(DevelopmentCard tempDevCard, Map<String, Card> cardMap){
        this.tempDevCard = getKeyByValue(cardMap, tempDevCard);
    }

    /**
     * This method, when called, updates the temporary production in this player's summary.
     * @param tempProduction the temporary production
     */
    public void updateTempProduction(Production tempProduction){
        this.tempProduction = tempProduction;
    }

    /**
     * This method, when called, updates the last used state in this player's summary, useful for disconnections
     * in the middle of a complex operation ( such as Dev card buy, production, etc. ).
     * @param lastUsedState the last used state
     */
    public void updateLastUsedState(StateName lastUsedState) { this.lastUsedState = lastUsedState; }

    //ALL GETTERS:

    public boolean isConnected() { return connected; }
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