package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GreyMarbleTest {
    private Adder player = new Player();

    public GreyMarbleTest() {
    }

    @Test
    public void testOnDraw() throws NegativeQuantityException {
        Marble greyMarble = new GreyMarble();
        Assertions.assertFalse(greyMarble.onDraw(this.player));
    }
}
