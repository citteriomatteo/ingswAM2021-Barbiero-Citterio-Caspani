package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.match.player.Adder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Market {
    private Marble[][] board = new Marble[3][4];
    private Marble slide;

    /**
     * Customized constructor
     * @param board the market board
     * @param slide the single marble
     */

    public Market(Marble[][] board, Marble slide) {
        this.board = board;
        this.slide = slide;
    }

    /**
     * Default constructor.
     * Puts 4 white marbles, 2 blue marbles, 2 grey marbles, 2 yellow marbles, 2 purple marbles and 1 red marble
     * in the market's board by a random order
     */
    public Market() {
        List<Marble> marblesList = new ArrayList<>();

        for(int i = 0; i < 4; i++) {
            marblesList.add(new WhiteMarble());
        }

        for(int i = 0; i < 2; i++) {
            marblesList.add(new BlueMarble());
            marblesList.add(new GreyMarble());
            marblesList.add(new YellowMarble());
            marblesList.add(new PurpleMarble());
        }

        marblesList.add(new RedMarble());
        Collections.shuffle(marblesList);
        int countList = 0;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 4; ++j) {
                this.board[i][j] = marblesList.get(countList);
                ++countList;
            }
        }

        this.slide = marblesList.get(countList);
    }

    /**
     * getter
     * @return the market's board
     */
    public Marble[][] getBoard() {
        return this.board.clone();
    }

    /**
     * getter
     * @return the market's slide
     */
    public Marble getSlide() {
        return this.slide;
    }

    /**
     * This method
     * @param numRow the row's number
     * @param adder the player's interface
     * @return the number of white marbles drawn
     * @throws InvalidOperationException
     * @throws MatchEndedException if the player reaches the end of his faithPath
     */

    public int selectRow(int numRow, Adder adder) throws InvalidOperationException, MatchEndedException {
        int countWhite = 0;

        if(numRow < 0 || numRow > 3)
            throw new InvalidOperationException("The number of column is out of range");

        for(int i = 0; i < 4; ++i) {
            if (this.board[numRow][i].onDraw(adder)) {
                ++countWhite;
            }
        }

        this.rearrange(true, numRow);
        return countWhite;
    }

    public int selectColumn(int numColumn, Adder adder) throws MatchEndedException, InvalidOperationException
    {
        int countWhite = 0;

        if(numColumn < 0 || numColumn > 4)
            throw new InvalidOperationException("The number of column is out of range");

        for(int i = 0; i < 3; ++i) {
            if (this.board[i][numColumn].onDraw(adder)) {
                ++countWhite;
            }
        }

        this.rearrange(false, numColumn);
        return countWhite;
    }

    private void rearrange(boolean choice, int num) {
        Marble tempMarble = this.slide;
        int i;
        if (choice) {
            this.slide = this.board[num][0];

            for(i = 0; i < 3; ++i) {
                this.board[num][i] = this.board[num][i + 1];
            }

            this.board[num][3] = tempMarble;
        } else {
            this.slide = this.board[0][num];

            for(i = 0; i < 2; ++i) {
                this.board[i][num] = this.board[i + 1][num];
            }

            this.board[2][num] = tempMarble;
        }

    }
}
