package it.polimi.ingsw.model.match.player.personalBoard.warehouse;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExtraShelfWarehouseTest
{
    /*
    Tests if extra shelves' functionalities, in terms of correct moves, work well.
    Also tests the delegation of the move operation, if needed.
     */
    @Test
    public void extraWarehouseDispositionTest() throws NegativeQuantityException, InvalidOperationException
    {
        Random rnd = new Random();
        Warehouse wh = new ConcreteWarehouse();
        Warehouse extrawh1 = new ExtraShelfWarehouse(wh, new PhysicalResource(ResType.values()[rnd.nextInt(5)], 2));
        Warehouse extrawh2 = new ExtraShelfWarehouse(extrawh1, new PhysicalResource(ResType.values()[rnd.nextInt(5)], 2));
        List<PhysicalResource> shelves = new ArrayList<>();

        PhysicalResource res1 = new PhysicalResource(ResType.values()[1], rnd.nextInt(2));
        shelves.add(res1);
        PhysicalResource res2 = new PhysicalResource(ResType.values()[2], rnd.nextInt(3));
        shelves.add(res2);
        PhysicalResource res3 = new PhysicalResource(ResType.values()[3], rnd.nextInt(4));
        shelves.add(res3);
        PhysicalResource res4 = new PhysicalResource(extrawh2.getWarehouseDisposition().get(3).getType(), rnd.nextInt(3));
        shelves.add(res4);
        PhysicalResource res5 = new PhysicalResource(extrawh2.getWarehouseDisposition().get(4).getType(), rnd.nextInt(3));
        shelves.add(res5);
        extrawh2.marketDraw(res1); extrawh2.marketDraw(res2); extrawh2.marketDraw(res3); extrawh2.marketDraw(res4); extrawh2.marketDraw(res5);
        extrawh2.moveInShelf(res1, 1);
        extrawh2.moveInShelf(res2, 2);
        extrawh2.moveInShelf(res3, 3);
        extrawh2.moveInShelf(res4, 4);
        extrawh2.moveInShelf(res5, 5);
        assertEquals(shelves, extrawh2.getWarehouseDisposition());
        assertEquals(extrawh2.getBuffer().size(), 0);
    }
    /*
    Tests move of a resource that is bigger than the available space, or not of the extraShelf type.
    Tests moves without inserting the resource in the marketBuffer.
    Tests moves on not existing shelves.
     */
    @Test
    public void extraImpossibleMovesTest() throws NegativeQuantityException
    {
        Warehouse wh = new ConcreteWarehouse();
        Warehouse extrawh1 = new ExtraShelfWarehouse(wh, new PhysicalResource(ResType.COIN, 2));
        Warehouse extrawh2 = new ExtraShelfWarehouse(extrawh1, new PhysicalResource(ResType.STONE, 2));

        //"single bigger move" test:
        PhysicalResource res1 = new PhysicalResource(ResType.STONE,3);
        extrawh2.marketDraw(res1);
        assertThrows(ShelfInsertException.class, ()->extrawh2.moveInShelf(res1,5));
        extrawh2.cleanMarketBuffer(res1);

        //"not expected type move" test:
        PhysicalResource res2 = new PhysicalResource(ResType.SERVANT,1);
        extrawh2.marketDraw(res2);
        assertThrows(ShelfInsertException.class, ()->extrawh2.moveInShelf(res2,5));
        extrawh2.cleanMarketBuffer(res2);

        //"unknown shelf move" test:
        PhysicalResource res3 = new PhysicalResource(ResType.COIN,1);
        extrawh2.marketDraw(res3);
        assertThrows(ShelfInsertException.class, ()->extrawh1.moveInShelf(res3,5));
        extrawh2.cleanMarketBuffer(res3);

        //"move with no marketDraw" test:
        PhysicalResource res4 = new PhysicalResource(ResType.STONE,1);
        assertThrows(InvalidOperationException.class, ()->extrawh2.moveInShelf(res4,5));
        extrawh2.cleanMarketBuffer(res4);
    }

    /*
    Tests take affordability in terms of concreteWarehouse shelves and extraShelf, then tests emptying procedure.
    Tests take operation exception in the extraShelf.
     */
    @Test
    public void fullExtraTakeTest() throws NegativeQuantityException, InvalidOperationException
    {
        Warehouse wh = new ConcreteWarehouse();
        PhysicalResource res1 = new PhysicalResource(ResType.COIN, 2);
        wh.marketDraw(res1); wh.moveInShelf(res1,3);
        Warehouse extrawh1 = new ExtraShelfWarehouse(wh, new PhysicalResource(ResType.STONE,3));

        //"concreteWarehouse take and clean (from extraWarehouse)" test:
        assertEquals(new PhysicalResource(ResType.COIN,1),extrawh1.take(3,1));
        assertEquals(extrawh1.getWarehouseDisposition().get(2).getQuantity(), 1);
        assertEquals(new PhysicalResource(ResType.COIN,0),extrawh1.take(3,1));
        assertEquals(extrawh1.getWarehouseDisposition().get(2).getQuantity(), 0);

        //"exception thrown" test:
        assertThrows(NotEnoughResourcesException.class, ()->extrawh1.take(1, 1)); //random take try on an empty basic shelf.
        PhysicalResource res2 = new PhysicalResource(ResType.STONE, 3);
        extrawh1.marketDraw(res2); extrawh1.moveInShelf(res2,4);
        assertThrows(NotEnoughResourcesException.class, ()->extrawh1.take(4, 4));
        extrawh1.cleanMarketBuffer(res2);

        //"extraShelf take and clean" test:
        assertEquals(new PhysicalResource(ResType.STONE,1),extrawh1.take(4,1));
        assertEquals(extrawh1.getWarehouseDisposition().get(3).getQuantity(), 2);
        assertEquals(new PhysicalResource(ResType.STONE,2),extrawh1.take(4,2));
        assertEquals(extrawh1.getWarehouseDisposition().get(3).getQuantity(), 0);
    }

    /*
    Tests switch between two basic shelves, two extra shelves and basic-extra combination.
    Tests switch behaviour, in the three combinations, when the operation isn't allowed (no enough space).
     */
    @Test
    public void ExtraWarehouseSwitchShelfTest() throws NegativeQuantityException, InvalidOperationException
    {
        Warehouse wh = new ConcreteWarehouse();
        Warehouse extrawh1 = new ExtraShelfWarehouse(wh, new PhysicalResource(ResType.values()[1], 2));
        Warehouse extrawh2 = new ExtraShelfWarehouse(extrawh1, new PhysicalResource(ResType.values()[4], 3));

        PhysicalResource res1 = new PhysicalResource(ResType.values()[1], 1);
        PhysicalResource res2 = new PhysicalResource(ResType.values()[2], 0);
        PhysicalResource res3 = new PhysicalResource(ResType.values()[3], 2);
        PhysicalResource res4 = new PhysicalResource(ResType.values()[1], 0); // ->2
        PhysicalResource res5 = new PhysicalResource(ResType.values()[4], 2); // ->3

        extrawh2.marketDraw(res1); extrawh2.marketDraw(res2); extrawh2.marketDraw(res3); extrawh2.marketDraw(res4); extrawh2.marketDraw(res5);
        extrawh2.moveInShelf(res1, 1);
        extrawh2.moveInShelf(res2, 2);
        extrawh2.moveInShelf(res3, 3);
        extrawh2.moveInShelf(res4, 4);
        extrawh2.moveInShelf(res5, 5);

        //"basic-basic switch" ok and not ok test:
        List<PhysicalResource> whbefore = extrawh2.getWarehouseDisposition();
        //ok
        extrawh2.switchShelf(1,2);
        assertEquals(extrawh2.getWarehouseDisposition().get(1), whbefore.get(0));
        assertEquals(extrawh2.getWarehouseDisposition().get(0), whbefore.get(1));
        extrawh2.switchShelf(1,2);
        //not ok
        assertThrows(ShelfInsertException.class, ()->extrawh2.switchShelf(1,3));
        assertEquals(whbefore, extrawh2.getWarehouseDisposition());

        //"basic-extra switch" ok and not ok test:
        //ok
        extrawh2.switchShelf(1,4);
        assertEquals(extrawh2.getWarehouseDisposition().get(0), whbefore.get(3));
        assertEquals(extrawh2.getWarehouseDisposition().get(3), whbefore.get(0));
        extrawh2.switchShelf(1,4);
        assertEquals(whbefore, extrawh2.getWarehouseDisposition());

        //not ok
        assertThrows(ShelfInsertException.class,()->extrawh2.switchShelf(2,4));
        assertEquals(whbefore, extrawh2.getWarehouseDisposition());

        res4 = new PhysicalResource(ResType.values()[1], 2);
        extrawh2.marketDraw(res4);
        extrawh2.moveInShelf(res4, 4);
        assertThrows(ShelfInsertException.class, ()->extrawh2.switchShelf(1,4));
        extrawh2.take(4,2);
        assertEquals(whbefore, extrawh2.getWarehouseDisposition());

        //"extra-extra switch" ok and not ok test:
        Warehouse wh2 = new ConcreteWarehouse();
        Warehouse extrawh12 = new ExtraShelfWarehouse(wh2, new PhysicalResource(ResType.values()[1], 1));
        Warehouse extrawh22 = new ExtraShelfWarehouse(extrawh12, new PhysicalResource(ResType.values()[1], 2));

        //ok
        whbefore = extrawh22.getWarehouseDisposition();
        extrawh22.marketDraw(res1);
        extrawh22.moveInShelf(res1, 4);
        extrawh22.marketDraw(res1);
        extrawh22.moveInShelf(res1, 5);

        extrawh22.switchShelf(4,5);
        assertEquals(extrawh22.getWarehouseDisposition().get(3), whbefore.get(4));
        assertEquals(extrawh22.getWarehouseDisposition().get(4), whbefore.get(3));
        extrawh22.switchShelf(4,5);
        assertEquals(extrawh22.getWarehouseDisposition(),whbefore);

        //not ok
        extrawh22.marketDraw(res1);
        extrawh22.moveInShelf(res1,5);
        assertThrows(ShelfInsertException.class, ()->extrawh22.switchShelf(4,5));
        assertEquals(extrawh22.getWarehouseDisposition(),whbefore);



    }

    @Test
    public void extraGetNumberOfTest() throws NegativeQuantityException, InvalidOperationException
    {
        Warehouse wh = new ConcreteWarehouse();
        List<PhysicalResource> l = new ArrayList<>();
        l.add(new PhysicalResource(ResType.COIN,1));
        wh.marketDraw(l.get(0)); wh.moveInShelf(l.get(0), 1);
        l.add(new PhysicalResource(ResType.STONE,2));
        wh.marketDraw(l.get(1)); wh.moveInShelf(l.get(1),2);
        l.add(new PhysicalResource(ResType.SERVANT,3));
        wh.marketDraw(l.get(2)); wh.moveInShelf(l.get(2),3);
        Warehouse extrawh1 = new ExtraShelfWarehouse(wh, new PhysicalResource(ResType.COIN, 2));
        Warehouse extrawh2 = new ExtraShelfWarehouse(extrawh1, new PhysicalResource(ResType.SERVANT, 2));
        l.add(new PhysicalResource(ResType.COIN,1));
        wh.marketDraw(l.get(3)); extrawh2.moveInShelf(l.get(3),4);
        l.add(new PhysicalResource(ResType.SERVANT,2));
        wh.marketDraw(l.get(4)); extrawh2.moveInShelf(l.get(4),5);

        assertEquals(extrawh2.getNumberOf(ResType.COIN),l.get(0).getQuantity()+l.get(3).getQuantity());
        assertEquals(extrawh2.getNumberOf(ResType.STONE),l.get(1).getQuantity());
        assertEquals(extrawh2.getNumberOf(ResType.SERVANT),l.get(2).getQuantity()+l.get(4).getQuantity());
        assertEquals(extrawh2.getNumberOf(ResType.UNKNOWN),0);
    }

    @Test
    public void wrongBufferDiscardTest() throws SingleMatchException, WrongSettingException, NegativeQuantityException, InvalidOperationException
    {
        Player player = new Player("player1");
        Player player1 = new Player("player2");
        Match match = new MultiMatch(Arrays.asList(player, player1),"src/test/resources/StandardConfiguration.json");

        player.getPersonalBoard().warehouseEvolution(new PhysicalResource(ResType.COIN,2));
        player.addToWarehouse(new PhysicalResource(ResType.COIN,5));
        player.moveIntoWarehouse(new PhysicalResource(ResType.COIN,2),2);

        //There must be space in the ExtraShelf yet
        assertThrows(InvalidOperationException.class, ()->player.getPersonalBoard().getWarehouse().discardRemains());
        player.getPersonalBoard().getWarehouse().switchShelf(2,3);
        player.moveIntoWarehouse(new PhysicalResource(ResType.COIN,2),4);

        //Now, after the switch, there must be a place for the remaining Coin in the shelf 3.
        assertThrows(InvalidOperationException.class, ()->player.getPersonalBoard().getWarehouse().discardRemains());
    }
}
