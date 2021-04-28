package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.*;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.WarehouseDecorator;
import it.polimi.ingsw.network.message.ctosmessage.MarketDrawMessage;
import it.polimi.ingsw.network.message.ctosmessage.SwitchShelfMessage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurnControllerTest {
    private Match match;
    private TurnController controller;
    private Map cardMap;

    public void initialization() throws WrongSettingException, SingleMatchException {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"), new Player("player2"), new Player("player3")));

        match = new MultiMatch(players,"src/test/resources/StandardConfiguration.json");
        cardMap = new HashMap<>();
        for (int i=1; i<=match.getMatchConfiguration().getAllDevCards().size(); i++)
            cardMap.put("D"+i,match.getMatchConfiguration().getAllDevCards().get(i-1));
        for (int i=1; i<=match.getMatchConfiguration().getAllLeaderCards().size(); i++)
            cardMap.put("L"+i,match.getMatchConfiguration().getAllLeaderCards().get(i-1));

        controller = new TurnController(match.getCurrentPlayer(),match,cardMap);

    }

    @Test
    public void testSwitchShelf() throws NegativeQuantityException, InvalidOperationException, WrongSettingException, SingleMatchException {
        initialization();
        PhysicalResource resource = new PhysicalResource(ResType.SERVANT,2);
        PhysicalResource resource1 = new PhysicalResource(ResType.SHIELD,2);
        PhysicalResource resource2 = new PhysicalResource(ResType.COIN,1);
        match.getCurrentPlayer().addToWarehouse(resource);
        match.getCurrentPlayer().addToWarehouse(resource1);
        match.getCurrentPlayer().moveIntoWarehouse(new PhysicalResource(ResType.SERVANT,2),2);
        match.getCurrentPlayer().moveIntoWarehouse(new PhysicalResource(ResType.SHIELD,2),3);

        controller.newOperation(new SwitchShelfMessage("player1",2,3));
        assertTrue(match.getCurrentPlayer().getPersonalBoard().getWarehouse().getWarehouseDisposition().get(1).equals(resource1));
        assertTrue(match.getCurrentPlayer().getPersonalBoard().getWarehouse().getWarehouseDisposition().get(2).equals(resource));

        match.getCurrentPlayer().addToWarehouse(resource2);
        match.getCurrentPlayer().moveIntoWarehouse(resource2,1);

        Warehouse oldWh = WarehouseDecorator.clone(match.getCurrentPlayer().getPersonalBoard().getWarehouse());

        controller.newOperation(new SwitchShelfMessage("player1",8,1));
        assertEquals(oldWh.getWarehouseDisposition(),match.getCurrentPlayer().getPersonalBoard().getWarehouse().getWarehouseDisposition());

        controller.newOperation(new SwitchShelfMessage("player1",2,1));
        assertEquals(oldWh.getWarehouseDisposition(),match.getCurrentPlayer().getPersonalBoard().getWarehouse().getWarehouseDisposition());


    }


}
