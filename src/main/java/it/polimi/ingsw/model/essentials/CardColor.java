package it.polimi.ingsw.model.essentials;

public enum CardColor {
    GREEN(1),    //ordinal: 0
    BLUE(2),     //1
    YELLOW(3),   //2
    PURPLE(4);    //3

    private final int val;
    CardColor(int v) { val = v; }
    public int getVal() { return val; }
}
