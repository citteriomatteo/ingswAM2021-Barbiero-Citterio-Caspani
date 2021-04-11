package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PlusOneShuffleTokenTest {
    private List<Player> players = new ArrayList<>();
    private SingleMatch singleMatch;
    private PlusOneShuffleToken plusOneShuffleToken = new PlusOneShuffleToken();
    @Test
    public void testOnDraw() throws NegativeQuantityException, FileNotFoundException, WrongSettingException, MatchEndedException {
        players.add(new Player("player1"));
        singleMatch = new SingleMatch(players);

        assertTrue(plusOneShuffleToken.onDraw(singleMatch));
    }
}
