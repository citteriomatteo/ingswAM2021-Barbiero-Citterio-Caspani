package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.CommonThingsTest;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MatchConfiguration;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

/*
Test for Production class
 */
public class ProductionTest extends CommonThingsTest {

    //test the constructor with random cost and earnings
    @Test
    public void ConstructorTest() throws NegativeQuantityException {
        List<PhysicalResource> cost = new ArrayList<>();
        List<Resource> earnings = new ArrayList<>();
        Random rand = new Random();
        int i;
        boolean faith;

        for (i = 0; i<10; i++){
            cost.add(new PhysicalResource(ResType.values()[rand.nextInt(4)+1], rand.nextInt(10)));
        }

        for (i = 0; i<5; i++){
            faith=rand.nextBoolean();
            if (faith)
                earnings.add(new FaithPoint(rand.nextInt(4)+1));
            else
                earnings.add(new PhysicalResource(ResType.values()[rand.nextInt(4)+1], rand.nextInt(10)));
        }
        Production production = new Production(cost, earnings);
        assertTrue(production.getCost().size() <= ResType.values().length, "Wrong dimension of cost List in constructor");
    }

    @Test
    public void produceTest() throws NegativeQuantityException, LastRoundException, WrongSettingException {
        List<PhysicalResource> cost = new ArrayList<>();
        List<Resource> earnings = new ArrayList<>();
        earnings.add(new PhysicalResource(ResType.SHIELD, 1));
        Production prod = new Production(cost, earnings);
        Player player = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new SingleMatch(player);

        assertEquals(0, match.getCurrentPlayer().getPersonalBoard().getStrongBox().getNumberOf(ResType.SHIELD));
        assertTrue(prod.produce(player), "produce returned false");
        assertEquals(1, match.getCurrentPlayer().getPersonalBoard().getStrongBox().getNumberOf(ResType.SHIELD));
    }

    @Test
    public void isPlayableTest() throws NegativeQuantityException, WrongSettingException {
        List<PhysicalResource> cost = new ArrayList<>();
        List<Resource> earnings = new ArrayList<>();
        cost.add(new PhysicalResource(ResType.SHIELD, 1));
        Production prod = new Production(cost, earnings);
        Player player = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        Match match = new SingleMatch(player);


        assertFalse(prod.isPlayable(player),"isPlayable returned true");

        player.addToStrongBox(new PhysicalResource(ResType.SHIELD,1));

        assertTrue(prod.isPlayable(player), "isPlayable returned false");

    }
}

