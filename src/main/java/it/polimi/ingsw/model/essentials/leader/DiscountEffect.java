package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.match.player.personalBoard.Effecter;

import java.util.Objects;

public class DiscountEffect implements Effect{
    //The 'discount' variable set in 'quantity' the entity of the discount on a resource of the relative type
    private final PhysicalResource discount;

    public DiscountEffect(PhysicalResource discount) {
        this.discount = discount;
    }

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
