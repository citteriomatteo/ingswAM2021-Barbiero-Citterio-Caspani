package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.match.player.personalBoard.Effecter;

import java.util.Objects;
/**
 * Auxiliary class used for implementing Strategy pattern for the LeaderCard effect : Discount
 */
public class DiscountEffect implements Effect{
    private final PhysicalResource discount;

    /**
     * Simple constructor
     * @param discount set in 'quantity' the entity of the discount on a resource of the relative type
     */
    public DiscountEffect(PhysicalResource discount) {
        this.discount = discount;
    }

    /**
     * Sets the relative discount in the discount map
     * @param effecter the PersonalBoard owned by the player who wants to activate the LeaderCard effect
     * @return false
     */
    @Override
    public boolean activate(Effecter effecter)
    {
        effecter.setDiscount(discount);
        return false;
    }

    @Override
    public String toString() {
        return "DiscountEffect{" +
                "discount=" + discount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountEffect that = (DiscountEffect) o;
        return Objects.equals(discount, that.discount);
    }


}
