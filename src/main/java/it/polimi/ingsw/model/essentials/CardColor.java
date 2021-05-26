package it.polimi.ingsw.model.essentials;

//TODO JAVADOC
public enum CardColor {
    GREEN(1),    //ordinal: 0
    BLUE(2),     //1
    YELLOW(3),   //2
    PURPLE(4);    //3

    private final int val;
    CardColor(int v) { val = v; }

    //TODO JAVADOC
    public int getVal() { return val; }
}
