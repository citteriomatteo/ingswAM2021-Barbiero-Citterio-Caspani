package it.polimi.ingsw.model.match.player.personalBoard;

import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.exceptions.FullColumnException;
import it.polimi.ingsw.exceptions.HighCardLevelException;
import it.polimi.ingsw.exceptions.InvalidOperationException;
import it.polimi.ingsw.model.match.CardGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the place where the player holds his DevelopmentCards purchased by the CardGrid
 * @see CardGrid
 * @see DevelopmentCard
 */
public class DevCardSlots
{
    private final ArrayList<DevelopmentCard>[] slots;
    private static final int NUMBER_OF_ROWS = 3;
    private static final int NUMBER_OF_COLUMNS = 3;

    /**
     * Simple constructor, it instantiates the DevCardSlots of the default dimensions
     */
    public DevCardSlots() {
        slots = new ArrayList[NUMBER_OF_COLUMNS];
        for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            slots[i] = new ArrayList<>(NUMBER_OF_ROWS);
        }
    }

    /**
     * Verify if the card could be pushed to the given column and than add it to the given column
     * @param column the column in which the caller wanted to insert the card
     * @param card the DevelopmentCard that has to be inserted
     * @return true
     * @throws InvalidOperationException if the given parameters generate a non valid request
     */
    public boolean pushNewCard(int column, DevelopmentCard card) throws InvalidOperationException
    {
        if (column < 1 || column > NUMBER_OF_COLUMNS)
            throw new InvalidOperationException("Tried to insert card out of bounds");

        int position = firstEmptySpace(column);
        if (position > NUMBER_OF_ROWS)
            throw new FullColumnException("Tried to add a card to a full stack");

        if(card.getType().getLevel() == firstEmptySpace(column)) {
            slots[column - 1].add(card);
            return true;
        }
        else
            throw new HighCardLevelException("Tried to add a card of level " + card.getType().getLevel() + " in a non valid position");
    }

    /**
     * Finds the first empty slot in a column, if the column is full returns NUMBER_OF_ROWS
     * @param column the column in which to search for empty space
     * @return the first empty slot in a column, if the column is full returns NUMBER_OF_ROWS
     */
    private int firstEmptySpace(int column)
    {
        return slots[column-1].size()+1;
    }

    /**
     * Returns the card on the top of each stack
     * @return available DevelopmentCards for production, the ones on the top of each stack
     */
    public List<DevelopmentCard> getTop()
    {
        List<DevelopmentCard> res = new ArrayList<>(NUMBER_OF_COLUMNS);

        for (List<DevelopmentCard> currentColumn : slots)
        {
            if(!currentColumn.isEmpty()){
                res.add(currentColumn.get(currentColumn.size()-1));
            }
        }
        return res;
    }

    /** @return the number of cards in all the structure */
    public int getCardsNumber(){
        int cardCount = 0;
        for (List<DevelopmentCard> currentColumn : slots)
            cardCount += currentColumn.size();

        return cardCount;
    }

    /** @return the sum of all the WinPoints given from the DevelopmentCards in this structure */
    public int getWinPoints()
    {
        int totalWinPoints = 0;
        for (List<DevelopmentCard> currentColumn : slots) {
            for (DevelopmentCard card : currentColumn)
                totalWinPoints += card.getWinPoints();
        }

        return totalWinPoints;
    }

    /**
     * Verifies if there is a free slot over a card whose level is: cardLevel-1.
     * If such a card exists it means that a card of the given type is placeable in this grid.
     * @param cardLevel the level of a card you want to verify if is placeable
     * @return true if the a card of the given level is placeable
     */
    public boolean isPlaceable(int cardLevel)
    {
        for (int i = 1; i <= NUMBER_OF_COLUMNS; i++)
        {
            if(cardLevel==firstEmptySpace(i))
                return true;
        }
        return false;
    }

    /**
     * Verifies if the requirement is satisfied:
     *     If the level in requirement is = 0 -> Controls if there are 'quantity' cards of that color
     *     Otherwise -> Controls if there are 'quantity' cards of that type
     * @param requirement the color (and the level) of the cards you want to verify the presence
     *                   in quantity equal to the 'quantity' of the CardType
     * @see CardType
     * @return true if the requirement is satisfied
     */
    public boolean isSatisfied(CardType requirement){

        int toSearch = requirement.getQuantity();
        if(requirement.getLevel()==0){
            for (List<DevelopmentCard> currentColumn : slots){
                for (DevelopmentCard card : currentColumn)
                {
                    if(requirement.getColor().equals(card.getType().getColor()))
                    {
                        toSearch--;
                        if (toSearch == 0)
                            return true;
                    }
                }
            }
            return false;
        }

        for (List<DevelopmentCard> currentColumn : slots){
            for (DevelopmentCard card : currentColumn)
            {
                if(requirement.equals(card.getType()))
                {
                    toSearch--;
                    if (toSearch == 0)
                        return true;
                }
            }
        }
        return false;
    }
}