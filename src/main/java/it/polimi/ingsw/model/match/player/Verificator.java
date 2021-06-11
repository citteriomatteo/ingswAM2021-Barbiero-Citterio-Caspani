package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.essentials.PhysicalResource;

import java.util.List;

/**
 * This interface allows the implementers to access to some verify methods on the Player's class.
 */
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

    /**
     * This method gets some costs, discounts them and returns a copy of the discounted list.
     * @param initialCosts the initial costs' list
     * @return the discounted list
     */
    List<PhysicalResource> getDiscountedCosts(List<PhysicalResource> initialCosts);
}
