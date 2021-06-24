package it.polimi.ingsw.gameLogic.model.essentials;

import it.polimi.ingsw.gameLogic.exceptions.InvalidQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.ShelfInsertException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.CommonThingsTest;
import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PhysicalResourceTest extends CommonThingsTest {
    private final Player player = new Player("player1");

    @Test
    public void testCreate() {
        Random gen = new Random();
        for (int i=0; i<50; i++){
            int randomType = gen.nextInt(5);
            int randomQuantity = gen.nextInt(10)-20;

            assertThrows(NegativeQuantityException.class, ()-> new PhysicalResource(ResType.values()[randomType], randomQuantity));
        }

    }

    @Test
    public void testIsResource(){
        Requirable resource = new PhysicalResource(ResType.STONE, 3);
        assertTrue(resource.isAResource());
    }

    @Test
    public void testEquals() throws NegativeQuantityException {
        PhysicalResource resource1 = new PhysicalResource(ResType.STONE, 3);
        PhysicalResource resource2 = new PhysicalResource(ResType.STONE, 2);
        PhysicalResource resource3 = new PhysicalResource(ResType.COIN,5);

        assertEquals(resource1, resource2);
        assertNotEquals(resource1, resource3);
    }

    @Test
    public void testAdd() throws NegativeQuantityException, WrongSettingException {
        PhysicalResource resource1 = new PhysicalResource(ResType.SERVANT,3);
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(),matchConfiguration.getBasicProduction());
        Match match = new SingleMatch(player);
        assertTrue(resource1.add(player));

        assertEquals(3,player.getPersonalBoard().getStrongBox().getNumberOf(ResType.SERVANT));
        assertEquals(0,player.getPersonalBoard().getStrongBox().getNumberOf(ResType.STONE));
        assertEquals(0,player.getPersonalBoard().getStrongBox().getNumberOf(ResType.SHIELD));
        assertEquals(0,player.getPersonalBoard().getStrongBox().getNumberOf(ResType.COIN));
    }

    @Test
    public void testVerify() throws WrongSettingException, InvalidQuantityException, ShelfInsertException {
        Player player = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new SingleMatch(player);
        PhysicalResource resource1 = new PhysicalResource(ResType.COIN,1);
        PhysicalResource resource2 = new PhysicalResource(ResType.STONE,2);
        PhysicalResource resource3 = new PhysicalResource(ResType.SERVANT,1);
        PhysicalResource resource4 = new PhysicalResource(ResType.STONE,4);
        PhysicalResource resource5 = new PhysicalResource(ResType.STONE,3);

        assertFalse(resource1.verify(player));

        player.addToStrongBox(resource1);

        assertTrue(resource1.verify(player));

        player.addToWarehouse(resource2);
        player.getPersonalBoard().getWarehouse().moveInShelf(resource2,2);

        assertTrue(resource2.verify(player));
        assertFalse(resource3.verify(player));
        assertFalse(resource4.verify(player));

        player.addToStrongBox(resource5);

        assertTrue(resource4.verify(player));
    }


}
