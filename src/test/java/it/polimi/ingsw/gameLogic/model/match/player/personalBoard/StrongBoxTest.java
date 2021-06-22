package it.polimi.ingsw.gameLogic.model.match.player.personalBoard;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.ResType;
import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.NotEnoughResourcesException;
import org.junit.jupiter.api.Test;

public class StrongBoxTest
{
    @Test
    public void initializationTest()
    {
        StrongBox sb = new StrongBox();
        for(ResType key: sb.getResources().keySet())
            assertEquals(sb.getResources().get(key), 0);
    }

    @Test
    public void putTest() throws NegativeQuantityException
    {
        StrongBox sb = new StrongBox();
        Random gen = new Random();
        for(int i=0; i<50; i++)
        {
            int rndtype=gen.nextInt(4);
            if(rndtype == 0)
                rndtype = 1;
            int rndquantity= gen.nextInt(50);
            int quantitybefore= sb.getResources().get(ResType.values()[rndtype]);
            PhysicalResource res = new PhysicalResource(ResType.values()[rndtype], rndquantity);
            sb.put(res);
            assertEquals(sb.getResources().get(ResType.values()[rndtype]), (quantitybefore+rndquantity));
        }
    }

    @Test
    public void takeTest() throws NotEnoughResourcesException, NegativeQuantityException {
        StrongBox sb = new StrongBox();
        Random gen = new Random();
        int rndtype=gen.nextInt(4);
        if(rndtype == 0)
            rndtype = 1;

        final PhysicalResource res = new PhysicalResource(ResType.values()[rndtype], 1);
        assertThrows(NotEnoughResourcesException.class, ()->sb.take(res));

        sb.put(res);

        PhysicalResource res2 = new PhysicalResource(ResType.values()[rndtype], 1);
        sb.take(res2);
        assertEquals(sb.getResources().get(ResType.values()[rndtype]), 0);
    }

}
