package it.polimi.ingsw.model.match.player.personalBoard;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.essentials.leader.*;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.FaithPathTest;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class PersonalBoardTest extends FaithPathTest
{
    /*
    Tests inserting operation of active leaders and production leaders in both arrays.
    Also tests the correct behaviour of every leader activated.
     */
    @Test
    public void personalBoardTest() throws NegativeQuantityException, FaithPathCreationException
    {
        ArrayList<PhysicalResource> bcost= new ArrayList<>();
        ArrayList bearnings= new ArrayList<>();
        bcost.add(new PhysicalResource(ResType.UNKNOWN,2));
        bearnings.add(new PhysicalResource(ResType.COIN,1));

        Production bp = new Production(bcost, bearnings);
        PersonalBoard personalBoard = new PersonalBoard(FaithPathTest.generatePath(), 0, bp);

        LeaderCard l1 = new LeaderCard(bearnings, 1, new ProductionEffect(bp));
        LeaderCard l2 = new LeaderCard(bearnings, 2, new ProductionEffect(bp));
        LeaderCard l3 = new LeaderCard(bearnings, 1, new DiscountEffect(new PhysicalResource(ResType.STONE,1)));
        LeaderCard l4 = new LeaderCard(bearnings, 1, new DiscountEffect(new PhysicalResource(ResType.STONE,1)));
        LeaderCard l5 = new LeaderCard(bearnings, 3, new ProductionEffect(bp));
        LeaderCard l6 = new LeaderCard(bearnings, 1, new SlotEffect(new PhysicalResource(ResType.STONE,2)));
        LeaderCard l7 = new LeaderCard(bearnings, 1, new WhiteMarbleEffect(new PhysicalResource(ResType.COIN,1)));
        LeaderCard l8 = new LeaderCard(bearnings, 1, new WhiteMarbleEffect(new PhysicalResource(ResType.SERVANT,1)));

        l1.activate(personalBoard);
        l2.activate(personalBoard);
        l3.activate(personalBoard);
        l4.activate(personalBoard);
        l5.activate(personalBoard);
        l6.activate(personalBoard);
        l7.activate(personalBoard);
        l8.activate(personalBoard);

        //"correct leaders disposition" test:
        assertEquals(Arrays.asList(l1,l2,l3,l4,l5,l6,l7,l8), personalBoard.getActiveLeaders());
        assertEquals(Arrays.asList(l1,l2,l5), personalBoard.getActiveProductionLeaders());

        //"correct leader effect activation" effect:
        assertEquals(4, personalBoard.getWarehouse().getWarehouseDisposition().size());
        assertEquals(2, personalBoard.getWhiteMarbleConversions().size());
        assertEquals(2, personalBoard.getDiscountMap().getDiscountMap().get(ResType.STONE));
    }

}