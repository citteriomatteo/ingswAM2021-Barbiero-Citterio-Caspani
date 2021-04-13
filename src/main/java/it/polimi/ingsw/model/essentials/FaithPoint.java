package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.FaithPathCreationException;

import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;

public class FaithPoint implements Resource{
    private final int quantity;

    public FaithPoint(int quantity) throws NegativeQuantityException {
        if(quantity >= 0)
            this.quantity = quantity;
        else
            throw new NegativeQuantityException("Negative quantity of faith");
    }


    @Override
    public boolean add(Adder adder) throws MatchEndedException {
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
