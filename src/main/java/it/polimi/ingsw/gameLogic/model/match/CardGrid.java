package it.polimi.ingsw.gameLogic.model.match;

import it.polimi.ingsw.gameLogic.model.essentials.CardColor;
import it.polimi.ingsw.gameLogic.model.essentials.CardType;
import it.polimi.ingsw.gameLogic.model.essentials.DevelopmentCard;
import it.polimi.ingsw.gameLogic.exceptions.*;
import it.polimi.ingsw.gameLogic.model.match.player.Verificator;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * A grid of DevelopmentCard decks
 */
public class  CardGrid {
    private final Stack<DevelopmentCard>[][] grid;
    static final int MAX_LEVEL = 3;

    /**
     * Returns a shallow copy of the two-dimensional array of decks that form the grid, changes in Stack objects will be transferred into this.
     * Use with caution
     * @return a copy of the entire grid
     */
    public Stack<DevelopmentCard>[][] getGrid() {
        Stack<DevelopmentCard>[][] res = new Stack[MAX_LEVEL][CardColor.values().length];
        for (int i = 0; i < MAX_LEVEL; i++) {
            res[i] = grid[i].clone();
        }
        return res;
//        return grid.clone();
    }

    /**
     * Builds the card grid starting from a list of DevelopmentCards sorted by CardType in this order:
     * {[lv1, GREEN], [lv1, BLUE], [lv1, YELLOW], [lv1, PURPLE], [lv2, GREEN], [lv2, BLUE], ...etc.}
     * following the order of the color in the enumeration CardColor.
     * You have to provide at least one card of any type.
     * Requires that the list 'cards' have to be previously correctly sorted
     * @param cards a sorted list of all the DevelopmentCards in the game
     * @throws WrongSettingException if are given not enough CardTypes or in the wrong order
     * @see CardType
     * @see DevelopmentCard
     */
    public CardGrid(List<DevelopmentCard> cards) throws WrongSettingException {
        //initialization of variable count used for index of the cards list and of the empty grid
        int count = 0;
        grid = new Stack[MAX_LEVEL][CardColor.values().length];

        for (int lv = 0; lv < MAX_LEVEL; lv++) {
            for (CardColor color : CardColor.values()) {

                if(!cards.get(count).getType().getColor().equals(color) || cards.get(count).getType().getLevel()!=lv+1)
                    throw new WrongSettingException("Not enough CardTypes or given in the wrong order");

                grid[lv][color.ordinal()] = new Stack<>();
                while (count < cards.size() && cards.get(count).getType().getColor().equals(color)
                        && cards.get(count).getType().getLevel()==lv+1) {
                    grid[lv][color.ordinal()].push(cards.get(count));
                    count++;
                }
                //After creating all the single stacks, they are shuffled
                Collections.shuffle(grid[lv][color.ordinal()]);
            }
        }

    }

