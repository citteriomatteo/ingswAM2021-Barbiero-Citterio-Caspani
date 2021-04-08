package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PurpleMarbleTest {
    private Adder player = new Player();

    public PurpleMarbleTest() {
    }

    @Test
    public void testOnDraw() throws NegativeQuantityException {
        Marble purpleMarble = new PurpleMarble();
        Assertions.assertFalse(purpleMarble.onDraw(this.player));
    }
}
