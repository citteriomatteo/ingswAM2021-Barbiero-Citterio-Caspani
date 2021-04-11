package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.Verificator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhysicalResourceTest {
    private Player player = new Player("player1",null);

    public PhysicalResourceTest() throws NegativeQuantityException {
    }

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
    public void testEquals() throws NegativeQuantityException {
        PhysicalResource resource1 = new PhysicalResource(ResType.STONE, 3);
        PhysicalResource resource2 = new PhysicalResource(ResType.STONE, 2);
        PhysicalResource resource3 = new PhysicalResource(ResType.COIN,5);

        assertTrue(resource1.equals(resource2));
        assertFalse(resource1.equals(resource3));
    }

    @Test
    public void testAdd() throws NegativeQuantityException, InvalidAddFaithException {
        PhysicalResource resource1 = new PhysicalResource(ResType.SERVANT,3);

        assertTrue(resource1.add(player));
    }

    @Test
    public void testVerify() throws NegativeQuantityException, FileNotFoundException, WrongSettingException, InvalidOperationException, ShelfInsertException {
        List<Player> players = new ArrayList<>();
        Player player = new Player("player1",null);
        players.add(player);
        Match match = new Match(players);
        player.setMatch(match);
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
