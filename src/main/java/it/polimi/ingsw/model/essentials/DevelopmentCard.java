package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.match.player.Verificator;

import java.util.List;

public class DevelopmentCard {
    private final List<PhysicalResource> price;
    private final CardType type;
    private final int winPoints;
    private final Production production;

    //Simple constructor
    public DevelopmentCard(List<PhysicalResource> price, CardType type, int winPoints, Production production) {
        this.price = price;
        this.type = type;
        this.winPoints = winPoints;
        this.production = production;
    }

    public List<PhysicalResource> getPrice() {
        return price;
    }

    public CardType getType() {
        return type;
    }

    public int getWinPoints() {
        return winPoints;
    }

    public Production getProduction() {
        return production;
    }

    //Returns true if the player have the required resources in his strongBox or Warehouse
    public boolean isBuyable(Verificator verificator){
        for (PhysicalResource res : price){
            if(!verificator.verifyResources(res))
                return false;
        }
        return true;
    }
}
