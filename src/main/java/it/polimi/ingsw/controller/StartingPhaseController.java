package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.exceptions.ShelfInsertException;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.WarehouseDecorator;


import java.util.*;

public class StartingPhaseController {
    private final Map<String, StateName> playerStates;
    private final Match match;
    private final Map<String, Card> cardMap;


    public StartingPhaseController(Match match,  Map<String, Card> cardMap) {
        this.match = match;
        this.cardMap = cardMap;
        playerStates = new HashMap<>();
        for (Player p : match.getPlayers())
            playerStates.put(p.getNickname(), StateName.WAITING_LEADERS);

    }

    public StateName getPlayerState(String nickname) {
        return playerStates.get(nickname);
    }

    public StateName leadersChoice(String nickname, List<String> leaders) throws RetryException {
        Player player = match.getPlayer(nickname);
        if(leaders.size() != 2 ||
                leaders.stream().filter((x)->!player.getHandLeaders().contains(cardMap.get(x))).count() != 0)
            throw new RetryException("Not valid leaders");
        else
        {
            List<LeaderCard> chosenLeaders = new ArrayList<>();
            for(String Id : leaders)
                chosenLeaders.add((LeaderCard) cardMap.get(Id));
            player.setHandLeaders(chosenLeaders);

            playerStates.replace(nickname,(match.getPlayers().indexOf(match.getPlayer(nickname))==0 ? StateName.STARTING_PHASE_DONE : StateName.WAITING_RESOURCES));
        }
        return playerStates.get(nickname);
    }

    public StateName startingResources(String nickname, List<PhysicalResource> resources) throws RetryException {
        Player player = match.getPlayer(nickname);

        int numResources = (match.getPlayers().indexOf(match.getPlayer(nickname))+1)/2;
        if(numResources != resources.size())
            throw new RetryException("Invalid resources quantity");
        else{
            Warehouse whBefore = WarehouseDecorator.clone(player.getPersonalBoard().getWarehouse());
            for (PhysicalResource r : resources) {
                PhysicalResource res = null;
                try {
                    res = new PhysicalResource(r.getType(),1);
                } catch (NegativeQuantityException e) { System.exit(1); }
                player.addToWarehouse(res);
                try {
                    player.moveIntoWarehouse(res, r.getQuantity());
                } catch (ShelfInsertException e) {
                    player.getPersonalBoard().setWarehouse(whBefore);
                    throw new RetryException("Invalid shelves choice");
                }

            }

        }
        playerStates.replace(nickname, StateName.STARTING_PHASE_DONE);
        return playerStates.get(nickname);
    }

    public void setState(String nickname, StateName state){
        playerStates.replace(nickname,state);
    }

    public boolean hasEnded(){
        for(StateName sn : playerStates.values()) {
            if(!sn.equals(StateName.STARTING_PHASE_DONE))
                return false;
        }
        return true;
    }
}
