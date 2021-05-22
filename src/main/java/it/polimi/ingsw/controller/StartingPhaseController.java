package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidQuantityException;
import it.polimi.ingsw.exceptions.NegativeQuantityException;
import it.polimi.ingsw.exceptions.RetryException;
import it.polimi.ingsw.exceptions.ShelfInsertException;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.Summary;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.match.player.personalBoard.warehouse.WarehouseDecorator;
import it.polimi.ingsw.network.message.stocmessage.HandLeadersStateMessage;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;
import it.polimi.ingsw.network.message.stocmessage.SummaryMessage;

import static it.polimi.ingsw.controller.MatchController.getKeyByValue;
import static it.polimi.ingsw.network.server.ServerUtilities.serverCall;


import java.util.*;
import java.util.stream.Collectors;

public class  StartingPhaseController {
    private final Map<String, StateName> playerStates;
    private final Match match;
    private final Map<String, Card> cardMap;


    public StartingPhaseController(Match match,  Map<String, Card> cardMap) {
        this.match = match;
        this.cardMap = cardMap;
        playerStates = new HashMap<>();

        //initializing the Summary and setting it to every player
        Summary summary = new Summary(match, cardMap, match.getMatchConfiguration().getCustomPath(), match.getMatchConfiguration().getBasicProduction());

        for (Player p : match.getPlayers()) {
            playerStates.put(p.getNickname(), StateName.WAITING_LEADERS);

            p.setSummary(summary);

            new SummaryMessage(p.getNickname(), summary).send(p.getNickname());
            //for the interested player
            new HandLeadersStateMessage(p.getNickname(), p.getHandLeaders().stream().map((x) -> getKeyByValue(cardMap, x)).collect(Collectors.toList())).send(p.getNickname());
            //masked, for the other players
            //new HandLeadersStateMessage(p.getNickname(), p.getHandLeaders().stream().map((x) -> "-1").collect(Collectors.toList())).sendBroadcast(this.match);
            changeState(p.getNickname(), StateName.WAITING_LEADERS);

        }
    }

    public StateName getPlayerState(String nickname) {
        return playerStates.get(nickname);
    }

    public StateName leadersChoice(String nickname, List<String> leaders) throws RetryException {
        Player player = match.getPlayer(nickname);
        if(leaders.size() != 2 || //todo: aggiungere controllo "se sono leader"
                leaders.stream().anyMatch((x) -> !player.getHandLeaders().contains(cardMap.get(x))))
            throw new RetryException("Not valid leaders");
        else
        {
            List<LeaderCard> chosenLeaders = new ArrayList<>();
            for(String Id : leaders)
                chosenLeaders.add((LeaderCard) cardMap.get(Id));
            player.setHandLeaders(chosenLeaders);

            if(match.getPlayers().indexOf(match.getPlayer(nickname))==0)
                changeState(nickname, StateName.STARTING_PHASE_DONE);
            else
                changeState(nickname, StateName.WAITING_RESOURCES);
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
                if(r == null)
                    throw new RetryException("Invalid resource asked");
                PhysicalResource res = null;
                try {
                    res = new PhysicalResource(r.getType(),1);
                } catch (NegativeQuantityException e) { System.exit(1); }
                player.addToWarehouse(res);
                try {
                    player.moveIntoWarehouse(res, r.getQuantity());
                } catch (ShelfInsertException | InvalidQuantityException e) {
                    player.getPersonalBoard().setWarehouse(whBefore);
                    throw new RetryException(e.getMessage());
                }

            }

        }

        //update_call
        player.updateWarehouse(nickname, player.getPersonalBoard().getWarehouse());
        player.updateMarketBuffer(nickname, player.getPersonalBoard().getWarehouse());

        changeState(nickname,StateName.STARTING_PHASE_DONE);

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

    private void changeState(String nickname, StateName newState){
        this.playerStates.replace(nickname, newState);
        match.getCurrentPlayer().getSummary().updateLastUsedState(nickname, newState);
    }

}
