package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.essentials.DevelopmentCard;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;

import java.util.List;

public class MatchConfiguration
{
    private List<DevelopmentCard> allDevCards;
    private List<LeaderCard> allLeaderCards;
    private List<Cell> customPath;
    private Production basicProduction;

    public MatchConfiguration(List<DevelopmentCard> allDevCards, List<LeaderCard> allLeaderCards, List<Cell> customPath, Production basicProduction) {
        this.allDevCards = allDevCards;
        this.allLeaderCards = allLeaderCards;
        this.customPath = customPath;
        this.basicProduction = basicProduction;
    }

    public List<DevelopmentCard> getAllDevCards() {
        return allDevCards;
    }
    public List<LeaderCard> getAllLeaderCards() {
        return allLeaderCards;
    }
    public List<Cell> getCustomPath() {
        return customPath;
    }
    public Production getBasicProduction() {
        return basicProduction;
    }

    public void setAllDevCards(List<DevelopmentCard> allDevCards) {
        this.allDevCards = allDevCards;
    }
    public void setAllLeaderCards(List<LeaderCard> allLeaderCards) { this.allLeaderCards = allLeaderCards;}
    public void setCustomPath(List<Cell> customPath) {
        this.customPath = customPath;
    }
    public void setBasicProduction(Production basicProduction) {
        this.basicProduction = basicProduction;
    }
}