package it.polimi.ingsw.model.match;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.match.market.Market;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.PlayerSummary;
import it.polimi.ingsw.model.match.player.personalBoard.DevCardSlots;
import it.polimi.ingsw.model.match.player.personalBoard.DiscountMap;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.SingleFaithPath;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.observer.ModelObserver;

import java.util.*;

/**
 * This class implements a summary of the match situation.
 * It contains whatever the client is able and needs to see in its view,
 * and it's particularly useful for making easier user reconnections.
 */
public class Summary implements ModelObserver
{
    private Map<String, Card> cardMap;
    private char[][] market;
    private char sideMarble;
    private List<String>[][] cardGrid;

    private int lorenzoMarker;  //stays "-1" in multi-player matches!

    private List<PlayerSummary> playersSummary;

    public Summary(List<Player> playersInMatch){

        //cardMap init
        cardMap = new HashMap<>();
        // market init
        this.market = new char[3][4];
        // cardGrid init
        this.cardGrid = new ArrayList[CardGrid.MAX_LEVEL][CardColor.values().length];
        //lorenzo's marker init: moved here in Summary because knowing if the match is single or multi is needed.
        this.lorenzoMarker = -1;
        //players summaries init
        playersSummary = new ArrayList<>();
        for(Player p : playersInMatch)
            playersSummary.add(new PlayerSummary(p));
    }

    /**
     * This constructor is called once at the beginning of the match.
     * It initializes the Summary basing on the actual (starting) situation.
     * @param match
     * @param cardMap
     */
    public Summary(Match match, Map<String, Card> cardMap){
        //cardMap init
        this.cardMap = cardMap;

        // market init
        this.market = new char[3][4];
        updateMarket(match.getMarket());

        // cardGrid init
        this.cardGrid = new ArrayList[CardGrid.MAX_LEVEL][CardColor.values().length];
        updateCardGrid(match.getCardGrid());

        //lorenzo's marker init: moved here in Summary because knowing if the match is single or multi is needed.
        this.lorenzoMarker = -1;
        if(match.getPlayers().size()==1)
            updateLorenzoMarker(((SingleFaithPath) match.getPlayers().get(0).getPersonalBoard().getFaithPath()).getBlackPosition());

        //players summaries init
        playersSummary = new ArrayList<>();
        for(Player p : match.getPlayers())
            playersSummary.add(new PlayerSummary(p, cardMap));
    }

    /**
     * This method returns the PlayerSummary associated to the requested nickname.
     * @param nickname
     * @return the summary
     */
    public PlayerSummary getPlayerSummary(String nickname){
        for(PlayerSummary s : playersSummary)
            if(s.getNickname().equals(nickname))
                return s;
        return null;
    }


    //UPDATE METHODS ( CALLED BY THE "OBSERVABLE" CLASS ) :

    /**
     * This method, when called, updates the market in the summary.
     * @param market
     */
    @Override
    public void updateMarket(Market market) {
        sideMarble = Character.toLowerCase(market.getSlide().toString().charAt(0));
        for(int i = 0; i< market.getBoard().length; i++)
            for(int j = 0; j<market.getBoard()[i].length; j++)
                this.market[i][j] = Character.toLowerCase(market.getBoard()[i][j].toString().charAt(0));
    }

    /**
     * This method, when called, updates the card grid in the summary.
     * It inserts into every slot the id of the top card and the depth of the
     * stack (got from cardGrid.getGrid()[i][j].size()).
     * @param cardGrid
     */
    @Override
    public void updateCardGrid(CardGrid cardGrid) {
        for(int i = 0; i<cardGrid.getTop().length; i++)
            for(int j = 0; j<cardGrid.getTop()[i].length; j++) {
                this.cardGrid[i][j] = new ArrayList<>();
                this.cardGrid[i][j].add(getKeyByValue(cardMap,cardGrid.getTop()[i][j]));
                this.cardGrid[i][j].add("" + cardGrid.getGrid()[i][j].size());
            }
    }

    /**
     * This method, when called, updates the lorenzo marker (if single-player) in the summary.
     * @param lorenzoMarker
     */
    @Override
    public void updateLorenzoMarker(int lorenzoMarker) {
        this.lorenzoMarker = lorenzoMarker;
    }

    /**
     * This method, when called, updates the personal board of the requested player in the summary.
     * @param nickname  the requested player
     * @param personalBoard
     */
    public void updatePersonalBoard(String nickname, PersonalBoard personalBoard){
        getPlayerSummary(nickname).updatePersonalBoard(personalBoard, cardMap);
    }

