package it.polimi.ingsw.model.match.token;

import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.LeaderStack;
import it.polimi.ingsw.model.match.SingleCardGrid;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BlueTokenTest {
    private List<Player> players = new ArrayList<>();
    private SingleMatch singleMatch;
    private BlueToken blueToken = new BlueToken();

    @Test
    public void testOnDraw() throws NegativeQuantityException, MatchEndedException, FileNotFoundException, WrongSettingException {
        singleMatch = new SingleMatch(new Player("player1"),"src/test/resources/StandardConfiguration.json");
        assertTrue(blueToken.onDraw(singleMatch));

    }
}
