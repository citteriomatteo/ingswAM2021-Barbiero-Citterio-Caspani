package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import it.polimi.ingsw.model.match.player.Adder;

import java.util.Objects;

public class FaithPoint implements Resource{
    private final int quantity;

    public FaithPoint(int quantity) throws InvalidAddFaithException {
        if(quantity > 0)
            this.quantity = quantity;
        else
            throw new InvalidAddFaithException("Negative quantity of faith");
    }


    @Override
    public boolean add(Adder adder) throws InvalidAddFaithException {
       return adder.addFaithPoints(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FaithPoint that = (FaithPoint) o;
        return quantity == that.quantity;
    }


    @Override
    public String toString() {
        return "FaithPoint{" +
                "quantity=" + quantity +
                '}';
    }
}
