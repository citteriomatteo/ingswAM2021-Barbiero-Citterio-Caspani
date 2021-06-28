package it.polimi.ingsw.gameLogic.model.match;

import it.polimi.ingsw.gameLogic.model.essentials.DevelopmentCard;
import it.polimi.ingsw.gameLogic.model.essentials.Production;
import it.polimi.ingsw.gameLogic.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.jsonUtilities.MyJsonParser;
import it.polimi.ingsw.jsonUtilities.Parser;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A configuration of the match all contained in one object
 */
public class MatchConfiguration
{
    private List<DevelopmentCard> allDevCards;
    private List<LeaderCard> allLeaderCards;
    private List<Cell> customPath;
    private Production basicProduction;

    /**
     * Simple constructor
     * @param allDevCards all the DevelopmentCards in the game
     * @param allLeaderCards all the LeaderCards in the game
     * @param customPath a list of cell that represents the custom FaithPath
     * @param basicProduction the custom basicProduction
     */
    public MatchConfiguration(List<DevelopmentCard> allDevCards, List<LeaderCard> allLeaderCards, List<Cell> customPath, Production basicProduction) {
        this.allDevCards = allDevCards;
        this.allLeaderCards = allLeaderCards;
        this.customPath = customPath;
        this.basicProduction = basicProduction;
    }

    /**
     * Default constructor, it builds an empty configuration, all the variables have to be passed via setter
     */
    public MatchConfiguration(){}

    //GETTER
    /**
     * @return all the DevelopmentCards in this configuration
     */
    public List<DevelopmentCard> getAllDevCards() {
        return allDevCards;
    }
    /**
     * @return all the LeaderCards in this configuration
     */
    public List<LeaderCard> getAllLeaderCards() {
        return allLeaderCards;
    }
    /**
     * Modified getter: returns a copy of the faith track (useful in order not to modify the real version, for rematches).
     * @return a list of cell that represents the custom FaithPath in this configuration
     */
    public List<Cell> getCustomPath() {
        return new ArrayList<>(customPath);
    }
    /**
     * @return the custom basicProduction of this configuration
     */
    public Production getBasicProduction() {
        return basicProduction;
    }

    //SETTER
    /**
     * @param allDevCards all the DevelopmentCards in this configuration
     */
    public void setAllDevCards(List<DevelopmentCard> allDevCards) {
        this.allDevCards = allDevCards;
    }
    /**
     * @param allLeaderCards all the LeaderCards in this configuration
     */
    public void setAllLeaderCards(List<LeaderCard> allLeaderCards) { this.allLeaderCards = allLeaderCards;}
    /**
     * @param customPath a list of cell that represents the custom FaithPath in this configuration
     */
    public void setCustomPath(List<Cell> customPath) {
        this.customPath = customPath;
    }
    /**
     * @param basicProduction the custom basicProduction of this configuration
     */
    public void setBasicProduction(Production basicProduction) {
        this.basicProduction = basicProduction;
    }

    // %%%%% UTILITY METHODS %%%%%

    /**
     * Function used to read the configuration from the json file at 'config' filePath
     * @param config the file path of the configuration file, relative to the directory "json/"
     * @return the object read in the json
     */
    public static MatchConfiguration assignConfiguration(String config){
        Parser parser = MyJsonParser.getParser();
        try {
            InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(MatchConfiguration.class.getResourceAsStream("/it/polimi/ingsw/json/" + config)));
            return parser.readMatchConfiguration(reader);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Application shutdown due to an internal error " );
            System.exit(1);
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MatchConfiguration)) return false;
        MatchConfiguration that = (MatchConfiguration) o;
        return Objects.equals(getAllDevCards(), that.getAllDevCards()) && Objects.equals(getAllLeaderCards(), that.getAllLeaderCards())
                && Objects.equals(getCustomPath(), that.getCustomPath()) && Objects.equals(getBasicProduction(), that.getBasicProduction());
    }

}