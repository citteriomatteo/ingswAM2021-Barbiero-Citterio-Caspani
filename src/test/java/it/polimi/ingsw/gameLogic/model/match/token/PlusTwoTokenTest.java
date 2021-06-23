package it.polimi.ingsw.gameLogic.model.match.token;

import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.CommonThingsTest;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath.SingleFaithPath;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.gameLogic.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlusTwoTokenTest extends CommonThingsTest {
    private List<Player> players = new ArrayList<>();
    private SingleMatch singleMatch;
    private PlusTwoToken plusTwoToken = new PlusTwoToken();
    @Test
    public void testOnDraw() throws FileNotFoundException, WrongSettingException, LastRoundException {
        Player p = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/StandardConfiguration.json");
        setSummary(p, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(),matchConfiguration.getBasicProduction());
        singleMatch = new SingleMatch(p);

        assertTrue(plusTwoToken.onDraw(singleMatch));

        SingleFaithPath singleFaithPath = (SingleFaithPath) singleMatch.getCurrentPlayer().getPersonalBoard().getFaithPath();

        assertEquals(singleFaithPath.getBlackPosition(),2);

    }

    @Test
    public void testToString(){
        assertEquals(plusTwoToken.getClass().getSimpleName(), plusTwoToken.toString());
    }
}
