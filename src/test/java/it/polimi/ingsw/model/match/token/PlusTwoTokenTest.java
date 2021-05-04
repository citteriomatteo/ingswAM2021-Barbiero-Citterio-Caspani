package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.exceptions.MatchEndedException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.SingleFaithPath;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlusTwoTokenTest {
    private List<Player> players = new ArrayList<>();
    private SingleMatch singleMatch;
    private PlusTwoToken plusTwoToken = new PlusTwoToken();
    @Test
    public void testOnDraw() throws FileNotFoundException, WrongSettingException, MatchEndedException {
        singleMatch = new SingleMatch(new Player("player1"),"src/test/resources/StandardConfiguration.json");

        assertTrue(plusTwoToken.onDraw(singleMatch));

        SingleFaithPath singleFaithPath = (SingleFaithPath) singleMatch.getCurrentPlayer().getPersonalBoard().getFaithPath();

        assertEquals(singleFaithPath.getBlackPosition(),2);

    }
}
