package it.polimi.ingsw.gameLogic.model.essentials;

import it.polimi.ingsw.gameLogic.exceptions.NegativeQuantityException;
import it.polimi.ingsw.gameLogic.model.match.player.Adder;
import it.polimi.ingsw.gameLogic.model.match.player.Verificator;

/**
 * Basic element of the game, it is an immutable element.
 * Represents a physical resource with his multiplicity, the possible types for the resource are defined in the enumeration
 * {@link ResType}
 */
public class PhysicalResource implements Resource, Requirable
{
    private final ResType type;
    private final int quantity;

    /**
     * Class constructor
     * @param type sets the type of the PhysicalResource
     * @param quantity sets the number of ResType in this object
     * @throws NegativeQuantityException if quantity is negative
     */
    public PhysicalResource(ResType type, int quantity) throws NegativeQuantityException {
        if(quantity >= 0)
            this.quantity = quantity;
        else
            throw new NegativeQuantityException("Negative quantity");

        this.type = type;
    }

    /**
     * Getter
     * @return true to show that this is a physical resource.
     */
    @Override
    public boolean isPhysical(){ return true; }

    /**
     * getter
     * @return the number of ResType that are contained
     */
    @Override
    public int getQuantity() {
        return quantity;
    }

    /**
     * getter
     * @return the ResType of the resources contained
     */
    public ResType getType() {
        return type;
    }

    /**
     * Adds the PhysicalResource to the player's strongbox
     * @param adder the player's interface
     * @return true if adder.addToStrongBox works
     */
    @Override
    public boolean add(Adder adder)
    {
        return adder.addToStrongBox(this);
    }

    /**
     * Verify if the player has the resources contained in this
     * @param verificator the player's interface
     * @return true if the player has the resources contained in this
     */
    @Override
    public boolean verify(Verificator verificator) {
        return verificator.verifyResources(this);
    }

    /**
     * The override of the equals method
     * @param obj the other PhysicalResources object to be compared
     * @return true if this and obj has the same ResType
     */
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof PhysicalResource)) {
            return false;
        }
        PhysicalResource physicalResource = (PhysicalResource) obj;

        return this.type.equals(physicalResource.type);

    }

    /**
     * This method returns true if the Requirable object is a resource, else false.
     * @return true
     */
    @Override
    public boolean isAResource() { return true; }

    @Override
    public String toString() {
        return "PhysicalResource{" +
                "type=" + type +
                ", quantity=" + quantity +
                '}';
    }
}
