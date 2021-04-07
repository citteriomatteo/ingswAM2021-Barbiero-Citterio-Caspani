package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import it.polimi.ingsw.model.match.player.Adder;

public class FaithPoint implements Resource{
    private int quantity;

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
}
