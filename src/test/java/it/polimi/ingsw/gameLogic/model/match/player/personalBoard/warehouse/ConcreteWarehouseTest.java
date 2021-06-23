package it.polimi.ingsw.gameLogic.model.match.player.personalBoard.warehouse;

import it.polimi.ingsw.gameLogic.exceptions.*;
import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.ResType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ConcreteWarehouseTest
{

    /*
    tests the affordability of moveInShelf, passing through marketDraw, and getWarehouseDisposition methods.
    It also tests marketBuffer emptiness after having moved all the resources.
    */

    @Test
    public void correctWarehouseDispositionTest() throws ShelfInsertException, InvalidQuantityException {
        Random rnd = new Random();

        for(int i=0; i<30; i++)
        {
            Warehouse wh = new ConcreteWarehouse();
            List<PhysicalResource> shelves = new ArrayList<>();

            PhysicalResource res1 = new PhysicalResource(ResType.values()[1], rnd.nextInt(1)+1);
            shelves.add(res1);
            PhysicalResource res2 = new PhysicalResource(ResType.values()[2], rnd.nextInt(2)+1);
            shelves.add(res2);
            PhysicalResource res3 = new PhysicalResource(ResType.values()[3], rnd.nextInt(3)+1);
            shelves.add(res3);
            wh.marketDraw(res1); wh.marketDraw(res2); wh.marketDraw(res3);
            wh.moveInShelf(res1, 1);
            wh.moveInShelf(res2, 2);
            wh.moveInShelf(res3, 3);
            assertEquals(shelves, wh.getWarehouseDisposition());
            assertEquals(wh.getBuffer().size(), 0);
        }
    }

    /*
    Tests exceptions triggers in moveInShelf operations, both all-in-one/two-steps invalid moves.
    Tests the moveInShelf operation with missing elements in the marketBuffer first.
    Also tests its behaviour if the resource is already present on another shelf.
     */
    @Test
    public void impossibleMovesTest() throws ShelfInsertException, InvalidQuantityException
    {
        Random rnd = new Random();

        Warehouse wh = new ConcreteWarehouse();
        PhysicalResource res = new PhysicalResource(ResType.values()[rnd.nextInt(4)+1], 5);
        wh.marketDraw(res);
        assertThrows(ShelfInsertException.class, () -> wh.moveInShelf(res, 1));
        wh.cleanMarketBuffer(res);

        PhysicalResource res1 = new PhysicalResource(ResType.values()[1], 2);
        wh.marketDraw(res1);
        wh.moveInShelf(res1, 3);
        wh.marketDraw(res1);
        assertThrows(ShelfInsertException.class, () -> wh.moveInShelf(res1, 3));
        wh.cleanMarketBuffer(res1);
        PhysicalResource res2 = new PhysicalResource(ResType.values()[1],1);
        wh.marketDraw(res2);
        assertThrows(ShelfInsertException.class, ()->wh.moveInShelf(res2,2));

        Warehouse wh1 = new ConcreteWarehouse();
        assertThrows(InvalidQuantityException.class, () -> wh1.moveInShelf(new PhysicalResource(ResType.values()[rnd.nextInt(4)+1], 1), 1));
    }

    /*
    Tests exception triggering, affordability of the returned element and shelf emptying operation.
     */
    @Test
    public void fullTakeTest()
            throws InvalidQuantityException, ShelfInsertException, NotEnoughResourcesException
    {
        Random rnd = new Random();
        for(int i=0; i<30; i++)
        {
            Warehouse wh = new ConcreteWarehouse();

            //1) exception test (made on the third shelf) -> "no warehouse modify" test
            PhysicalResource res = new PhysicalResource(ResType.values()[rnd.nextInt(4)+1], rnd.nextInt(3)+1);
            wh.marketDraw(res);
            wh.moveInShelf(res, 3);
            PhysicalResource shelfbefore = wh.getWarehouseDisposition().get(2);
            Map<ResType, Integer> whbefore = wh.getWarehouse();
            assertThrows(NotEnoughResourcesException.class, ()->wh.take(3, res.getQuantity()+1));
            assertEquals(shelfbefore, wh.getWarehouseDisposition().get(2));
            assertEquals(whbefore, wh.getWarehouse());

            //2) correct return value test
            assertEquals(res, wh.take(3,res.getQuantity()));

            //3) emptying procedure test
            PhysicalResource res1 = new PhysicalResource(ResType.values()[rnd.nextInt(4)+1], rnd.nextInt(3)+1);
            wh.marketDraw(res1);
            wh.moveInShelf(res1, 3);
            PhysicalResource takenres = wh.take(3, res1.getQuantity()-rnd.nextInt(res1.getQuantity()));
            assertTrue(res1.getType().equals(takenres.getType())
                    && (wh.getWarehouseDisposition().get(2).getQuantity() + takenres.getQuantity()) == res1.getQuantity());
        }
    }

    /*
    Tests exceptions with impossible shelf switches, such as full third <-> not even full first/second.
    Tests also the affordability of the switch.
     */
    @Test
    public void BasicWarehouseSwitchShelfTest() throws InvalidQuantityException, InvalidOperationException {
        Warehouse wh = new ConcreteWarehouse();

        //"negative shelf values" test:
        assertThrows(ShelfInsertException.class, ()->wh.switchShelf(-1,1));
        assertThrows(ShelfInsertException.class, ()->wh.switchShelf(1,-1));

        //"not enough space to switch" test:
        PhysicalResource res1 = new PhysicalResource(ResType.COIN,1);
        PhysicalResource res2 = new PhysicalResource(ResType.SERVANT,3);
        wh.marketDraw(res1); wh.marketDraw(res2);
        wh.moveInShelf(res1, 1);
        wh.moveInShelf(res2, 3);
        assertThrows(ShelfInsertException.class, ()->wh.switchShelf(2,3));
        assertThrows(ShelfInsertException.class, ()->wh.switchShelf(3,2));

        //"switch affordability" test:
        res2 = new PhysicalResource(ResType.STONE, 1);
        wh.marketDraw(res2);
        wh.moveInShelf(res2, 2);
        wh.switchShelf(1,2);
        assertEquals(res1,wh.getWarehouseDisposition().get(1));
        assertEquals(res2,wh.getWarehouseDisposition().get(0));
    }

    @Test
    public void getNumberOfTest() throws InvalidQuantityException, ShelfInsertException
    {
        Warehouse wh = new ConcreteWarehouse();
        List<PhysicalResource> l = new ArrayList<>();
        l.add(new PhysicalResource(ResType.COIN,1));
        wh.marketDraw(l.get(0)); wh.moveInShelf(l.get(0),1);
        l.add(new PhysicalResource(ResType.STONE,2));
        wh.marketDraw(l.get(1)); wh.moveInShelf(l.get(1),2);
        l.add(new PhysicalResource(ResType.SERVANT,3));
        wh.marketDraw(l.get(2)); wh.moveInShelf(l.get(2),3);

        assertEquals(wh.getNumberOf(ResType.COIN),l.get(0).getQuantity());
        assertEquals(wh.getNumberOf(ResType.STONE),l.get(1).getQuantity());
        assertEquals(wh.getNumberOf(ResType.SERVANT),l.get(2).getQuantity());
        assertEquals(wh.getNumberOf(ResType.UNKNOWN),0);

    }
}
