package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import it.polimi.ingsw.model.match.player.Adder;

public class FaithPoint implements Resource{
    private int quantity;

    public FaithPoint(int quantity) {
        this.quantity = quantity;
    }


    @Override
    public boolean add(Adder adder) throws InvalidAddFaithException {
       return adder.addFaithPoints(quantity);
    }
}
