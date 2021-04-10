package it.polimi.ingsw.model.match.player.personalBoard;

import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.model.exceptions.FullColumnException;
import it.polimi.ingsw.model.exceptions.HighCardLevelException;
import it.polimi.ingsw.model.exceptions.InvalidOperationException;

import java.util.ArrayList;
import java.util.List;

public class DevCardSlots {
    private final DevelopmentCard[][] slots;
    private static final int NUMBER_OF_ROWS = 3;
    private static final int NUMBER_OF_COLUMNS = 3;

    public DevCardSlots() {
        slots = new DevelopmentCard[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
    }

    //Verify if the card could be pushed and than add it to the column
    public boolean pushNewCard(int column, DevelopmentCard card) throws InvalidOperationException {
        if (column < 1 || column > NUMBER_OF_COLUMNS)
            throw new InvalidOperationException("Tried to insert card out of bounds");

        int position = firstEmptySpace(column);
        if (position>= NUMBER_OF_ROWS)
            throw new FullColumnException("Tried to add a card to a full stack");

        if(card.getType().getLevel()==1 ||
                (position > 0 && card.getType().getLevel()<=slots[position-1][column-1].getType().getLevel()+1)) {
            slots[position][column - 1] = card;
            return true;
        }
        else throw new HighCardLevelException("Tried to add a card of level " + card.getType().getLevel() + " in a non valid position");
    }



    //find the first empty slot in a column, if the column is full returns NUMBEROFROWS
    private int firstEmptySpace(int column){
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            if (slots[i][column-1]==null)
                return i;
        }
        return NUMBER_OF_ROWS;
    }



    //Returns the cards on the top of each stack
    public List<DevelopmentCard> getTop(){
        List<DevelopmentCard> res = new ArrayList<>();
        int pointer;
        for (int i = 1; i <= NUMBER_OF_COLUMNS; i++) {
            pointer = firstEmptySpace(i);
            if(pointer>0)
                res.add(slots[pointer-1][i-1]);
        }
        return res;
    }


    //Returns the number of cards in all the structure
    public int getCardsNumber(){
        int cardCount = 0;

        for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < NUMBER_OF_ROWS; j++) {
                if (slots[j][i]==null)
                    break;
                cardCount++;
            }
        }
        return cardCount;
    }


    //Returns the sum of WinPoints from all the DevelopmentCards
    public int getWinPoints(){
        int totalWinPoints = 0;

        for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < NUMBER_OF_ROWS; j++) {
                if (slots[j][i]==null)
                    break;
                totalWinPoints += slots[j][i].getWinPoints();
            }
        }
        return totalWinPoints;
    }

    //Verify if there is a free slot over a card whose level is: cardLevel-1
    public boolean isPlaceable(int cardLevel){
        if (cardLevel == 1)
            return true;

        int pointer;
        for (int i = 1; i <= NUMBER_OF_COLUMNS; i++) {
            pointer = firstEmptySpace(i);
            if(pointer>0 && pointer< NUMBER_OF_ROWS && cardLevel-1==slots[pointer-1][i-1].getType().getLevel())
                return true;
        }

        return false;
    }

    /*
    If the level in requirements is = 0 -> Controls if there are 'quantity' cards of that color
    Otherwise -> Controls if there are 'quantity' cards of that type
     */
    public boolean isSatisfied(CardType requirements){
        int toSearch = requirements.getQuantity();
        if(requirements.getLevel()==0){
            for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
                for (int j = 0; j < NUMBER_OF_ROWS; j++) {
                    if (slots[j][i] == null)
                        break;
                    if(requirements.getColor().equals(slots[j][i].getType().getColor())) {
                        toSearch--;
                        if (toSearch == 0)
                            return true;
                    }
                }
            }
            return false;
        }


        for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < NUMBER_OF_ROWS; j++) {
                if (slots[j][i] == null)
                    break;
                if (requirements.equals(slots[j][i].getType())) {
                    toSearch--;
                    if (toSearch == 0)
                        return true;
                }
            }
        }
        return false;
    }
}
