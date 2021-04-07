package it.polimi.ingsw.model.essentials;

import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.match.player.Verificator;

public class CardType implements Requirable{
    private final CardColor color;
    private final int level;
    private final int quantity;

    public CardType(CardColor color, int level, int quantity) throws InvalidQuantityException {
        this.color = color;
        if(level >= 0 && level < 3)
            this.level = level;
        else
            throw new InvalidQuantityException("Invalid Level");
        this.quantity = quantity;
    }

    public CardColor getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CardType)) {
            return false;
        }

        CardType cardType = (CardType) obj;

        if(this.color.equals(cardType.color) && this.level == cardType.level)
            return true;
        else
            return false;
    }

    @Override
    public boolean verify(Verificator verificator) {

        return verificator.verifyCard(this);
    }
}
