package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Player;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

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

        Assertions.assertTrue(countWhite == 4 && countBlue == 2 && countGrey == 2 && countYellow == 2 && countPurple == 2 && countRed == 1);
    }

    @Test
    public void testSelectRow() throws NegativeQuantityException {
        Random gen = new Random();
        Adder player = new Player();
        int numRow = gen.nextInt(3);
        int countWhite = 0;

        for(int i = 0; i < 4; ++i) {
            if (this.board[numRow][i] instanceof WhiteMarble) {
                ++countWhite;
            }
        }

        Marble marble1 = this.board[numRow][0];
        Marble marble2 = this.board[numRow][1];
        Marble marble3 = this.board[numRow][2];
        Marble marble4 = this.board[numRow][3];
        Marble marble5 = this.market1.getSlide();
        int numWhite = this.market1.selectRow(numRow, player);
        Assertions.assertEquals(countWhite, numWhite);
        Assertions.assertEquals(marble1, this.market1.getSlide());
        Assertions.assertEquals(marble2, this.board[numRow][0]);
        Assertions.assertEquals(marble3, this.board[numRow][1]);
        Assertions.assertEquals(marble4, this.board[numRow][2]);
        Assertions.assertEquals(marble5, this.board[numRow][3]);
    }

    @Test
    public void testSelectColumn() throws NegativeQuantityException {
        Random gen = new Random();
        Adder player = new Player();
        int numColumn = gen.nextInt(3);
        int countWhite = 0;

        for(int i = 0; i < 3; ++i) {
            if (this.board[i][numColumn] instanceof WhiteMarble) {
                ++countWhite;
            }
        }

        Marble marble1 = this.board[0][numColumn];
        Marble marble2 = this.board[1][numColumn];
        Marble marble3 = this.board[2][numColumn];
        Marble marble4 = this.market1.getSlide();
        int numWhite = this.market1.selectColumn(numColumn, player);
        Assertions.assertEquals(countWhite, numWhite);
        Assertions.assertEquals(marble1, this.market1.getSlide());
        Assertions.assertEquals(marble2, this.board[0][numColumn]);
        Assertions.assertEquals(marble3, this.board[1][numColumn]);
        Assertions.assertEquals(marble4, this.board[2][numColumn]);
    }
}
