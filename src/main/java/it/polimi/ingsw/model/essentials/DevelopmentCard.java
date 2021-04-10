package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.match.player.Verificator;

import java.util.List;
import java.util.Objects;

public class DevelopmentCard {
    private final CardType type;
    private final List<PhysicalResource> price;
    private final Production production;
    private final int winPoints;

    //Simple constructor
    public DevelopmentCard(CardType type, List<PhysicalResource> price, Production production, int winPoints) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevelopmentCard that = (DevelopmentCard) o;
        return getWinPoints() == that.getWinPoints() && Objects.equals(getType(), that.getType()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getProduction(), that.getProduction());
    }


    @Override
    public String toString() {
        return "DevelopmentCard{" +
                "\ntype=" + type +
                ",\nprice=" + price +
                ",\nproduction=" + production +
                ",\nwinPoints=" + winPoints +
                "\n}";
    }
}
