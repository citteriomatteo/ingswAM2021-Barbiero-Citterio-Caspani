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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class implements the summary of a single player.
 * It contains whatever the client can and needs to see in its view during the game.
 */
public class PlayerSummary
{
    //player's general things
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
    private DevelopmentCard tempDevCard;
    private Production tempProduction;
    private StateName lastUsedState;


    /**
     * This constructor is called once for every player, at the beginning of the starting phase.
     * It initializes its Summary in an "empty everything" state.
     */
    public PlayerSummary(Player player){
        this.nickname = player.getNickname();

        //last used state init to the first state after the creation of the match
        updateLastUsedState(StateName.WAITING_LEADERS);
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
     * This constructor is called once for every player, at the beginning of the match.
     * It initializes its Summary basing on its actual (starting) situation.
     * (cardMap is passed only because here we need to use it!)
     */
    public PlayerSummary(Player player, Map<String, Card> cardMap){
        this.nickname = player.getNickname();

        if(player.getPersonalBoard() != null){

            //last used state init to the first state after the creation of the match
            updateLastUsedState(StateName.WAITING_LEADERS);

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
            handLeaders = new ArrayList<>();
            updateHandLeaders(player.getHandLeaders(), cardMap);

            //active leaders init
            activeLeaders = new ArrayList<>();
            updateActiveLeaders(player.getPersonalBoard().getActiveLeaders(), cardMap);
            updateActiveLeaders(player.getPersonalBoard().getActiveProductionLeaders(), cardMap);

            //white marble conversions init
            this.whiteMarbleConversions = new ArrayList<>();
            for(PhysicalResource conv : player.getWhiteMarbleConversions())
                updateWhiteMarbleConversions(conv);

            //discount map init
            this.discountMap = new ArrayList<>();
            updateDiscountMap(player.getPersonalBoard().getDiscountMap());

            //temp dev card init
            updateTempDevCard(player.getTempDevCard());

            //temp production init
            updateTempProduction(player.getTempProduction());

        }
    }

    //UPDATE METHODS ( CALLED BY THE SUMMARY ) :

    /**
     * This method, when called, updates the personal board in this player's summary.
     * @param personalBoard
     * @param cardMap
     */
    public void updatePersonalBoard(PersonalBoard personalBoard, Map<String, Card> cardMap){

    }

    /**
     * This method, when called, updates the market buffer in this player's summary.
     * @param warehouse
     */
    public void updateMarketBuffer(Warehouse warehouse){
        this.marketBuffer = warehouse.getBuffer();
    }

    /**
     * This method, when called, updates the warehouse in this player's summary.
     * @param warehouse
     */
    public void updateWarehouse(Warehouse warehouse){
        this.warehouse = warehouse.getWarehouseDisposition();
    }

    /**
     * This method, when called, updates the strongbox in this player's summary.
     * @param strongbox
     */
    public void updateStrongbox(StrongBox strongbox){
        this.strongbox = new ArrayList<>();
        try {
            for (ResType type : ResType.values())
                this.strongbox.add(new PhysicalResource(type, strongbox.getNumberOf(type)));
        }
        catch(NegativeQuantityException e){ System.exit(1); }

    }

    /**
     * This method, when called, updates the faith marker in this player's summary.
     * @param faithMarker
     */
    public void updateFaithMarker(int faithMarker){
        this.faithMarker = faithMarker;
    }

    /**
     * This method, when called, updates the pope tiles' state in this player's summary.
     * @param popeTiles
     */
    public void updatePopeTiles(List<Integer> popeTiles){
        this.popeTiles = popeTiles;
    }

    /**
     * This method, when called, updates the dev card slots in this player's summary.
     * @param devCardSlots
     */
    public void updateDevCardSlots(DevCardSlots devCardSlots, Map<String, Card> cardMap){
        for(int i = 0; i<devCardSlots.getSlots().length; i++)
            this.devCardSlots[i] = new ArrayList<>(devCardSlots.getSlots()[i].stream().map((x)->getKeyByValue(cardMap,x)).collect(Collectors.toList()));

    }

    /**
     * This method, when called, updates the hand leaders in this player's summary.
     * @param handLeaders
     */
    public void updateHandLeaders(List<LeaderCard> handLeaders, Map<String, Card> cardMap){
        this.handLeaders = new ArrayList<>(handLeaders.stream().map((x)-> getKeyByValue(cardMap, x)).collect(Collectors.toList()));
    }

    /**
     * This method, when called, updates the active leaders in this player's summary, adding them in the list.
     * It clears the list before re-inserting everything, to avoid duplicates.
     * @param activeLeaders
     */
    public void updateActiveLeaders(List<LeaderCard> activeLeaders, Map<String, Card> cardMap){
        for(LeaderCard lc : activeLeaders)
            if(!this.activeLeaders.contains(getKeyByValue(cardMap,lc)))
                this.activeLeaders.add(getKeyByValue(cardMap,lc));
    }

    /**
     * This method, when called, updates the white marble conversions in this player's summary, adding the new one.
     * @param whiteMarbleConversion
     */
    public void updateWhiteMarbleConversions(PhysicalResource whiteMarbleConversion){
        this.whiteMarbleConversions.add(whiteMarbleConversion);
    }

    /**
     * This method, when called, updates the discount map in this player's summary.
     * It clears the list before re-inserting everything, to avoid duplicates.
     * @param discountMap
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
     * @param tempDevCard
     */
    public void updateTempDevCard(DevelopmentCard tempDevCard){
        this.tempDevCard = tempDevCard;
    }

    /**
     * This method, when called, updates the temporary production in this player's summary.
     * @param tempProduction
     */
    public void updateTempProduction(Production tempProduction){
        this.tempProduction = tempProduction;
    }

    /**
     * This method, when called, updates the last used state in this player's summary, useful for disconnections
     * in the middle of a complex operation ( such as Dev card buy, production, etc. ).
     * @param lastUsedState
     */
    public void updateLastUsedState(StateName lastUsedState) { this.lastUsedState = lastUsedState; }

    //ALL GETTERS:
    public String getNickname() { return nickname; }
    public List<PhysicalResource> getWarehouse() { return warehouse; }
    public List<PhysicalResource> getStrongbox() { return strongbox; }
    public int getFaithMarker() { return faithMarker; }
    public List<Integer> getPopeTiles() { return popeTiles; }
    public List<String>[] getDevCardSlots() { return devCardSlots; }
    public List<String> getHandLeaders() { return handLeaders; }
    public List<String> getActiveLeaders() { return activeLeaders; }
    public List<PhysicalResource> getWhiteMarbleConversions() { return whiteMarbleConversions; }
    public List<PhysicalResource> getDiscountMap() { return discountMap; }
    public DevelopmentCard getTempDevCard() { return tempDevCard; }
    public Production getTempProduction() { return tempProduction; }
    public StateName getLastUsedState() { return lastUsedState; }

    //FUNCTIONAL METHODS:
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}