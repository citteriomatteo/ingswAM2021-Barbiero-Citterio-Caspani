package it.polimi.ingsw.gameLogic.model.match.market;

import it.polimi.ingsw.gameLogic.model.essentials.PhysicalResource;
import it.polimi.ingsw.gameLogic.model.essentials.ResType;
import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
import it.polimi.ingsw.gameLogic.exceptions.SingleMatchException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;
import it.polimi.ingsw.gameLogic.model.match.CommonThingsTest;
import it.polimi.ingsw.gameLogic.model.match.Match;
import it.polimi.ingsw.gameLogic.model.match.MatchConfiguration;
import it.polimi.ingsw.gameLogic.model.match.MultiMatch;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.*;

public class BlueMarbleTest extends CommonThingsTest {
    private Player player;
    private Match match;

    @Test
    public void testOnDraw() throws NegativeQuantityException, LastRoundException, FileNotFoundException, WrongSettingException, SingleMatchException {
        List<Player> players = new ArrayList<>(List.of(new Player("player1"),new Player("player2")));
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummaries(players, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(), matchConfiguration.getBasicProduction());
        match = new MultiMatch(players);

        Marble blueMarble = new BlueMarble();
        player = match.getCurrentPlayer();
        Assertions.assertFalse(blueMarble.onDraw(this.player));

        Assertions.assertEquals(new PhysicalResource(ResType.SHIELD,1),match.getCurrentPlayer().getPersonalBoard().getWarehouse().getBuffer().get(0));
    }
}
