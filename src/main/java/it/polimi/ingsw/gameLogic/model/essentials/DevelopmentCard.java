package it.polimi.ingsw.gameLogic.model.essentials;

import it.polimi.ingsw.gameLogic.model.match.player.Verificator;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Class that represents a Development Card of the game
 */
public class DevelopmentCard implements Card, Comparator<DevelopmentCard> {
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
     * Returns false to show that it's a devCard
     * @return false
     */
    @Override
    public boolean isLeader(){
        return false;
    }

    /**
     * Verifies if the player has the required resources in his strongBox or Warehouse to buy this
     * @param verificator the player who wants to buy this
     * @return true if the player have the required resources in his strongBox or Warehouse
     */
    public boolean isBuyable(Verificator verificator){
        List<PhysicalResource> discountedPrice = verificator.getDiscountedCosts(price);

        for (PhysicalResource res : discountedPrice){
            if(!verificator.verifyResources(res))
                return false;
        }
        return true;
    }

    /**
     * Compare two development cards, the first is lesser if its level is lesser or if the level of the two cards is the same
     * but che color of the first comes first in the CardColor order.
     * @param devCard1 the first development card to compare
     * @param devCard2 the second development card to compare
     * @return -1 if the first card is less then the second in the comparator's logic,
     * 0 if the two cards are equals in that logic and 1 if the first card is greater then the second in the comparator's logic
     */
    @Override
    public int compare(DevelopmentCard devCard1, DevelopmentCard devCard2) {
        if(devCard1.getType().getLevel() < devCard2.getType().getLevel() ||
                (devCard1.getType().getLevel() == devCard2.getType().getLevel() && devCard1.getType().getColor().getVal() < devCard2.getType().getColor().getVal()))
            return -1;
        else if (devCard1.getType().equals(devCard2.getType()))
            return 0;
        else
            return 1;
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
