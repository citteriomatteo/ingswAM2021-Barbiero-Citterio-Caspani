package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

public class RedMarbleTest {
    private Player player = new Player("player1");
    private Match match;

    public RedMarbleTest() throws NegativeQuantityException {
    }

    @Test
    public void testOnDraw() throws NegativeQuantityException, MatchEndedException, FileNotFoundException, WrongSettingException {
        match = new SingleMatch(player,"src/test/resources/StandardConfiguration.json");
        Marble redMarble = new RedMarble();
        Assertions.assertFalse(redMarble.onDraw(this.player));
    }
}
