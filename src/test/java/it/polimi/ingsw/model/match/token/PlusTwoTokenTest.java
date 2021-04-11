package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlusTwoTokenTest {
    private List<Player> players = new ArrayList<>();
    private SingleMatch singleMatch;
    private PlusTwoToken plusTwoToken = new PlusTwoToken();
    @Test
    public void testOnDraw() throws NegativeQuantityException, FileNotFoundException, WrongSettingException, MatchEndedException {
        players.add(new Player("player1"));
        singleMatch = new SingleMatch(players);

        assertTrue(plusTwoToken.onDraw(singleMatch));
    }
}
