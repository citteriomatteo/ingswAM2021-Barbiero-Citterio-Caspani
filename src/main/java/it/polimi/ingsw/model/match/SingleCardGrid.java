package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.model.exceptions.InvalidCardRequestException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NoMoreCardsException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;

import java.util.List;

public class SingleCardGrid extends CardGrid {

    public SingleCardGrid(List<DevelopmentCard> cards) throws WrongSettingException {
        super(cards);
    }


    //Discards 2 cards of the given color, starting from the lowest level possible
    public boolean discard(CardColor color) throws MatchEndedException {
        for (int i = 0; i < 2; i++) {

            try {
                //try to remove level 1
                super.take(1, color.ordinal());

            } catch (InvalidCardRequestException e) {
                e.printStackTrace();
                System.exit(1);

            } catch (NoMoreCardsException e) {
                try {
                    //try to remove level 2
                    super.take(2, color.ordinal());

                } catch (InvalidCardRequestException e2) {
                    e2.printStackTrace();
                    System.exit(1);

                } catch (NoMoreCardsException e2) {
                    try {
                        //try to remove level 3
                        super.take(3, color.ordinal());

                    } catch (InvalidCardRequestException e3) {
                        e3.printStackTrace();
                        System.exit(1);

                    } catch (NoMoreCardsException e3) {
                        //Cards of that color are finished: Lorenzo win the game -> You lost
                        throw new MatchEndedException("Player have lost -> Lorenzo finished the " + color + " cards");
                    }
                }
            }
        }
        return true;
    }
}
