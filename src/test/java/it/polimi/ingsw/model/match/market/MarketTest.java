package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.exceptions.LastRoundException;
import it.polimi.ingsw.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.CommonThingsTest;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MatchConfiguration;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;

import java.io.FileNotFoundException;
import java.util.Random;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

public class MarketTest extends CommonThingsTest {
    private Market market1 = new Market();
    private Marble[][] board;
    private Match match;

    public MarketTest() {
        this.board = this.market1.getBoard();
    }

    @Test
    @Order(1)
    public void testCreate() {
        int countWhite = 0;
        int countBlue = 0;
        int countGrey = 0;
        int countYellow = 0;
        int countPurple = 0;
        int countRed = 0;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 4; ++j) {
                if (this.board[i][j] instanceof WhiteMarble) {
                    ++countWhite;
                } else if (this.board[i][j] instanceof BlueMarble) {
                    ++countBlue;
                } else if (this.board[i][j] instanceof GreyMarble) {
                    ++countGrey;
                } else if (this.board[i][j] instanceof YellowMarble) {
                    ++countYellow;
                } else if (this.board[i][j] instanceof PurpleMarble) {
                    ++countPurple;
                } else if (this.board[i][j] instanceof RedMarble) {
                    ++countRed;
                }
            }
        }

        if (this.market1.getSlide() instanceof WhiteMarble) {
            ++countWhite;
        } else if (this.market1.getSlide() instanceof BlueMarble) {
            ++countBlue;
        } else if (this.market1.getSlide() instanceof GreyMarble) {
            ++countGrey;
        } else if (this.market1.getSlide() instanceof YellowMarble) {
            ++countYellow;
        } else if (this.market1.getSlide() instanceof PurpleMarble) {
            ++countPurple;
        } else if (this.market1.getSlide() instanceof RedMarble) {
            ++countRed;
        }

        assertTrue(countWhite == 4 && countBlue == 2 && countGrey == 2 && countYellow == 2 && countPurple == 2 && countRed == 1);
    }

    @Test
    public void testSelectRow() throws LastRoundException, WrongSettingException, InvalidOperationException {
        Random gen = new Random();
        Player player = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(),matchConfiguration.getBasicProduction());
        match = new SingleMatch(player);
        int numRow = gen.nextInt(3);
        int countWhite = 0;

        for(int i = 0; i < 4; ++i) {
            if (this.board[numRow][i] instanceof WhiteMarble) {
                ++countWhite;
            }
        }

        assertThrows(InvalidOperationException.class,()->market1.selectRow(-1,player));
        assertThrows(InvalidOperationException.class,()->market1.selectRow(6,player));

        Marble marble1 = this.board[numRow][0];
        Marble marble2 = this.board[numRow][1];
        Marble marble3 = this.board[numRow][2];
        Marble marble4 = this.board[numRow][3];
        Marble marble5 = this.market1.getSlide();
        int numWhite = this.market1.selectRow(numRow+1, player);
        assertEquals(countWhite, numWhite);
        assertEquals(marble1, this.market1.getSlide());
        assertEquals(marble2, this.board[numRow][0]);
        assertEquals(marble3, this.board[numRow][1]);
        assertEquals(marble4, this.board[numRow][2]);
        assertEquals(marble5, this.board[numRow][3]);
    }

    @Test
    public void testSelectColumn() throws LastRoundException, WrongSettingException, InvalidOperationException {
        Random gen = new Random();
        Player player = new Player("player1");
        MatchConfiguration matchConfiguration = assignConfiguration("src/test/resources/PartialFreeConfiguration.json");
        setSummary(player, getCardMap(matchConfiguration), matchConfiguration.getCustomPath(),matchConfiguration.getBasicProduction());
        match = new SingleMatch(player);
        int numColumn = gen.nextInt(3);
        int countWhite = 0;

        for(int i = 0; i < 3; ++i) {
            if (this.board[i][numColumn] instanceof WhiteMarble) {
                ++countWhite;
            }
        }

        assertThrows(InvalidOperationException.class,()->market1.selectColumn(-1,player));
        assertThrows(InvalidOperationException.class,()->market1.selectColumn(6,player));


        Marble marble1 = this.board[0][numColumn];
        Marble marble2 = this.board[1][numColumn];
        Marble marble3 = this.board[2][numColumn];
        Marble marble4 = this.market1.getSlide();
        int numWhite = this.market1.selectColumn(numColumn+1, player);
        assertEquals(countWhite, numWhite);
        assertEquals(marble1, this.market1.getSlide());
        assertEquals(marble2, this.board[0][numColumn]);
        assertEquals(marble3, this.board[1][numColumn]);
        assertEquals(marble4, this.board[2][numColumn]);
    }
}
