package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.essentials.PhysicalResource;

public class Player implements Adder, Verificator {
    @Override
    public boolean addFaithPoints(int quantity) {
        //TODO
        return true;
    }

    @Override
    public boolean addToStrongBox(PhysicalResource resource) {
        //TODO
        return true;
    }

    @Override
    public boolean addToWarehouse(PhysicalResource resource) {
        //TODO
        return true;
    }

    @Override
    public boolean verifyResources(PhysicalResource physicalResource) {
        //TODO
        return true;
    }

    @Override
    public boolean verifyCard(CardType card) {
        //TODO
        return true;
    }


}
