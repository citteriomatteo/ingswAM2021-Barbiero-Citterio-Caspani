package it.polimi.ingsw.gameLogic.model.match.market;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.ResType;
import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.CommonThingsTest;
import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.SingleMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class YellowMarbleTest extends CommonThingsTest {
    private Player player = new Player("player1");
    private Match match;

    @Test
    public void testOnDraw() throws NegativeQuantityException, LastRoundException, FileNotFoundException, WrongSettingException {
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(),matchConfiguration.getBasicProduction());
        match = new SingleMatch(player);

        Marble yellowMarble = new YellowMarble();
        Assertions.assertFalse(yellowMarble.onDraw(this.player));

        Assertions.assertEquals(new PhysicalResource(ResType.COIN,1),match.getCurrentPlayer().getPersonalBoard().getWarehouse().getBuffer().get(0));

    }
}
