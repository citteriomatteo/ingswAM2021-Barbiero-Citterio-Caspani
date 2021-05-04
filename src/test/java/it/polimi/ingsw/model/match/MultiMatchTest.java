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
    public void testCreate() throws WrongSettingException, FileNotFoundException, SingleMatchException {

        assertThrows(SingleMatchException.class,()->new MultiMatch(List.of(new Player("player1")),"src/test/resources/StandardConfiguration.json"));
        try{match = new MultiMatch(List.of(new Player("player1"),new Player("player2"),new Player("player3"),new Player("player4")),
                "src/test/resources/StandardConfiguration.json");}catch(UnsupportedOperationException e){e.printStackTrace();}

        assertEquals("player1",match.getCurrentPlayer().toString());
        assertEquals(4,match.getCurrentPlayer().getHandLeaders().size());


    }

    @Test
    public void testNextTurn() throws WrongSettingException, FileNotFoundException, SingleMatchException {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"), new Player("player2"), new Player("player3")));

        match = new MultiMatch(players,"src/test/resources/StandardConfiguration.json");

        assertEquals(match.getCurrentPlayer(),players.get(0));

        match.nextTurn();
        assertEquals(match.getCurrentPlayer(),players.get(1));

        match.nextTurn();
        assertEquals(match.getCurrentPlayer(),players.get(2));

        match.nextTurn();
        assertEquals(match.getCurrentPlayer(),players.get(0));

    }

    @Test
    public void getNextPlayer() throws WrongSettingException, FileNotFoundException, SingleMatchException {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"), new Player("player2"), new Player("player3")));

        match = new MultiMatch(players,"src/test/resources/StandardConfiguration.json");

        assertEquals(match.getNextPlayer(),players.get(1));
        match.nextTurn();
        assertEquals(match.getNextPlayer(),players.get(2));
        match.nextTurn();
        assertEquals(match.getNextPlayer(),players.get(0));

    }

    @Test
    public void testGetPlayer() throws WrongSettingException, FileNotFoundException, SingleMatchException {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"), new Player("player2"), new Player("player3")));

        match = new MultiMatch(players,"src/test/resources/StandardConfiguration.json");

        assertEquals(players.get(0),match.getPlayer("player1"));
        assertEquals(players.get(1),match.getPlayer("player2"));
        assertEquals(players.get(2),match.getPlayer("player3"));
        assertNull(match.getPlayer("player10"));

    }

}