package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.essentials.leader.ProductionEffect;
import it.polimi.ingsw.model.essentials.leader.WhiteMarbleEffect;
import it.polimi.ingsw.model.match.CommonThingsTest;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MatchConfiguration;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.market.Marble;
import it.polimi.ingsw.model.match.market.WhiteMarble;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.StrongBox;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.WarehouseDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;
import static it.polimi.ingsw.controller.MatchController.getKeyByValue;


import java.util.*;


public class MatchControllerTest extends CommonThingsTest {
    private static MatchController matchController;
    private Player player;
    private Map<String, Card> map = getCardMap(assignConfiguration("src/test/resources/PartialFreeConfiguration.json"));
    private Match match;

    public void initialization() throws RetryException {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"), new Player("player2"), new Player("player3"), new Player("player4")));

        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummaries(players, map, matchConfiguration.getCustomPath());
        matchController = new MatchController(players, matchConfiguration);
        player = matchController.getCurrentPlayer();
        map = matchController.getCardMap();
    }

    public void initializationCosts() {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"), new Player("player2"), new Player("player3"), new Player("player4")));
        setSummaries(players, map, assignConfiguration("src/test/resources/StandardConfiguration.json").getCustomPath());
        matchController = new MatchController(players);
        player = matchController.getCurrentPlayer();
        map = matchController.getCardMap();
    }

