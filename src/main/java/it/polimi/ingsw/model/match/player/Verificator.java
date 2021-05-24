package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;

import java.util.List;
import java.util.Map;

public interface Verificator
{
    /**
     * this method verifies the presence of the PhysicalResources in the warehouse or in the StrongBox.
     * @param physicalResource  the resource to check
     * @return                  true if it's ok, else false
     */
    boolean verifyResources(PhysicalResource physicalResource);

    /**
     * this method verifies the presence of a card with the indicated color, level and quantity in the DevCardSlot.
     * @param card the card to check
     * @return     true if it's ok, else false
     */
    boolean verifyCard(CardType card);

    /**
     * this method verifies if the card level is placeable in some cardSlots.
     * @param cardLevel the card level to check
     * @return          true if it's placeable, else false
     */
    boolean verifyPlaceability(int cardLevel);

    List<PhysicalResource> getDiscountedCosts(List<PhysicalResource> initialCosts);
}
