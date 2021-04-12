package it.polimi.ingsw.model.essentials;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
Test for Production class
 */
public class ProductionTest {

    private Production production;
    private Player player;

    //test the constructor with random cost and earnings
    @Test
    @BeforeEach
    public void ConstructorTest() throws NegativeQuantityException, InvalidAddFaithException {
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
        production = new Production(cost, earnings);
        assertTrue(production.getCost().size() <= ResType.values().length, "Wrong dimension of cost List in constructor");
    }

    @Test
    public void produceTest() throws NegativeQuantityException, InvalidAddFaithException, MatchEndedException {
        List<PhysicalResource> cost = new ArrayList<>();
        List<Resource> earnings = new ArrayList<>();
        earnings.add(new PhysicalResource(ResType.SHIELD, 1));
        Production prod = new Production(cost, earnings);

        assertTrue(prod.produce(new Player("player1")), "produce returned false");


        //TODO finish produceTest after implementing add methods
    }

    @Test
    public void isPlayableTest() throws NegativeQuantityException {
        List<PhysicalResource> cost = new ArrayList<>();
        List<Resource> earnings = new ArrayList<>();
        cost.add(new PhysicalResource(ResType.SHIELD, 1));
        Production prod = new Production(cost, earnings);
        player = new Player("player1");

        assertFalse(prod.isPlayable(player),"isPlayable returned true");

        player.addToStrongBox(new PhysicalResource(ResType.SHIELD,1));

        assertTrue(prod.isPlayable(player), "isPlayable returned false");



    }
}