    /**
     * Counts the occurrences of each CardType in this
     * @return an array of CardType where the value 'quantity' reports the occurrences of the own type in this
     * @see CardType
     */
    public CardType[] countRemaining() {
        int count = 0;
        CardType[] res = new CardType[CardColor.values().length * MAX_LEVEL];

        for (int lv = 0; lv < MAX_LEVEL; lv++) {
            for (CardColor color : CardColor.values()) {
                try {
                    res[count] = new CardType(color, lv, grid[lv][color.ordinal()].size());
                    count++;
                } catch (InvalidQuantityException e) {
                    System.err.println("System shutdown. An error has occurred inside cardGrid");
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
        return res;
    }

    /**
     * Returns True if the card in the given position is buyable by the player passed as verificator.
     * The method controls if the player has the resources either in the warehouse or in the strongbox.
     * @param verificator the player who wants to buy the card
     * @param lv the level of the card or the row of the grid in which it is placed (starting from 1)
     * @param color the int value for the color of the card ({@link CardColor#getVal()})
     *              or the column of the grid in which it is placed (starting from 1)
     * @return true if the card in the given position is buyable by the player passed as verificator
     * @throws InvalidCardRequestException if it is tried to control a card out of range
     * @throws NoMoreCardsException if it is tried to control a card from an empty stack
     */
    public boolean isBuyable(Verificator verificator, int lv, int color) throws InvalidCardRequestException, NoMoreCardsException {
        if (lv < 1 || lv > MAX_LEVEL || color < 1 || color > CardColor.values().length)
            throw new InvalidCardRequestException("Tried to control a card out of range");

        try {
            return grid[lv-1][color-1].peek().isBuyable(verificator);
        }
        catch (EmptyStackException e){
            throw new NoMoreCardsException("Tried to control a card from an empty stack");
        }
    }

    /**
     * Verifies if the stack in the given position is empty
     * @param lv the level of the card or the row of the grid in which it is placed (starting from 1)
     * @param color the int value for the color of the card ({@link CardColor#getVal()})
     *              or the column of the grid in which it is placed (starting from 1)
     * @return true if the stack in the given position is empty
     * @throws InvalidCardRequestException if it is tried to control a stack out of range
     */
     boolean isEmpty(int lv, int color) throws InvalidCardRequestException {
        if (lv < 1 || lv > MAX_LEVEL || color < 1 || color > CardColor.values().length)
            throw new InvalidCardRequestException("Tried to control a card out of range");
        try {
            grid[lv-1][color-1].peek();
        }
        catch (EmptyStackException e){
            return true;
        }
        return false;
    }

    /**
     * Checks if there's an empty column
     * This method is used in matchEndingProcedure (MatchController.class) to see if Lorenzo has won the match or not.
     * @return true if an empty column exists, false elsewhere.
     */
    public boolean emptyColumnExists() {
        for(int i = 1; i <= CardColor.values().length; i++)
            try {
                if(isEmpty(1,i) && isEmpty(2,i) && isEmpty(3,i))
                    return true;
            } catch (InvalidCardRequestException exc) { return false; }
        return false;
    }

    /**
     * draws the first DevelopmentCard from the deck in this grid in the given position (or with the given level and color).
     * This method doesn't control if the player has already payed the resources to buy that card.
     * @param lv the level of the card or the row of the grid in which it is placed (starting from 1)
     * @param color the int value for the color of the card ({@link CardColor#getVal()})
     *              or the column of the grid in which it is placed (starting from 1)
     * @return the drawn DevelopmentCard
     * @throws InvalidCardRequestException if it is tried to draw a card out of range
     * @throws NoMoreCardsException if it is tried to draw a card from an empty stack
     * @throws LastRoundException never in multiplayer
     */
    public DevelopmentCard take(int lv, int color) throws InvalidCardRequestException, NoMoreCardsException, LastRoundException {
        if (lv < 1 || lv > MAX_LEVEL || color < 1 || color > CardColor.values().length)
            throw new InvalidCardRequestException("Tried to take a card out of range");
        try {
            return grid[lv - 1][color-1].pop();
        }
        catch (EmptyStackException e){
            throw new NoMoreCardsException("Tried to take a card from an empty stack");
        }
    }

    /**
     * @return a matrix containing the first card of each stack of this in the right position
     */
    public DevelopmentCard[][] getTop(){
        DevelopmentCard[][] res = new DevelopmentCard[MAX_LEVEL][CardColor.values().length];
        for (int lv = 0; lv < MAX_LEVEL; lv++) {
            for (CardColor color : CardColor.values()) {
                if (!grid[lv][color.ordinal()].empty())
                    res[lv][color.ordinal()] = grid[lv][color.ordinal()].peek();
            }
        }
        return res;
    }

    /**
     * Returns the first card of the deck in the given position without removing it
     * @param row the number of row (starting from 1)
     * @param col the number of column (starting from 1)
     * @return the first card of the deck in the given position
     */
    public DevelopmentCard peek(int row, int col){
        return grid[row-1][col-1].peek();
    }
}