    @Test
    public void isComputableTest() throws RetryException {
        initialization();
        Player player = matchController.getCurrentPlayer();
        assertFalse(matchController.devCardDraw("player5", 1, 1 ));
        assertThrows(RetryException.class, ()->matchController.switchShelf(player.getNickname(), 1,2));
        assertThrows(RetryException.class, ()->matchController.switchShelf(player.getNickname(), 1,2));
        matchController.setState(player.getNickname(), StateName.MARKET_ACTION);
        MultiMatch match1 = (MultiMatch) matchController.getMatch();
        assertFalse(matchController.devCardDraw(match1.getNextPlayer().getNickname(), 1, 1 ));

    }
    @Test
    public void startingLeaderTest() throws RetryException {
        initialization();
        List<LeaderCard> initialLeaders = player.getHandLeaders();
        initialLeaders.remove(0);
        initialLeaders.remove(0);
        String leader1 = getKeyByValue(map, initialLeaders.get(0));
        String leader2 = getKeyByValue(map, initialLeaders.get(1));
        matchController.startingLeader(player.getNickname(), new ArrayList<>(List.of(leader1, leader2)));

        assertEquals(initialLeaders, player.getHandLeaders());
        initialization();
        assertThrows(RetryException.class, ()->matchController.startingLeader(player.getNickname(), List.of("a")));

    }
    @Test
    public void startingResourcesTest() throws RetryException, NegativeQuantityException {
        initialization();
        match = matchController.getMatch();

        player = match.getPlayers().get(0);
        matchController.setState(player.getNickname(), StateName.WAITING_RESOURCES);
        assertThrows(RetryException.class, ()->matchController.startingResources(player.getNickname(), new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,1)))));

        player = match.getPlayers().get(1);
        matchController.setState(player.getNickname(), StateName.WAITING_RESOURCES);
        matchController.startingResources(player.getNickname(), new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,1))));
        assertEquals(new PhysicalResource(ResType.SHIELD,1), match.getPlayer(player.getNickname()).getPersonalBoard().getWarehouse().getWarehouseDisposition().get(0));

        player = match.getPlayers().get(2);
        match = matchController.getMatch();
        matchController.setState(player.getNickname(), StateName.WAITING_RESOURCES);
        matchController.startingResources(player.getNickname(), new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,2))));
        assertEquals(new PhysicalResource(ResType.COIN,1), match.getPlayer(player.getNickname()).
                getPersonalBoard().getWarehouse().getWarehouseDisposition().get(1));

        player = match.getPlayers().get(3);
        match = matchController.getMatch();
        matchController.setState(player.getNickname(), StateName.WAITING_RESOURCES);
        assertThrows(RetryException.class, ()->matchController.startingResources(player.getNickname(),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,2)))));
        List<PhysicalResource> oldWarehouse = player.getPersonalBoard().getWarehouse().getWarehouseDisposition();
        assertThrows(RetryException.class, ()->matchController.startingResources(player.getNickname(),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,1), new PhysicalResource(ResType.COIN, 1)))));
        assertEquals(oldWarehouse, player.getPersonalBoard().getWarehouse().getWarehouseDisposition());

        matchController.startingResources(player.getNickname(), new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,2),
                new PhysicalResource(ResType.SERVANT,1))));
        assertEquals(new PhysicalResource(ResType.COIN,1), player.getPersonalBoard().getWarehouse().getWarehouseDisposition().get(1));
        assertEquals(new PhysicalResource(ResType.SERVANT,1), player.getPersonalBoard().getWarehouse().getWarehouseDisposition().get(0));

    }

    @Test
    public void switchShelfTest() throws RetryException, InvalidQuantityException, ShelfInsertException {
        initialization();
        PhysicalResource physicalResource = new PhysicalResource(ResType.SHIELD,1);
        PhysicalResource physicalResource1 = new PhysicalResource(ResType.COIN,1);
        player = matchController.getCurrentPlayer();
        player.addToWarehouse(physicalResource);
        player.addToWarehouse(physicalResource1);
        player.moveIntoWarehouse(physicalResource,1);
        player.moveIntoWarehouse(physicalResource1, 2);
        matchController.setState(player.getNickname(), StateName.STARTING_TURN);

        Warehouse oldWh = WarehouseDecorator.clone(player.getPersonalBoard().getWarehouse());
        matchController.switchShelf("player11", 8, 1);
        assertEquals(oldWh.getWarehouseDisposition(), player.getPersonalBoard().getWarehouse().getWarehouseDisposition());

        matchController.switchShelf(player.getNickname(), 1, 2);
        assertEquals(physicalResource, player.getPersonalBoard().getWarehouse().getWarehouseDisposition().get(1));
        assertEquals(physicalResource1, player.getPersonalBoard().getWarehouse().getWarehouseDisposition().get(0));
    }

    @Test
    public void leaderActivationTest() throws RetryException {
        initialization();
        player = matchController.getCurrentPlayer();
        player.setHandLeaders(player.getHandLeaders().subList(0,1));
        Card card = player.getHandLeaders().get(0);
        matchController.setState(player.getNickname(), StateName.STARTING_TURN);

        assertThrows(RetryException.class, ()-> matchController.leaderActivation(player.getNickname(), "L20"));
        matchController.leaderActivation(player.getNickname(), getKeyByValue(map, player.getHandLeaders().get(0)));
        assertEquals(card,player.getPersonalBoard().getActiveLeaders().get(0));
        assertFalse(player.getHandLeaders().contains(card));

    }

    @Test
    public void leaderDiscardingTest() throws RetryException {
        initializationCosts();
        player = matchController.getCurrentPlayer();
        List<LeaderCard> initialLeaders = new ArrayList<>();
        initialLeaders.addAll(player.getHandLeaders());
        Card card = initialLeaders.get(0);
        matchController.setState(player.getNickname(), StateName.STARTING_TURN);
        assertThrows(RetryException.class, ()-> matchController.leaderActivation(player.getNickname(), "L20"));
        matchController.leaderDiscarding(player.getNickname(), getKeyByValue(map, player.getHandLeaders().get(0)));
        assertEquals(initialLeaders.get(1), player.getHandLeaders().get(0));
        assertEquals(initialLeaders.get(2), player.getHandLeaders().get(1));
        assertEquals(initialLeaders.get(3), player.getHandLeaders().get(2));
        assertFalse(player.getHandLeaders().contains(card));
    }

    @Test
    public void marketDrawTest() throws RetryException, NegativeQuantityException {
        initialization();
        player = matchController.getCurrentPlayer();
        match = matchController.getMatch();
        matchController.setState(player.getNickname(), StateName.STARTING_TURN);
        assertThrows(RetryException.class, ()->matchController.marketDraw(player.getNickname(), true, 18));

        player.setHandLeaders(new ArrayList<>(List.of(new LeaderCard(List.of(new PhysicalResource(ResType.STONE, 0)), 5, new WhiteMarbleEffect(new PhysicalResource(ResType.STONE, 1))),
                new LeaderCard(List.of(new PhysicalResource(ResType.STONE, 0)), 5, new WhiteMarbleEffect(new PhysicalResource(ResType.SHIELD, 1))))));
        player.activateLeader(player.getHandLeaders().get(0));
        StateName stateName = StateName.RESOURCES_PLACEMENT;
        Marble[][] board = match.getMarket().getBoard();
        for (int i=0; i<4; i++)
            if(board[2][i] instanceof WhiteMarble)
                stateName = StateName.MARKET_ACTION;

        matchController.marketDraw(player.getNickname(), true, 3);
        assertTrue(player.getPersonalBoard().getWarehouse().getBuffer().size() > 0);

        assertEquals(stateName, matchController.getCurrentState(player.getNickname()) );
    }

    @Test
    public void whiteMarbleConversionTest() throws RetryException, NegativeQuantityException {
        initialization();
        player = matchController.getCurrentPlayer();
        match = matchController.getMatch();
        matchController.setState(player.getNickname(), StateName.MARKET_ACTION);

        player.setHandLeaders(new LinkedList<>(Arrays.asList(
                new LeaderCard(Arrays.asList(new PhysicalResource(ResType.STONE, 0)), 5, new WhiteMarbleEffect(new PhysicalResource(ResType.STONE, 1))),
                new LeaderCard(Arrays.asList(new PhysicalResource(ResType.STONE, 0)), 5, new WhiteMarbleEffect(new PhysicalResource(ResType.COIN, 1)))
        )));

        player.activateLeader(player.getHandLeaders().get(0));
        player.activateLeader(player.getHandLeaders().get(0));

        matchController.setWhiteMarblesDrawn(3);

        List<PhysicalResource> whiteResources = new LinkedList<>(Arrays.asList(new PhysicalResource(ResType.STONE, 1),
                new PhysicalResource(ResType.SHIELD, 3), new PhysicalResource(ResType.COIN, 1), new PhysicalResource(ResType.COIN,2)));

        assertThrows(RetryException.class, ()-> matchController.whiteMarblesConversion(player.getNickname(), whiteResources));

        whiteResources.remove(3);

        assertThrows(RetryException.class, ()-> matchController.whiteMarblesConversion(player.getNickname(), whiteResources));
        assertEquals(1, player.getPersonalBoard().getWarehouse().getBuffer().size());

        whiteResources.remove(1);
        whiteResources.remove(1);

        assertThrows(RetryException.class,()->matchController.whiteMarblesConversion(player.getNickname(), whiteResources));
        assertEquals(1, player.getPersonalBoard().getWarehouse().getBuffer().size());

        whiteResources.add(new PhysicalResource(ResType.STONE, 1));
        matchController.whiteMarblesConversion(player.getNickname(), whiteResources);
        assertEquals(3, player.getPersonalBoard().getWarehouse().getBuffer().size());
    }

    @Test
    public void testWareHouseInsertion() throws RetryException, NegativeQuantityException, InvalidCardRequestException {
        initialization();
        player = matchController.getCurrentPlayer();
        match = matchController.getMatch();
        matchController.setState(player.getNickname(), StateName.RESOURCES_PLACEMENT);
        PhysicalResource resource = new PhysicalResource(ResType.COIN,2);
        PhysicalResource resource1 = new PhysicalResource(ResType.SHIELD,1);

        player.addToWarehouse(resource);
        player.addToWarehouse(resource1);

        matchController.warehouseInsertion(player.getNickname(), new ArrayList<>(Arrays.asList(resource,resource1,new PhysicalResource(ResType.COIN,2))));
        assertEquals(resource,player.getPersonalBoard().getWarehouse().getWarehouseDisposition().get(1));
        assertEquals(resource.getQuantity(),player.getPersonalBoard().getWarehouse().getWarehouseDisposition().get(1).getQuantity());
        assertEquals(resource1,player.getPersonalBoard().getWarehouse().getWarehouseDisposition().get(0));
        assertEquals(StateName.END_TURN,matchController.getCurrentState(player.getNickname()));

        player.payFromWarehouse(resource,2);
        player.payFromWarehouse(resource1,1);

        player.addToWarehouse(resource);
        player.addToWarehouse(resource1);

        matchController.setState(player.getNickname(), StateName.RESOURCES_PLACEMENT);
        assertThrows(RetryException.class, ()->matchController.warehouseInsertion(player.getNickname(), new ArrayList<>(Arrays.asList(resource))));

        assertEquals(StateName.RESOURCES_PLACEMENT,matchController.getCurrentState(player.getNickname()));
    }

    @Test
    public void devCardDrawTest() throws RetryException {
        initializationCosts();
        player = matchController.getCurrentPlayer();
        match = matchController.getMatch();
        matchController.setState(player.getNickname(), StateName.STARTING_TURN);

        assertThrows(RetryException.class, ()-> matchController.devCardDraw(player.getNickname(), 1, 1));

        DevelopmentCard card = match.getCardGrid().peek(2, 1);
        List<PhysicalResource> costs = card.getPrice();
        for(PhysicalResource r : costs)
            player.addToStrongBox(r);

        assertThrows(RetryException.class, ()-> matchController.devCardDraw(player.getNickname(), 8,8));
        assertThrows(RetryException.class, ()-> matchController.devCardDraw(player.getNickname(), 2,1));

        card = match.getCardGrid().peek(1, 1);
        costs = card.getPrice();
        for(PhysicalResource r : costs)
            player.addToStrongBox(r);

        matchController.devCardDraw(player.getNickname(), 1, 1);
        assertEquals(StateName.BUY_DEV_ACTION, matchController.getCurrentState(player.getNickname()));
        assertEquals(card, player.getTempDevCard());
    }

    @Test
    public void devCardPlacementTest() throws RetryException{
        initializationCosts();
        player = matchController.getCurrentPlayer();
        match = matchController.getMatch();
        matchController.setState(player.getNickname(), StateName.PLACE_DEV_CARD);

        DevelopmentCard card = match.getCardGrid().peek(1, 1);
        player.setTempDevCard(card);

        assertThrows(RetryException.class, ()-> matchController.devCardPlacement(player.getNickname(), 8));
        matchController.devCardPlacement(player.getNickname(), 1);
        assertEquals(card, player.getPersonalBoard().getDevCardSlots().getTop().get(0));


    }

    @Test
    public void paymentsTest() throws RetryException, InvalidQuantityException, ShelfInsertException {
        initializationCosts();
        player = matchController.getCurrentPlayer();
        match = matchController.getMatch();
        matchController.setState(player.getNickname(), StateName.BUY_DEV_ACTION);

        DevelopmentCard card = match.getCardGrid().peek(2, 1);
        player.setTempDevCard(card);
        List<PhysicalResource> oldWarehouse;
        Map<ResType, Integer> oldStrongbox;

        Map<Integer, PhysicalResource> warehouseCosts = new HashMap<>();
        List<PhysicalResource> strongboxCosts = new ArrayList<>();

        oldWarehouse = player.getPersonalBoard().getWarehouse().getWarehouseDisposition();
        oldStrongbox = player.getPersonalBoard().getStrongBox().getResources();

        assertThrows(RetryException.class, ()-> matchController.payments(player.getNickname(), card.getPrice(), null));
        assertEquals(oldWarehouse, player.getPersonalBoard().getWarehouse().getWarehouseDisposition());
        assertEquals(oldStrongbox, player.getPersonalBoard().getStrongBox().getResources());

        for (PhysicalResource resource : card.getPrice()){
            player.addToStrongBox(resource);
            strongboxCosts.add(resource);
        }

        oldWarehouse = WarehouseDecorator.clone(player.getPersonalBoard().getWarehouse()).getWarehouseDisposition();
        oldStrongbox = StrongBox.clone(player.getPersonalBoard().getStrongBox()).getResources();

        if(strongboxCosts.get(0).getQuantity() <= 3) {
            warehouseCosts.put(3, strongboxCosts.get(0));
            strongboxCosts.remove(0);
        }
        else {
            PhysicalResource resource = strongboxCosts.remove(0);
            warehouseCosts.put(3, new PhysicalResource(resource.getType(), 3));
            strongboxCosts.add(0, new PhysicalResource(resource.getType(), resource.getQuantity()-3));
        }

        assertThrows(RetryException.class, ()-> matchController.payments(player.getNickname(), strongboxCosts, warehouseCosts));
        assertEquals(oldWarehouse, player.getPersonalBoard().getWarehouse().getWarehouseDisposition());
        assertEquals(oldStrongbox, player.getPersonalBoard().getStrongBox().getResources());

        player.addToWarehouse(warehouseCosts.get(3));
        player.moveIntoWarehouse(warehouseCosts.get(3), 3);

        matchController.payments(player.getNickname(), strongboxCosts, warehouseCosts);
        assertEquals(StateName.PLACE_DEV_CARD, matchController.getCurrentState(player.getNickname()));

        initializationCosts();
        player = matchController.getCurrentPlayer();
        match = matchController.getMatch();
        matchController.setState(player.getNickname(), StateName.PRODUCTION_ACTION);

        List<PhysicalResource> costs = new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 1), new PhysicalResource(ResType.COIN, 2)));
        List<Resource> earnings = new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 3), new FaithPoint(2)));
        Production production = new Production(costs, earnings);

        strongboxCosts.clear();
        strongboxCosts.add(new PhysicalResource(ResType.COIN, 1));
        warehouseCosts.clear();
        warehouseCosts.put(1, new PhysicalResource(ResType.COIN, 1));

        player.setTempProduction(production);

        assertThrows(RetryException.class, ()-> matchController.payments(player.getNickname(), strongboxCosts, warehouseCosts));
        strongboxCosts.remove(0);
        strongboxCosts.add(new PhysicalResource(ResType.COIN, 2));
        warehouseCosts.replace(1, new PhysicalResource(ResType.STONE, 1));

        assertThrows(RetryException.class, ()-> matchController.payments(player.getNickname(), strongboxCosts, warehouseCosts));

        player.addToStrongBox(strongboxCosts.get(0));
        player.addToWarehouse(warehouseCosts.get(1));
        player.moveIntoWarehouse(warehouseCosts.get(1), 1);

        matchController.payments(player.getNickname(), strongboxCosts, warehouseCosts);

        assertEquals(3, player.getPersonalBoard().getStrongBox().getNumberOf(ResType.SHIELD));
        assertEquals(2, player.getPersonalBoard().getFaithPath().getPosition());

    }

    @Test
    public void productionTest() throws RetryException, NegativeQuantityException {
        initialization();
        player = matchController.getCurrentPlayer();
        match = matchController.getMatch();
        matchController.setState(player.getNickname(), StateName.STARTING_TURN);
        Production production;

        player.setHandLeaders(new LinkedList<>(Arrays.asList(
                new LeaderCard(Arrays.asList(new PhysicalResource(ResType.STONE, 0)), 4, new ProductionEffect(new Production(
                        List.of(new PhysicalResource(ResType.UNKNOWN, 1)), List.of(new PhysicalResource(ResType.UNKNOWN,1), new FaithPoint(1))))),
                new LeaderCard(Arrays.asList(new PhysicalResource(ResType.STONE, 0)), 5, new ProductionEffect(new Production(
                        List.of(new PhysicalResource(ResType.COIN, 1)), List.of(new PhysicalResource(ResType.UNKNOWN, 2), new PhysicalResource(ResType.SHIELD, 3)))))
        )));

        player.activateLeader(player.getHandLeaders().get(0));
        player.activateLeader(player.getHandLeaders().get(0));

        List<String> cardIds = new ArrayList<>(List.of("L77", getKeyByValue(map, player.getPersonalBoard().getActiveLeaders().get(0))));

        assertThrows(RetryException.class, ()->matchController.production(player.getNickname(), cardIds, new Production(new ArrayList<>(), new ArrayList<>())));

        cardIds.add("BASICPROD");
        cardIds.remove("L77");
        List<PhysicalResource> prodCosts =  new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1), new PhysicalResource(ResType.SHIELD, 2)));
        List<Resource> prodEarnings =  new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 3), new PhysicalResource(ResType.COIN, 2)));

        assertThrows(RetryException.class, ()-> matchController.production(player.getNickname(), cardIds, new Production(prodCosts, prodEarnings)));

        prodEarnings.remove(0);

        cardIds.add("BASICPROD");
        assertThrows(RetryException.class, ()-> matchController.production(player.getNickname(), cardIds, new Production(prodCosts, prodEarnings)));

        cardIds.add("BASICPROD");
        player.addToStrongBox(new PhysicalResource(ResType.COIN, 1));
        player.addToStrongBox(new PhysicalResource(ResType.SHIELD, 2));

        production = new Production(prodCosts, prodEarnings);
        matchController.production(player.getNickname(), cardIds, production);

        prodEarnings.add(new FaithPoint(1));
        production = new Production(prodCosts, prodEarnings);

        assertEquals(production, player.getTempProduction());
        assertEquals(StateName.PRODUCTION_ACTION, matchController.getCurrentState(player.getNickname()));

    }

    @Test
    public void testNextTurn() throws RetryException {
        initialization();
        player = matchController.getCurrentPlayer();

        assertThrows(RetryException.class, ()->matchController.nextTurn(player.getNickname()));

        matchController.setState(player.getNickname(), StateName.END_TURN);
        matchController.nextTurn(player.getNickname());

        assertEquals(StateName.WAITING_FOR_TURN, matchController.getCurrentState(player.getNickname()));
        assertEquals(StateName.WAITING_FOR_TURN, matchController.getCurrentState(matchController.getCurrentPlayer().getNickname()));

        initialization();
        player = matchController.getCurrentPlayer();
        matchController.setState(player.getNickname(), StateName.END_TURN);
        matchController.nextTurn(player.getNickname());

        player = matchController.getCurrentPlayer();
        matchController.setState(player.getNickname(), StateName.END_TURN);
        matchController.nextTurn(player.getNickname());

        player = matchController.getCurrentPlayer();
        matchController.setLastRound(true);
        matchController.setState(player.getNickname(), StateName.END_TURN);
        matchController.nextTurn(player.getNickname());

        player = matchController.getCurrentPlayer();
        matchController.setState(player.getNickname(), StateName.END_TURN);
        matchController.nextTurn(player.getNickname());

        assertEquals(StateName.REMATCH_OFFER, matchController.getCurrentState(player.getNickname()));


    }

}
