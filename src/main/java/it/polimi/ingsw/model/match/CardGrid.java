package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.match.player.Verificator;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * A grid of DevelopmentCard decks
 */
public class CardGrid {
    private final Stack<DevelopmentCard>[][] grid;
    static final int MAX_LEVEL = 3;

    /**
     * For tests only
     * Return a copy of the two-dimensional array of decks that form the grid
     * @return a copy of the entire grid
     */
    public Stack<DevelopmentCard>[][] getGrid() {
        return grid.clone();
    }

    /**
     * Builds the card grid starting from a list of DevelopmentCards sorted by CardType in this order:
     * {[lv1, GREEN], [lv1, BLUE], [lv1, YELLOW], [lv1, PURPLE], [lv2, GREEN], [lv2, BLUE], ...etc.}
     * following the order of the color in the enumeration CardColor.
     * You have to provide at least one card of any type.
     * @requires The list 'cards' have to be previously correctly sorted
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
     * @param lv the level of the card or the row of the grid in which it is placed
     * @param color the int value for the color of the card ({@link CardColor#getVal()})
     *              or the column of the grid in which it is placed
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
     * @param lv the level of the card or the row of the grid in which it is placed
     * @param color the int value for the color of the card ({@link CardColor#getVal()})
     *              or the column of the grid in which it is placed
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
     * draws the first DevelopmentCard from the deck in this grid in the given position (or with the given level and color).
     * This method doesn't control if the player has already payed the resources to buy that card.
     * @param lv the level of the card or the row of the grid in which it is placed
     * @param color the int value for the color of the card ({@link CardColor#getVal()})
     *              or the column of the grid in which it is placed
     * @return the drawn DevelopmentCard
     * @throws InvalidCardRequestException if it is tried to draw a card out of range
     * @throws NoMoreCardsException if it is tried to draw a card from an empty stack
     * @throws LastRoundException never
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
}