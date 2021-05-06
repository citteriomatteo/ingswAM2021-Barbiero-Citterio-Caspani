package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MultiMatch;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest
{


    public void paySequence(Player p1, Player p2) throws NegativeQuantityException, InvalidOperationException {
        p1.payFromWarehouse(new PhysicalResource(ResType.SHIELD,1), 3);
        p2.payFromStrongbox(new PhysicalResource(ResType.COIN,3));
    }


    public void addSequence(Player p1, Player p2) throws NegativeQuantityException, InvalidOperationException {
        p1.addToWarehouse(new PhysicalResource(ResType.SERVANT,3));
        p1.addToWarehouse(new PhysicalResource(ResType.COIN, 2));
        p1.addToWarehouse(new PhysicalResource(ResType.SHIELD,4));

        p1.moveIntoWarehouse(new PhysicalResource(ResType.SERVANT, 1), 1);
        p1.moveIntoWarehouse(new PhysicalResource(ResType.COIN, 2), 2);
        p1.moveIntoWarehouse(new PhysicalResource(ResType.SHIELD, 2), 3);
        p1.addToStrongBox(new PhysicalResource(ResType.COIN, 5));
        p1.addToStrongBox(new PhysicalResource(ResType.STONE, 7));

        p2.addToWarehouse(new PhysicalResource(ResType.SERVANT,1));
        p2.addToWarehouse(new PhysicalResource(ResType.COIN, 1));
        p2.addToWarehouse(new PhysicalResource(ResType.SHIELD,1));

        p2.moveIntoWarehouse(new PhysicalResource(ResType.SERVANT, 1), 1);
        p2.moveIntoWarehouse(new PhysicalResource(ResType.COIN, 1), 2);
        p2.moveIntoWarehouse(new PhysicalResource(ResType.SHIELD, 1), 3);
        p2.addToStrongBox(new PhysicalResource(ResType.COIN, 10));
        p2.addToStrongBox(new PhysicalResource(ResType.STONE, 10));
    }

    @Test
    public void totalWinPointsTest() throws NegativeQuantityException,
            SingleMatchException, WrongSettingException, InvalidOperationException, LastRoundException {
        Player player = new Player("player1");
        Player player1 = new Player("player2");
        Match match = new MultiMatch(Arrays.asList(player, player1));

        addSequence(player, player1);
        paySequence(player, player1);

        player.addFaithPoints(18);

        /*tests that player has exactly floor(16/5)=3 points for the resources, 5 points for the pope tiles and 34 for the faith path.
        player1 has only floor(20/5)=4 points for the resources instead.*/
        assertEquals(42,player.totalWinPoints());
        assertEquals(4,player1.totalWinPoints());
    }

    public void boh() throws WrongSettingException, SingleMatchException {
        Player player = new Player("player1");
        Player player1 = new Player("player2");
        Match match = new MultiMatch(Arrays.asList(player, player1));

    }
}
