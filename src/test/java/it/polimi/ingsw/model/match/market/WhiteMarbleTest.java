package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WhiteMarbleTest {
    private Adder player = new Player("player1");

    public WhiteMarbleTest() throws NegativeQuantityException {
    }

    @Test
    public void testOnDraw() throws NegativeQuantityException, MatchEndedException {
        Marble whiteMarble = new WhiteMarble();
        Assertions.assertTrue(whiteMarble.onDraw(this.player));
    }
}
