package it.polimi.ingsw.model.match;

import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.token.PlusOneShuffleToken;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

public class SingleMatchTest extends CommonThingsTest {
    private SingleMatch match;

    @Test
    public void testNextTurn() throws WrongSettingException, LastRoundException {
        int dim;
        Player p = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(p, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        match = new SingleMatch(p);

        dim = match.getTokenStack().getStack().size();

        if (match.getTokenStack().getStack().get(dim-1) instanceof PlusOneShuffleToken) {
            match.nextTurn();

            assertEquals(match.getTokenStack().getStack().size(), 7);
        } else {
            match.nextTurn();

            assertEquals(match.getTokenStack().getStack().size(), dim - 1);

        }
    }

    @Test
    public void testGetPlayer() throws WrongSettingException {
        Player player = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        match = new SingleMatch(player);

        assertEquals(player,match.getPlayer("player1"));
        assertNull(match.getPlayer("player2"));

    }
}
