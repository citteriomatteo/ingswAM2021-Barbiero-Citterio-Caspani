package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.InvalidAddFaithException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.player.Adder;
import it.polimi.ingsw.model.match.player.Verificator;

public class PhysicalResource implements Resource, Requirable {
    private final ResType type;
    private final int quantity;

    public PhysicalResource(ResType type, int quantity) throws NegativeQuantityException {
        if(quantity >= 0)
            this.quantity = quantity;
        else
            throw new NegativeQuantityException("Negative quantity");

        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public ResType getType() {
        return type;
    }

    @Override
    public boolean add(Adder adder) throws InvalidAddFaithException {
        return adder.addToStrongBox(this);
    }

    @Override
    public boolean verify(Verificator verificator) {
        return verificator.verifyResources(this);
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof PhysicalResource)) {
            return false;
        }
        PhysicalResource physicalResource = (PhysicalResource) obj;

        return this.type.equals(physicalResource.type);

    }


    @Override
    public String toString() {
        return "PhysicalResource{" +
                "type=" + type +
                ", quantity=" + quantity +
                '}';
    }
}
