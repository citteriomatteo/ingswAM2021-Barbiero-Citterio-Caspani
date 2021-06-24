package it.polimi.ingsw.gameLogic.model.match.player.personalBoard;

import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.Production;
import it.polimi.ingsw.gameLogic.model.essentials.ResType;
import it.polimi.ingsw.gameLogic.model.essentials.leader.*;
import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath.FaithPathTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalBoardTest extends FaithPathTest
{
    /*
    Tests inserting operation of active leaders and production leaders in both arrays.
    Also tests the correct behaviour of every leader activated.
     */
    @Test
    public void personalBoardTest() throws NegativeQuantityException, WrongSettingException {
        ArrayList<PhysicalResource> bcost= new ArrayList<>();
        ArrayList bearnings= new ArrayList<>();
        bcost.add(new PhysicalResource(ResType.UNKNOWN,2));
        bearnings.add(new PhysicalResource(ResType.COIN,1));

        Player player = new Player("player1");
        Match match = new SingleMatch(player);
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());

        Production bp = new Production(bcost, bearnings);
   //     PersonalBoard personalBoard = new PersonalBoard(FaithPathTest.generatePath(), 0, bp, player);

        LeaderCard l1 = new LeaderCard(bearnings, 1, new ProductionEffect(bp));
        LeaderCard l2 = new LeaderCard(bearnings, 2, new ProductionEffect(bp));
        LeaderCard l3 = new LeaderCard(bearnings, 1, new DiscountEffect(new PhysicalResource(ResType.STONE,1)));
        LeaderCard l4 = new LeaderCard(bearnings, 1, new DiscountEffect(new PhysicalResource(ResType.STONE,1)));
        LeaderCard l5 = new LeaderCard(bearnings, 3, new ProductionEffect(bp));
        LeaderCard l6 = new LeaderCard(bearnings, 1, new SlotEffect(new PhysicalResource(ResType.STONE,2)));
        LeaderCard l7 = new LeaderCard(bearnings, 1, new WhiteMarbleEffect(new PhysicalResource(ResType.COIN,1)));
        LeaderCard l8 = new LeaderCard(bearnings, 1, new WhiteMarbleEffect(new PhysicalResource(ResType.SERVANT,1)));

        l1.activate(player.getPersonalBoard());
        l2.activate(player.getPersonalBoard());
        l3.activate(player.getPersonalBoard());
        l4.activate(player.getPersonalBoard());
        l5.activate(player.getPersonalBoard());
        l6.activate(player.getPersonalBoard());
        l7.activate(player.getPersonalBoard());
        l8.activate(player.getPersonalBoard());

        //"correct leaders disposition" test:
        assertEquals(Arrays.asList(l1,l2,l3,l4,l5,l6,l7,l8), player.getPersonalBoard().getActiveLeaders());
       // assertEquals(Arrays.asList(l1,l2), personalBoard.getActiveLeaders());
        assertEquals(Arrays.asList(l1,l2,l5), player.getPersonalBoard().getActiveProductionLeaders());
     //   assertEquals(Arrays.asList(l1,l2), personalBoard.getActiveProductionLeaders());

        //"correct leader effect activation" effect:
        assertEquals(4, player.getPersonalBoard().getWarehouse().getWarehouseDisposition().size());
        assertEquals(2, player.getPersonalBoard().getWhiteMarbleConversions().size());
        assertEquals(2, player.getPersonalBoard().getDiscountMap().getDiscountMap().get(ResType.STONE));
        // 4,2,2
        //3,0,0
    }

}