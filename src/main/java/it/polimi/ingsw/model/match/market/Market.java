package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Market {
    private Marble[][] board = new Marble[3][4];
    private Marble slide;

    public Market(Marble[][] board, Marble slide) {
        this.board = board;
        this.slide = slide;
    }

    public Market() {
        List<Marble> marblesList = new ArrayList<>();

        int countList;
        for(countList = 0; countList < 4; ++countList) {
            marblesList.add(new WhiteMarble());
        }

        for(countList = 4; countList < 6; ++countList) {
            marblesList.add(new BlueMarble());
        }

        for(countList = 6; countList < 8; ++countList) {
            marblesList.add(new GreyMarble());
        }

        for(countList = 8; countList < 10; ++countList) {
            marblesList.add(new YellowMarble());
        }

        for(countList = 10; countList < 12; ++countList) {
            marblesList.add(new PurpleMarble());
        }

        marblesList.add(new RedMarble());
        Collections.shuffle(marblesList);
        countList = 0;

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 4; ++j) {
                this.board[i][j] = marblesList.get(countList);
                ++countList;
            }
        }

        this.slide = marblesList.get(countList);
    }

    public Marble[][] getBoard() {
        return this.board;
    }

    public Marble getSlide() {
        return this.slide;
    }

    public int selectRow(int numRow, Adder adder) throws NegativeQuantityException, InvalidQuantityException {
        int countWhite = 0;

        if(numRow < 0 || numRow > 3)
            throw new InvalidQuantityException("The number of column is out of range");

        for(int i = 0; i < 4; ++i) {
            if (this.board[numRow][i].onDraw(adder)) {
                ++countWhite;
            }
        }

        this.rearrange(true, numRow);
        return countWhite;
    }

    public int selectColumn(int numColumn, Adder adder) throws NegativeQuantityException, InvalidQuantityException {
        int countWhite = 0;

        if(numColumn < 0 || numColumn > 4)
            throw new InvalidQuantityException("The number of column is out of range");

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
