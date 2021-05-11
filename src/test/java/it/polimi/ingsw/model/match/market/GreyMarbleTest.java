package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.CommonThingsTest;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class GreyMarbleTest extends CommonThingsTest {
    private Player player = new Player("player1");
    private Match match;


    @Test
    public void testOnDraw() throws NegativeQuantityException, LastRoundException, FileNotFoundException, WrongSettingException {
        setSummary(player);
        match = new SingleMatch(player);
        Marble greyMarble = new GreyMarble();
        Assertions.assertFalse(greyMarble.onDraw(this.player));

        Assertions.assertEquals(new PhysicalResource(ResType.STONE,1),match.getCurrentPlayer().getPersonalBoard().getWarehouse().getBuffer().get(0));

    }
}
