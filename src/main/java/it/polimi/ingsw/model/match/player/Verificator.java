package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.essentials.PhysicalResource;

public interface Verificator
{

    //this method verify the presence of the PhysicalResources in the warehouse or in the StrongBox
    boolean verifyResources(PhysicalResource physicalResource);

    //this method verify the presence of a card with the indicated color, level and quantity in the DevCardSlot
    boolean verifyCard(CardType card);

    //this method verifies if the card level is placeable in some cardSlots.
    boolean verifyPlaceability(int cardLevel);
}