    /**
     * This method, when called, updates the market buffer of the requested player in the summary.
     * @param nickname  the requested player
     * @param warehouse
     */
    public void updateMarketBuffer(String nickname, Warehouse warehouse){
        getPlayerSummary(nickname).updateMarketBuffer(warehouse);
    }

    /**
     * This method, when called, updates the warehouse of the requested player in the summary.
     * @param nickname  the requested player
     * @param warehouse
     */
    @Override
    public void updateWarehouse(String nickname, Warehouse warehouse) {
        getPlayerSummary(nickname).updateWarehouse(warehouse);
    }

    /**
     * This method, when called, updates the strongbox of the requested player in the summary.
     * @param nickname  the requested player
     * @param strongbox
     */
    @Override
    public void updateStrongbox(String nickname, StrongBox strongbox) {
        getPlayerSummary(nickname).updateStrongbox(strongbox);
    }

    /**
     * This method, when called, updates the faith marker of the requested player in the summary.
     * @param nickname  the requested player
     * @param faithMarker
     */
    @Override
    public void updateFaithMarker(String nickname, int faithMarker) {
        getPlayerSummary(nickname).updateFaithMarker(faithMarker);
    }

    /**
     * This method, when called, updates the pope tiles' state of the requested player in the summary.
     * @param nickname  the requested player
     * @param popeTiles
     */
    @Override
    public void updatePopeTiles(String nickname, List<Integer> popeTiles) {
        getPlayerSummary(nickname).updatePopeTiles(popeTiles);
    }

    /**
     * This method, when called, updates the dev card slots of the requested player in the summary.
     * @param nickname  the requested player
     * @param devCardSlots
     */
    @Override
    public void updateDevCardSlots(String nickname, DevCardSlots devCardSlots) {
        getPlayerSummary(nickname).updateDevCardSlots(devCardSlots, cardMap);
    }

    /**
     * This method, when called, updates the hand leaders' state of the requested player in the summary.
     * @param nickname  the requested player
     * @param handLeaders
     */
    @Override
    public void updateHandLeaders(String nickname, List<LeaderCard> handLeaders) {
        getPlayerSummary(nickname).updateHandLeaders(handLeaders, cardMap);
    }

    /**
     * This method, when called, updates the active leaders' state of the requested player in the summary.
     * @param nickname  the requested player
     * @param activeLeaders
     */
    @Override
    public void updateActiveLeaders(String nickname, List<LeaderCard> activeLeaders) {
        getPlayerSummary(nickname).updateActiveLeaders(activeLeaders, cardMap);
    }

    /**
     * This update method receives the new conversion to add in the list of the others.
     * @param nickname              the player who activated the conversion leader
     * @param whiteMarbleConversion the related conversion
     */
    @Override
    public void updateWhiteMarbleConversions(String nickname, PhysicalResource whiteMarbleConversion) {
        getPlayerSummary(nickname).updateWhiteMarbleConversions(whiteMarbleConversion);
    }

    /**
     * This method, when called, updates the discount map of the requested player in the summary.
     * @param nickname  the requested player
     * @param discountMap
     */
    @Override
    public void updateDiscountMap(String nickname, DiscountMap discountMap) {
        getPlayerSummary(nickname).updateDiscountMap(discountMap);
    }

    /**
     * This method, when called, updates the temporary dev card of the requested player in the summary.
     * @param nickname  the requested player
     * @param tempDevCard
     */
    @Override
    public void updateTempDevCard(String nickname, DevelopmentCard tempDevCard) {
        getPlayerSummary(nickname).updateTempDevCard(tempDevCard);
    }

    /**
     * This method, when called, updates the temporary production of the requested player in the summary.
     * @param nickname  the requested player
     * @param tempProduction
     */
    @Override
    public void updateTempProduction(String nickname, Production tempProduction) {
        getPlayerSummary(nickname).updateTempProduction(tempProduction);
    }

    /**
     * This method, when called, updates the last used state in this player's summary, useful for disconnection
     * in the middle of a complex operation ( such as Dev card buy, production, etc. ).
     * @param lastUsedState
     */
    @Override
    public void updateLastUsedState(String nickname, StateName lastUsedState) {
        getPlayerSummary(nickname).updateLastUsedState(lastUsedState);
    }


    //GETTERS:


    public Map<String, Card> getCardMap() { return cardMap; }
    public char[][] getMarket() { return market; }
    public char getSideMarble() { return sideMarble; }
    public List<String>[][] getCardGrid() { return cardGrid; }
    public int getLorenzoMarker() { return lorenzoMarker; }
    public List<PlayerSummary> getPlayersSummary() { return playersSummary; }

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