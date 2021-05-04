package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.match.player.Verificator;

/**
 * this class represents one or more generic cards
 */

public class CardType implements Requirable{
    private final CardColor color;
    private final int level;
    private final int quantity;

    /**
     * Class constructor with the parameter quantity
     * @param color the color of this card
     * @param level the level of this card
     * @param quantity how many cards of the same type are contained in this object
     * @throws InvalidQuantityException if the parameter leve is not between 1 and 3
     */

    public CardType(CardColor color, int level, int quantity) throws InvalidQuantityException {
        this.color = color;
        if(level >= 0 && level <= 3)
            this.level = level;
        else
            throw new InvalidQuantityException("Invalid Level");
        this.quantity = quantity;
    }

    /**
     * Class constructor who set quantity to default 1
     * @param color the color of this card
     * @param level the level of this card
     * @throws InvalidQuantityException if the parameter leve is not between 1 and 3
     */

    public CardType(CardColor color, int level) throws InvalidQuantityException {
        this.color = color;
        if(level >= 0 && level <= 3)
            this.level = level;
        else
            throw new InvalidQuantityException("Invalid Level");
        this.quantity = 1;
    }

    /**
     * getter
     * @return the color of the CardType object
     */

    public CardColor getColor() {
        return color;
    }

    /**
     * getter
     * @return the level of the CardType object
     */

    public int getLevel() {
        return level;
    }

    /**
     * getter
     * @return the quantity of cards contained
     */

    public int getQuantity() {
        return quantity;
    }

    /**
     * The override of the equals method
     * @param obj the other CardType object to be compared
     * @return this and obj have the same color and the same level
     */

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CardType)) {
            return false;
        }

        CardType cardType = (CardType) obj;

        return this.color.equals(cardType.color) && this.level == cardType.level;
    }

    /**
     * Verify if the player has the right number of cards of this type
     * @param verificator the player's interface
     * @return true if he has them
     */

    @Override
    public boolean verify(Verificator verificator) {

        return verificator.verifyCard(this);
    }

    @Override
    public String toString() {
        return "CardType{" +
                "color=" + color +
                ", level=" + level +
                ", quantity=" + quantity +
                '}';
    }
}
