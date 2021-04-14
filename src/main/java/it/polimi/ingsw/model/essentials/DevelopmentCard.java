package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.match.player.Verificator;

import java.util.List;
import java.util.Objects;

public class DevelopmentCard {
    private final CardType type;
    private final List<PhysicalResource> price;
    private final Production production;
    private final int winPoints;

    /**
     * Simple constructor
     * @param type the card type of this
     * @param price the physical resources the player ought to pay in order to buy this from the grid
     * @param production the production associated to this card
     * @param winPoints the points the player get at the end of the game if he places this on his personal board
     */
    public DevelopmentCard(CardType type, List<PhysicalResource> price, Production production, int winPoints) {
        this.price = price;
        this.type = type;
        this.winPoints = winPoints;
        this.production = production;
    }

    //ALL GETTERS

    /**
     * Getter
     * @return the price of this
     */
    public List<PhysicalResource> getPrice() {
        return price;
    }
    /**
     * Getter
     * @return the card type of this
     */
    public CardType getType() {
        return type;
    }
    /**
     * Getter
     * @return the win points of this
     */
    public int getWinPoints() {
        return winPoints;
    }
    /**
     * Getter
     * @return the production of this
     */
    public Production getProduction() {
        return production;
    }

    /**
     * Verifies if the player has the required resources in his strongBox or Warehouse to buy this
     * @param verificator the player who wants to buy this
     * @return true if the player have the required resources in his strongBox or Warehouse
     */
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
