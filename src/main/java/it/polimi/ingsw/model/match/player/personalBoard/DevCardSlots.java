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
    private static final int NUMBEROFROWS = 3;
    private static final int NUMBEROFCOLUMNS = 3;

    public DevCardSlots() {
        slots = new DevelopmentCard[NUMBEROFROWS][NUMBEROFCOLUMNS];
    }

    //Verify if the card could be pushed and than add it to the column
    public boolean pushNewCard(int column, DevelopmentCard card) throws InvalidOperationException {
        if (column < 1 || column > NUMBEROFCOLUMNS)
            throw new InvalidOperationException("Tried to insert card out of bounds");

        int position = firstEmptySpace(column);
        if (position>=NUMBEROFROWS)
            throw new FullColumnException("Tried to add a card to a full stack");

        if(card.getType().getLevel()==1 ||
                (position > 0 && card.getType().getLevel()<=slots[position-1][column-1].getType().getLevel()+1)) {
            slots[position][column - 1] = card;
            return true;
        }
        else throw new HighCardLevelException("Tried to add a card of level " + card.getType().getLevel() + " in a non valid position");
    }

    //find the first empty slot in a column, if the column is full returns -1
    private int firstEmptySpace(int column){
        for (int i = 0; i < NUMBEROFROWS; i++) {
            if (slots[i][column-1]==null)
                return i;
        }
        return NUMBEROFROWS;
    }

    //Returns the cards on the top of each stack
    public List<DevelopmentCard> getTop(){
        List<DevelopmentCard> res = new ArrayList<>();
        int pointer;
        for (int i = 1; i <= NUMBEROFCOLUMNS; i++) {
            pointer = firstEmptySpace(i);
            if(pointer>0)
                res.add(slots[pointer-1][i-1]);
        }
        return res;
    }



    public int getCardsNumber(){
        //TODO
        return 1;
    }

    public int getWinPoints(){
        //TODO
        return 1;
    }

    public boolean isPlaceable(int cardLevel){
        //TODO
        return true;
    }

    public boolean isSatisfied(CardType requirements){
        //TODO
        return true;
    }
}
