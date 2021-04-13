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

public class GreenTokenTest {
    private List<Player> players = new ArrayList<>();
    private SingleMatch singleMatch;
    private GreenToken greenToken = new GreenToken();

    @Test
    public void testOnDraw() throws NegativeQuantityException, FileNotFoundException, WrongSettingException, MatchEndedException {
        singleMatch = new SingleMatch(new Player("player1"),"src/test/resources/StandardConfiguration.json");
        assertTrue(greenToken.onDraw(singleMatch));

    }
}
