package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.SingleMatchException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlueMarbleTest {
    private Player player;
    private Match match;

    @Test
    public void testOnDraw() throws NegativeQuantityException, LastRoundException, FileNotFoundException, WrongSettingException, SingleMatchException {
        match = new MultiMatch(new ArrayList<>(Arrays.asList(new Player("player1"),new Player("player2"))));
        Marble blueMarble = new BlueMarble();
        player = match.getCurrentPlayer();
        Assertions.assertFalse(blueMarble.onDraw(this.player));

        Assertions.assertEquals(new PhysicalResource(ResType.SHIELD,1),match.getCurrentPlayer().getPersonalBoard().getWarehouse().getBuffer().get(0));
    }
}
