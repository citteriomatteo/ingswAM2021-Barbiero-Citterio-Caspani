package it.polimi.ingsw.gameLogic.model.match.token;

import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
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
import java.util.Stack;

import static it.polimi.ingsw.gameLogic.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

public class PlusOneShuffleTokenTest extends CommonThingsTest {
    private List<Player> players = new ArrayList<>();
    private SingleMatch singleMatch;
    private PlusOneShuffleToken plusOneShuffleToken = new PlusOneShuffleToken();
    @Test
    public void testOnDraw() throws NegativeQuantityException, FileNotFoundException, WrongSettingException, LastRoundException {
        Player p = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(p, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        singleMatch = new SingleMatch(p);
        Stack<Token> tokenStack = singleMatch.getTokenStack().getStack();

        assertTrue(plusOneShuffleToken.onDraw(singleMatch));
        SingleFaithPath singleFaithPath = (SingleFaithPath) singleMatch.getCurrentPlayer().getPersonalBoard().getFaithPath();

        assertEquals(singleFaithPath.getBlackPosition(),1);
        assertNotEquals(tokenStack,singleMatch.getTokenStack().getStack());
        assertEquals(tokenStack.size(),singleMatch.getTokenStack().getStack().size());
    }
}
