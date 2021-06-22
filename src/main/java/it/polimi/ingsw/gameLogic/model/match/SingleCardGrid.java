package it.polimi.ingsw.gameLogic.model.match;

import it.polimi.ingsw.gameLogic.model.essentials.CardColor;
import it.polimi.ingsw.gameLogic.model.essentials.CardType;
import it.polimi.ingsw.gameLogic.model.essentials.DevelopmentCard;
import it.polimi.ingsw.gameLogic.exceptions.InvalidCardRequestException;
import it.polimi.ingsw.gameLogic.exceptions.LastRoundException;
import it.polimi.ingsw.gameLogic.exceptions.NoMoreCardsException;
import it.polimi.ingsw.gameLogic.exceptions.WrongSettingException;

import java.util.List;

/**
 * A CardGrid for single player match, it is exactly equals to a normal cardGrid but you can discard cards from it
 * @see CardGrid
 */
public class SingleCardGrid extends CardGrid {
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
    public SingleCardGrid(List<DevelopmentCard> cards) throws WrongSettingException {
        super(cards);
    }

    /**
     * The same method as CardGrid but before return it controls if the card of the selected color are finished
     * @param lv the level of the card or the row of the grid in which it is placed
     * @param color the int value for the color of the card ({@link CardColor#getVal()})
     *              or the column of the grid in which it is placed
     * @return the drawn DevelopmentCard
     * @throws InvalidCardRequestException if it is tried to draw a card out of range
     * @throws NoMoreCardsException if it is tried to draw a card from an empty stack
     * @throws LastRoundException if Cards of that color are finished: Lorenzo win the game
     * @see CardGrid#take(int, int)
     */
    @Override
    public DevelopmentCard take(int lv, int color) throws InvalidCardRequestException, NoMoreCardsException, LastRoundException {
        if (lv<MAX_LEVEL)
            return super.take(lv, color);
        DevelopmentCard res = null;
        try {
            res = super.take(lv, color);
        } catch (NoMoreCardsException e) {
            System.err.println("System shutdown due to internal error in SingleCardGrid");
            e.printStackTrace();
            System.exit(1);
        }

        if(super.isEmpty(lv, color))
            //Cards of that color are finished: Lorenzo win the game -> You lost
            throw new LastRoundException("Player have lost -> Lorenzo finished the " + color + " cards");

        return res;
    }

    /**
     * Discards 2 cards of the given color, starting from the lowest level possible
     * @param color the color of the cards to discard
     * @return true
     * @throws LastRoundException if Cards of that color are finished: Lorenzo win the game
     */
    public boolean discard(CardColor color) throws LastRoundException {
        for (int i = 0; i < 2; i++) {
            try {
                //try to remove level 1
                super.take(1, color.getVal());

            } catch (InvalidCardRequestException e) {
                e.printStackTrace();
                System.exit(1);

            } catch (NoMoreCardsException e) {
                try {
                    //try to remove level 2
                    super.take(2, color.getVal());

                } catch (InvalidCardRequestException e2) {
                    e2.printStackTrace();
                    System.exit(1);

                } catch (NoMoreCardsException e2) {
                    try {
                        //try to remove level 3
                        super.take(3, color.getVal());
                        if(super.isEmpty(3, color.getVal()))
                            //Cards of that color are finished: Lorenzo win the game -> You lost
                            throw new LastRoundException("Player have lost -> Lorenzo finished the " + color + " cards");

                    } catch (InvalidCardRequestException | NoMoreCardsException e3) {
                        e3.printStackTrace();
                        System.exit(1);

                    }
                }
            }
        }
        return true;
    }
}
