package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.model.exceptions.InvalidCardRequestException;
import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.exceptions.NoMoreCardsException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.player.Verificator;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class CardGrid {
    private Stack<DevelopmentCard>[][] topGrid;

    /*
    Builds the card grid receiving a list sorted by CardType in this order:
    {[lv1, GREEN], [lv1, BLUE], [lv1, YELLOW], [lv1, PURPLE], [lv2, GREEN], [lv2, BLUE], ...etc.}
    following the order of the color in the enumeration CardColor
    You have to provide at least one card of any type
     */
    public CardGrid(List<DevelopmentCard> cards) throws WrongSettingException {

        //initialization of variable count used for index of the cards list
        int count = 0;

        topGrid = new Stack[3][CardColor.values().length];
        for (int lv = 0; lv < 3; lv++) {
            for (CardColor color : CardColor.values()) {

                if(!cards.get(count).getType().getColor().equals(color) || cards.get(count).getType().getLevel()!=lv+1)
                    throw new WrongSettingException("Not enough CardTypes or given in the wrong order");

                topGrid[lv][color.ordinal()] = new Stack<>();
                while (count < cards.size() && cards.get(count).getType().getColor().equals(color) && cards.get(count).getType().getLevel()==lv+1) {
                    topGrid[lv][color.ordinal()].push(cards.get(count));
                    count++;
                }

            }
        }

    }

    public CardType[] countRemaining() {
        int count = 0;
        CardType[] res = new CardType[CardColor.values().length * 3];

        for (int lv = 0; lv < 3; lv++) {
            for (CardColor color : CardColor.values()) {
                try {
                    res[count] = new CardType(color, lv, topGrid[lv][color.ordinal()].size());
                    count++;
                } catch (InvalidQuantityException e) {
                    System.err.println("an error has occurred inside cardGrid");
                    e.printStackTrace();
                }
            }
        }

        return res;
    }

    public boolean isBuyable(Verificator verificator, int lv, int color) throws InvalidCardRequestException, NoMoreCardsException {
        if (lv > 3 || color > CardColor.values().length)
            throw new InvalidCardRequestException("Tried to take a card out of range");

        try {

            return topGrid[lv-1][color-1].peek().isBuyable(verificator);
        }
        catch (EmptyStackException e){
            throw new NoMoreCardsException("Tried to take a card from an empty stack");
        }

    }

    public DevelopmentCard take(int lv, int color) throws InvalidCardRequestException, NoMoreCardsException {
        if (lv > 3 || color > CardColor.values().length)
            throw new InvalidCardRequestException("Tried to take a card out of range");
        try {

            return topGrid[lv - 1][color - 1].pop();
        }
        catch (EmptyStackException e){
            throw new NoMoreCardsException("Tried to take a card from an empty stack");
        }
    }



}
