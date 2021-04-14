package it.polimi.ingsw.model.essentials.leader;


import it.polimi.ingsw.model.essentials.DevelopmentCard;

import java.util.Comparator;

public class CardComparator implements Comparator<DevelopmentCard> {

    @Override
    public int compare(DevelopmentCard devCard1, DevelopmentCard devCard2) {
        if(devCard1.getType().getLevel() < devCard2.getType().getLevel() ||
                (devCard1.getType().getLevel() == devCard2.getType().getLevel() && devCard1.getType().getColor().getVal() < devCard2.getType().getColor().getVal()))
            return -1;
        else
            return 1;
    }
}
