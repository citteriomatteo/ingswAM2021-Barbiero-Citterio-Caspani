package it.polimi.ingsw.model.match.market;

import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.exceptions.LastRoundException;
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
        return this.board;
    }

    /**
     * getter
     * @return the market's slide
     */
    public Marble getSlide() {
        return this.slide;
    }

    /**
     * This method draw all the marbles in the selected row
     * @param numRow the row's number
     * @param adder the player's interface
     * @return the number of white marbles drawn
     * @throws InvalidOperationException if the numRow is less than 0 or more than 3
     * @throws LastRoundException if the player reaches the end of his faithPath
     */
    public int selectRow(int numRow, Adder adder) throws InvalidOperationException, LastRoundException
    {
        LastRoundException exc = null;
        numRow--;
        int countWhite = 0;

        if(numRow < 0 || numRow >= 3)
            throw new InvalidOperationException("The number of column is out of range");

        for(int i = 0; i < 4; ++i)
            try {
                if (this.board[numRow][i].onDraw(adder))
                    ++countWhite;
            }
            catch(LastRoundException e){ exc = e; }

        if(exc != null)
            throw exc;

        this.rearrange(true, numRow);
        return countWhite;
    }

    /**
     * This method draw all the marbles in the selected column
     * @param numColumn the column's number
     * @param adder the player's interface
     * @return the number of white marbles drawn
     * @throws LastRoundException if the numColumn is less than 0 or more than 4
     * @throws InvalidOperationException if the player reaches the end of his faithPath
     */
    public int selectColumn(int numColumn, Adder adder) throws LastRoundException, InvalidOperationException
    {
        LastRoundException exc = null;
        numColumn--;
        int countWhite = 0;

        if(numColumn < 0 || numColumn >= 4)
            throw new InvalidOperationException("The number of column is out of range");

        for(int i = 0; i < 3; ++i)
            try {
                if (this.board[i][numColumn].onDraw(adder))
                    ++countWhite;
            }
            catch(LastRoundException e){ exc = e; }

        if(exc != null)
            throw exc;

        this.rearrange(false, numColumn);
        return countWhite;
    }

    /**
     * This method rearrange the market board after the calls of selectColumn or selectRow
     * @param choice true means that this method was called by selectRow, false means that this method was called by selectColumn
     * @param num the number of column or row
     */
    private void rearrange(boolean choice, int num)
    {
        Marble tempMarble = this.slide;
        int i;
        if (choice)
        {
            this.slide = this.board[num][0];
            for(i = 0; i < 3; ++i)
                this.board[num][i] = this.board[num][i + 1];
            this.board[num][3] = tempMarble;
        }
        else
            {
                this.slide = this.board[0][num];
                for(i = 0; i < 2; ++i)
                    this.board[i][num] = this.board[i + 1][num];
                this.board[2][num] = tempMarble;
            }
    }

}
