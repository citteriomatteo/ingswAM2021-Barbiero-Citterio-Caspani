package it.polimi.ingsw.model.essentials.leader;

import it.polimi.ingsw.model.essentials.Requirable;
import it.polimi.ingsw.model.match.player.Verificator;
import it.polimi.ingsw.model.match.player.personalBoard.Effecter;

import java.util.List;
import java.util.Objects;

public class LeaderCard {
    private final List<Requirable> requirements;
    private final int winPoints;
    private final Effect effect;

    // GETTERS
    public List<Requirable> getRequirements() {
        return requirements;
    }
    public int getWinPoints() {
        return winPoints;
    }
    public Effect getEffect() {
        return effect;
    }


    public LeaderCard(List<Requirable> requirements, int winPoints, Effect effect) {
        this.requirements = requirements;
        this.winPoints = winPoints;
        this.effect = effect;
    }

    // Verify if the player has satisfied all the requirements
    public boolean isActivable(Verificator verificator){
        for (Requirable req : requirements){
            if(!req.verify(verificator))
                return false;
        }
        return true;
    }

    // Activate the precise effect and then add the leader to the ActiveLeader List
    // The player must have already called isActivable himself before
    public boolean activate (Effecter effecter){

        return effecter.addActiveLeader(this, effect.activate(effecter));
    }

    @Override
    public String toString() {
        return "LeaderCard{" +
                "\nrequirements=" + requirements +
                ",\nwinPoints=" + winPoints +
                ",\neffect=" + effect +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaderCard that = (LeaderCard) o;
        return getWinPoints() == that.getWinPoints() && Objects.equals(getRequirements(), that.getRequirements()) && Objects.equals(getEffect(), that.getEffect());
    }

}
