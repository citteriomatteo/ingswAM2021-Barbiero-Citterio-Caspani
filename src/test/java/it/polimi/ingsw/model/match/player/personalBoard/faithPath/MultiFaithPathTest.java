package it.polimi.ingsw.model.match.player.personalBoard.faithPath;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.SingleMatchException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiFaithPathTest extends FaithPathTest
{

    @Test
    public void externalVaticanReportTest() throws SingleMatchException,
            WrongSettingException, LastRoundException {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"),
                new Player("player2"), new Player("player3")));
        Match match = new MultiMatch(players);

        match.getCurrentPlayer().addFaithPoints(9);
        assertEquals(match.getPlayers().get(0).getPersonalBoard().getFaithPath().getPopeTiles().get(0),1);
        assertEquals(match.getPlayers().get(1).getPersonalBoard().getFaithPath().getPopeTiles().get(0),2);
        assertEquals(match.getPlayers().get(2).getPersonalBoard().getFaithPath().getPopeTiles().get(0),2);
        match.nextTurn();
        match.getCurrentPlayer().addFaithPoints(17);

        List<Integer> tiles = match.getPlayers().get(0).getPersonalBoard().getFaithPath().getPopeTiles();
        assertEquals(tiles.get(0),1); assertEquals(tiles.get(1),2); assertEquals(tiles.get(2),0);

        tiles = match.getPlayers().get(1).getPersonalBoard().getFaithPath().getPopeTiles();
        assertEquals(tiles.get(0),2); assertEquals(tiles.get(1),1); assertEquals(tiles.get(2),0);

        tiles = match.getPlayers().get(2).getPersonalBoard().getFaithPath().getPopeTiles();
        assertEquals(tiles.get(0),2); assertEquals(tiles.get(1),2); assertEquals(tiles.get(2),0);

        //"last vatican report before ending" test;
        match.nextTurn();
        assertThrows(LastRoundException.class, ()->match.getCurrentPlayer().addFaithPoints(24));

        tiles = match.getPlayers().get(0).getPersonalBoard().getFaithPath().getPopeTiles();
        assertEquals(1,tiles.get(0)); assertEquals(2,tiles.get(1)); assertEquals(2,tiles.get(2));

        tiles = match.getPlayers().get(1).getPersonalBoard().getFaithPath().getPopeTiles();
        assertEquals(2,tiles.get(0)); assertEquals(1,tiles.get(1)); assertEquals(2,tiles.get(2));

        tiles = match.getPlayers().get(2).getPersonalBoard().getFaithPath().getPopeTiles();
        assertEquals(2,tiles.get(0)); assertEquals(2,tiles.get(1)); assertEquals(1,tiles.get(2));



    }

}
