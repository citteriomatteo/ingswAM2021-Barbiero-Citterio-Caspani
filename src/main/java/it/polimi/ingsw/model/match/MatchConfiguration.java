package it.polimi.ingsw.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;
import static it.polimi.ingsw.gsonUtilities.GsonHandler.effectConfig;

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
     * @return a list of cell that represents the custom FaithPath in this configuration
     */
    public List<Cell> getCustomPath() {
        return customPath;
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

    //Returns the configuration at the path "config".
    /**
     * Function used to read the configuration from the json file at 'config' filePath
     * @param config the file path of the configuration file
     * @return the object read in the json
     */
    public static MatchConfiguration assignConfiguration(String config){
        Gson g = cellConfig(resourceConfig(requirableConfig(effectConfig(new GsonBuilder())))).setPrettyPrinting().create();
        try {
            FileReader reader = new FileReader(config);
            return g.fromJson(reader, MatchConfiguration.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Application shutdown due to an internal error " );
            System.exit(1);
            return null;
        }
    }
}