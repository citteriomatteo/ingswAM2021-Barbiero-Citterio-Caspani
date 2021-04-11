package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Player;
import java.util.Random;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTest {
    private Market market1 = new Market();
    private Marble[][] board;

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
    public void testSelectRow() throws NegativeQuantityException, InvalidQuantityException, MatchEndedException {
        Random gen = new Random();
        Adder player = new Player("player1",null);
        int numRow = gen.nextInt(3);
        int countWhite = 0;

        for(int i = 0; i < 4; ++i) {
            if (this.board[numRow][i] instanceof WhiteMarble) {
                ++countWhite;
            }
        }

        assertThrows(InvalidQuantityException.class,()->market1.selectRow(-1,player));
        assertThrows(InvalidQuantityException.class,()->market1.selectRow(6,player));

        Marble marble1 = this.board[numRow][0];
        Marble marble2 = this.board[numRow][1];
        Marble marble3 = this.board[numRow][2];
        Marble marble4 = this.board[numRow][3];
        Marble marble5 = this.market1.getSlide();
        int numWhite = this.market1.selectRow(numRow, player);
        assertEquals(countWhite, numWhite);
        assertEquals(marble1, this.market1.getSlide());
        assertEquals(marble2, this.board[numRow][0]);
        assertEquals(marble3, this.board[numRow][1]);
        assertEquals(marble4, this.board[numRow][2]);
        assertEquals(marble5, this.board[numRow][3]);
    }

    @Test
    public void testSelectColumn() throws NegativeQuantityException, InvalidQuantityException, MatchEndedException {
        Random gen = new Random();
        Adder player = new Player("player1",null);
        int numColumn = gen.nextInt(3);
        int countWhite = 0;

        for(int i = 0; i < 3; ++i) {
            if (this.board[i][numColumn] instanceof WhiteMarble) {
                ++countWhite;
            }
        }

        assertThrows(InvalidQuantityException.class,()->market1.selectColumn(-1,player));
        assertThrows(InvalidQuantityException.class,()->market1.selectColumn(6,player));


        Marble marble1 = this.board[0][numColumn];
        Marble marble2 = this.board[1][numColumn];
        Marble marble3 = this.board[2][numColumn];
        Marble marble4 = this.market1.getSlide();
        int numWhite = this.market1.selectColumn(numColumn, player);
        assertEquals(countWhite, numWhite);
        assertEquals(marble1, this.market1.getSlide());
        assertEquals(marble2, this.board[0][numColumn]);
        assertEquals(marble3, this.board[1][numColumn]);
        assertEquals(marble4, this.board[2][numColumn]);
    }
}
