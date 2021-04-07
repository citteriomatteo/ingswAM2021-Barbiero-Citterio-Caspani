package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import java.util.Random;

import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.Verificator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhysicalResourceTest {
    private Player player = new Player();
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
    public void testVerify() throws NegativeQuantityException {
        PhysicalResource resource1 = new PhysicalResource(ResType.COIN,1);

        assertTrue(resource1.verify(player));
    }


}
