package it.polimi.ingsw.model.essentials;

import java.util.Comparator;

/**
 * A comparator created to sort the DevelopmentCards
 */
public class CardComparator implements Comparator<DevelopmentCard> {

    /**
     * Compare two development cards, the first is lesser if its level is lesser or if the level of the two cards is the same
     * but che color of the first comes first in the CardColor order.
     * @param devCard1 the first development card to compare
     * @param devCard2 the second development card to compare
     * @return -1 if the first card is less then the second in the comparator's logic,
     * 0 if the two cards are equals in that logic and 1 if the first card is greater then the second in the comparator's logic
     */
    @Override
    public int compare(DevelopmentCard devCard1, DevelopmentCard devCard2) {
        if(devCard1.getType().getLevel() < devCard2.getType().getLevel() ||
                (devCard1.getType().getLevel() == devCard2.getType().getLevel() && devCard1.getType().getColor().getVal() < devCard2.getType().getColor().getVal()))
            return -1;
        else if (devCard1.getType().equals(devCard2.getType()))
                return 0;
        else
            return 1;
    }
}
