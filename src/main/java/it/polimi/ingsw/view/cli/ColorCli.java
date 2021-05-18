package it.polimi.ingsw.view.cli;

public enum ColorCli
{
    //Color end string, color reset
    RESET("\033[0m"),
    CLEAR("\033[1;33m"), //original clear: "\033[H\033[2J"

    // Regular Colors. Normal color, no bold, background color etc.
    RED("\033[0;31m"),      // RED
    GREEN("\u001B[32m"),    // GREEN  "\u001B[32m"
    BLUE("\033[0;34m"),     // BLUE
    YELLOW("\u001B[33m"),
    PURPLE("\u001B[35m"),
    GREY("\u001B[36m"),
    WHITE("\u001B[37m"),


    // Bold
    YELLOW_BOLD("\033[1;33m"), // YELLOW
    GREY_BOLD("\033[1;90m");    // GREY


    private final String code;

    ColorCli(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

}
