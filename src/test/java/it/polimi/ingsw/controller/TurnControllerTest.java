package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.essentials.leader.SlotEffect;
import it.polimi.ingsw.model.essentials.leader.WhiteMarbleEffect;
import it.polimi.ingsw.model.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.SingleMatchException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.WarehouseDecorator;
import it.polimi.ingsw.network.message.ctosmessage.LeaderActivationMessage;
import it.polimi.ingsw.network.message.ctosmessage.LeaderDiscardingMessage;
import it.polimi.ingsw.network.message.ctosmessage.SwitchShelfMessage;
import it.polimi.ingsw.network.message.ctosmessage.WhiteMarbleConversionMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TurnControllerTest {

    private Match match;
    private TurnController controller;
    private Map<String, Card> cardMap;
    private StateName currentState;

    public void initialization() throws WrongSettingException, SingleMatchException
    {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"), new Player("player2"), new Player("player3")));
        this.match = new MultiMatch(players,"src/test/resources/FreeConfiguration.json");
        this.cardMap = new HashMap<>();
        for (int i=1; i<=match.getMatchConfiguration().getAllDevCards().size(); i++)
             cardMap.put("D"+i,match.getMatchConfiguration().getAllDevCards().get(i-1));
        for (int i=1; i<=match.getMatchConfiguration().getAllLeaderCards().size(); i++)
            cardMap.put("L"+i,match.getMatchConfiguration().getAllLeaderCards().get(i-1));
        this.controller = new TurnController(match.getCurrentPlayer(),match,cardMap);
        this.currentState = controller.getCurrentState();
    }



    @Test
    public void leaderActivationTest() throws WrongSettingException, SingleMatchException, NegativeQuantityException {

        initialization();

        match.getCurrentPlayer().setHandLeaders(new LinkedList<>(Arrays.asList(
                new LeaderCard(Arrays.asList(new PhysicalResource(ResType.STONE,0)),3,new SlotEffect(new PhysicalResource(ResType.COIN,2))))));

        LeaderCard chosenLeader = match.getCurrentPlayer().getHandLeaders().get(0);

        match.getCurrentPlayer().addToStrongBox(new PhysicalResource(ResType.SHIELD,5));

        controller.newOperation(new LeaderActivationMessage(match.getCurrentPlayer().getNickname(),
                getKeyByValue(cardMap,chosenLeader)));

        assertTrue(match.getCurrentPlayer().getPersonalBoard().getActiveLeaders().contains(chosenLeader) ||
                match.getCurrentPlayer().getPersonalBoard().getActiveProductionLeaders().contains(chosenLeader));
    }

    @Test
    public void leaderDiscardingTest() throws WrongSettingException, SingleMatchException, NegativeQuantityException {

        initialization();

        List<LeaderCard> handLeaders = new ArrayList<>();
        handLeaders.addAll(match.getCurrentPlayer().getHandLeaders());

        controller.newOperation(new LeaderDiscardingMessage(match.getCurrentPlayer().getNickname(),
                getKeyByValue(cardMap,handLeaders.get(0))));

        assertEquals(handLeaders.get(1), match.getCurrentPlayer().getHandLeaders().get(0));
        assertEquals(handLeaders.get(2), match.getCurrentPlayer().getHandLeaders().get(1));
        assertEquals(handLeaders.get(3), match.getCurrentPlayer().getHandLeaders().get(2));
        assertFalse(match.getCurrentPlayer().getHandLeaders().contains(handLeaders.get(0)));
    }


    @Test
    public void whiteMarbleConversionsTest() throws WrongSettingException, SingleMatchException, NegativeQuantityException {
        initialization();

        Player p = match.getCurrentPlayer();
        p.setHandLeaders(new LinkedList<>(Arrays.asList(
                new LeaderCard(Arrays.asList(new PhysicalResource(ResType.STONE, 0)), 5, new WhiteMarbleEffect(new PhysicalResource(ResType.STONE, 1))),
                new LeaderCard(Arrays.asList(new PhysicalResource(ResType.STONE, 0)), 5, new WhiteMarbleEffect(new PhysicalResource(ResType.COIN, 1)))
        )));

        p.activateLeader(p.getHandLeaders().get(0));
        p.activateLeader(p.getHandLeaders().get(0));

        controller.setCurrentState(StateName.MARKET_ACTION);
        controller.setWhiteMarbleDrawn(3);

        List<PhysicalResource> whiteResources = new LinkedList<>(Arrays.asList(new PhysicalResource(ResType.STONE, 1), new PhysicalResource(ResType.SHIELD, 3), new PhysicalResource(ResType.COIN, 1)));

        controller.newOperation(new WhiteMarbleConversionMessage(p.getNickname(), whiteResources));
        assertEquals(1, p.getPersonalBoard().getWarehouse().getBuffer().size());

        whiteResources.remove(1);
        whiteResources.remove(1);
        controller.newOperation(new WhiteMarbleConversionMessage(p.getNickname(), whiteResources));
        assertEquals(1, p.getPersonalBoard().getWarehouse().getBuffer().size());

        whiteResources.add(new PhysicalResource(ResType.STONE, 1));
        controller.newOperation(new WhiteMarbleConversionMessage(p.getNickname(), whiteResources));
        assertEquals(3, p.getPersonalBoard().getWarehouse().getBuffer().size());

    }
        @Test
        public void testSwitchShelf () throws
        NegativeQuantityException, InvalidOperationException, WrongSettingException, SingleMatchException {
            initialization();
            PhysicalResource resource = new PhysicalResource(ResType.SERVANT, 2);
            PhysicalResource resource1 = new PhysicalResource(ResType.SHIELD, 2);
            PhysicalResource resource2 = new PhysicalResource(ResType.COIN, 1);
            match.getCurrentPlayer().addToWarehouse(resource);
            match.getCurrentPlayer().addToWarehouse(resource1);
            match.getCurrentPlayer().moveIntoWarehouse(new PhysicalResource(ResType.SERVANT, 2), 2);
            match.getCurrentPlayer().moveIntoWarehouse(new PhysicalResource(ResType.SHIELD, 2), 3);

            controller.newOperation(new SwitchShelfMessage("player1", 2, 3));
            assertTrue(match.getCurrentPlayer().getPersonalBoard().getWarehouse().getWarehouseDisposition().get(1).equals(resource1));
            assertTrue(match.getCurrentPlayer().getPersonalBoard().getWarehouse().getWarehouseDisposition().get(2).equals(resource));

            match.getCurrentPlayer().addToWarehouse(resource2);
            match.getCurrentPlayer().moveIntoWarehouse(resource2, 1);

            Warehouse oldWh = WarehouseDecorator.clone(match.getCurrentPlayer().getPersonalBoard().getWarehouse());

            controller.newOperation(new SwitchShelfMessage("player1", 8, 1));
            assertEquals(oldWh.getWarehouseDisposition(), match.getCurrentPlayer().getPersonalBoard().getWarehouse().getWarehouseDisposition());

            controller.newOperation(new SwitchShelfMessage("player1", 2, 1));
            assertEquals(oldWh.getWarehouseDisposition(), match.getCurrentPlayer().getPersonalBoard().getWarehouse().getWarehouseDisposition());


        }


    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}
