package it.polimi.ingsw.model.match;

import it.polimi.ingsw.exceptions.SingleMatchException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiMatchTest {
    private MultiMatch match;

    @Test
    public void testCreate() throws WrongSettingException, SingleMatchException {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");

        assertThrows(SingleMatchException.class,()->new MultiMatch(List.of(new Player("player1"))));
        try{
            match = new MultiMatch(new ArrayList<>(List.of(player1,player2,player3,player4)));
        }catch(UnsupportedOperationException e){e.printStackTrace();}

        assertTrue(match.getPlayers().contains(player1));
        assertTrue(match.getPlayers().contains(player2));
        assertTrue(match.getPlayers().contains(player3));
        assertTrue(match.getPlayers().contains(player4));

        assertEquals(4,match.getCurrentPlayer().getHandLeaders().size());


    }

    @Test
    public void testNextTurn() throws WrongSettingException, SingleMatchException {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"), new Player("player2"), new Player("player3")));

        match = new MultiMatch(players);

        assertEquals(match.getCurrentPlayer(),players.get(0));

        match.nextTurn();
        assertEquals(match.getCurrentPlayer(),players.get(1));

        match.nextTurn();
        assertEquals(match.getCurrentPlayer(),players.get(2));

        match.nextTurn();
        assertEquals(match.getCurrentPlayer(),players.get(0));

    }

    @Test
    public void getNextPlayer() throws WrongSettingException, SingleMatchException {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"), new Player("player2"), new Player("player3")));

        match = new MultiMatch(players);

        assertEquals(match.getNextPlayer(),players.get(1));
        match.nextTurn();
        assertEquals(match.getNextPlayer(),players.get(2));
        match.nextTurn();
        assertEquals(match.getNextPlayer(),players.get(0));

    }


}