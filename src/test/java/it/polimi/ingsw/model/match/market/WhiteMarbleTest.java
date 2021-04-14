package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class WhiteMarbleTest {
    private Player player = new Player("player1");
    private Match match;

    @Test
    public void testOnDraw() throws NegativeQuantityException, MatchEndedException, FileNotFoundException, WrongSettingException {
        match = new SingleMatch(player,"src/test/resources/StandardConfiguration.json");
        Marble whiteMarble = new WhiteMarble();
        Assertions.assertTrue(whiteMarble.onDraw(this.player));
    }
}
