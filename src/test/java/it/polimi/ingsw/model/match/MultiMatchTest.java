package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.exceptions.SingleMatchException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiMatchTest {
    private MultiMatch match;

    @Test
    public void testCreate() {

        assertThrows(SingleMatchException.class,()->new MultiMatch(List.of(new Player("player1")),"src/test/resources/StandardConfiguration.json"));

    }

    @Test
    public void testNextTurn() throws WrongSettingException, FileNotFoundException, SingleMatchException {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"), new Player("player2"), new Player("player3")));

        match = new MultiMatch(players,"src/test/resources/StandardConfiguration.json");

        assertEquals(match.getCurrentPlayer(),players.get(0));

        match.nextTurn();

        assertEquals(match.getCurrentPlayer(),players.get(1));

        match.nextTurn();
        match.nextTurn();

        assertEquals(match.getCurrentPlayer(),players.get(0));

    }

    @Test
    public void getNextPlayer() throws WrongSettingException, FileNotFoundException, SingleMatchException {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"), new Player("player2"), new Player("player3")));

        match = new MultiMatch(players,"src/test/resources/StandardConfiguration.json");

        assertEquals(match.getNextPlayer(),players.get(1));
    }

}