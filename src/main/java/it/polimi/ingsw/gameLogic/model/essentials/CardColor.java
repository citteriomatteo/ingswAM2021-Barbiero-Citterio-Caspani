package it.polimi.ingsw.gameLogic.model.essentials;

/**
 * This enumeration keeps all the available cards' colors.
 */
public enum CardColor {
    GREEN(1),    //ordinal: 0
    BLUE(2),     //1
    YELLOW(3),   //2
    PURPLE(4);    //3

    private final int val;
    CardColor(int v) { val = v; }

    /**
     * This method returns the position of the single value on the enumeration's list.
     * @return the numeric value of this
     */
    public int getVal() { return val; }
}
