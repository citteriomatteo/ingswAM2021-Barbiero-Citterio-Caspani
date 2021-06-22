package it.polimi.ingsw.view.CLI;

/**
 * This enumeration is used to simplify the use of color in command line interfaces, using ANSI codes.
 */
public enum ColorCli
{
    RESET("\033[0m"),
    CLEAR("\033[1;33m"),

    RED("\033[0;31m"),
    GREEN("\u001B[32m"),
    BLUE("\033[0;34m"),
    YELLOW("\u001B[33m"),
    PURPLE("\u001B[35m"),
    GREY("\u001B[36m"),
    WHITE("\u001B[97m"),

    YELLOW_BOLD("\033[1;33m"),
    GREY_BOLD("\033[1;90m");


    private final String code;

    /**
     * Constructor
     * @param code an ansi code
     */
    ColorCli(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    /**
     * Converts the content of an ANSI code avoiding special characters, such as [ or ] .
     * @return the regex string
     */
    public String toRegexString(){
        StringBuilder regex = new StringBuilder();
        for(char c : code.toCharArray()){
            if(c == '[' || c == ']')
                regex.append("\\");
            regex.append(c);
        }
        return regex.toString();
    }

    /**
     * Gets a string and returns it coloured.
     * @param str the string to colour
     * @return the coloured string
     */
    public String paint(String str){
        StringBuilder builder = new StringBuilder(this.code);
        builder.append(str).append(CLEAR.code);
        return builder.toString();
    }

}
