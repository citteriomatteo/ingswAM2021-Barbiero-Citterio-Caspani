package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.essentials.PhysicalResource;

public interface Verificator {

    //this method verify the presence of the PhisicalResources in the wherhouse or in the StrongBox
    boolean verifyResources(PhysicalResource physicalResource);

    ////this method verify the presence of a card with the indicated color, level and quantity
    boolean verifyCard(CardType card);
}
