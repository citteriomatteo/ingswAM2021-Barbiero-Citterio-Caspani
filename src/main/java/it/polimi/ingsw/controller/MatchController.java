package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.Production;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.MatchConfiguration;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.SingleMatch;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.stocmessage.EndGameResultsMessage;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchController {
    private Match match;
    private TurnController turnController;
    private StartingPhaseController startingPhaseController;
    private RematchPhaseController rematchPhaseController;
    private final Map<StateName, List<CtoSMessageType>> acceptedMessagesMap;
    private final Map<String, Card> cardMap;


    public MatchController(List<Player> playersInMatch, MatchConfiguration configuration) throws RetryException {
        acceptedMessagesMap = new HashMap<>();
        for(StateName sn : StateName.values())
            acceptedMessagesMap.put(sn, acceptedMessages(sn));

        this.cardMap = new HashMap<>();
        for (int i=1; i<=configuration.getAllDevCards().size(); i++)
            cardMap.put("D"+i,configuration.getAllDevCards().get(i-1));
        for (int i=1; i<=configuration.getAllLeaderCards().size(); i++)
            cardMap.put("L"+i,configuration.getAllLeaderCards().get(i-1));


        startingPhaseController = new StartingPhaseController(match, cardMap);
        turnController = null;
        rematchPhaseController = null;

        try {
            this.match = new MultiMatch(playersInMatch, configuration);
        } catch (SingleMatchException e) {
            System.err.println("internal error");
            System.exit(1);
        } catch (WrongSettingException e) {
            throw new RetryException("Wrong configuration");
        }

    }

    public MatchController(Player player, MatchConfiguration configuration) throws RetryException {
        acceptedMessagesMap = new HashMap<>();
        for(StateName sn : StateName.values())
            acceptedMessagesMap.put(sn, acceptedMessages(sn));

        this.cardMap = new HashMap<>();
        for (int i=1; i<=configuration.getAllDevCards().size(); i++)
            cardMap.put("D"+i,configuration.getAllDevCards().get(i-1));
        for (int i=1; i<=configuration.getAllLeaderCards().size(); i++)
            cardMap.put("L"+i,configuration.getAllLeaderCards().get(i-1));


        startingPhaseController = new StartingPhaseController(match, cardMap);
        turnController = null;
        rematchPhaseController = null;

        try {
            this.match = new SingleMatch(player, configuration);
        } catch (WrongSettingException e) {
            throw new RetryException("Wrong configuration");

        }

    }

    private List<CtoSMessageType> acceptedMessages(StateName sn){
        List<CtoSMessageType> accepted = new ArrayList<>();
        accepted.add(CtoSMessageType.SWITCH_SHELF);
        switch (sn)
        {
            //STARTING PHASE STATES
            case WAITING_LEADERS:
                accepted.remove(CtoSMessageType.SWITCH_SHELF);
                accepted.add(CtoSMessageType.LEADERS_CHOICE);
                break;
            case WAITING_RESOURCES:
                accepted.remove(CtoSMessageType.SWITCH_SHELF);
                accepted.add(CtoSMessageType.STARTING_RESOURCES);
                break;
            case STARTING_PHASE_DONE:
                accepted.remove(CtoSMessageType.SWITCH_SHELF);
                break;

            //TURN STATES
            case STARTING_TURN:
                accepted.add(CtoSMessageType.LEADER_ACTIVATION);
                accepted.add(CtoSMessageType.LEADER_DISCARDING);
                accepted.add(CtoSMessageType.MARKET_DRAW);
                accepted.add(CtoSMessageType.DEV_CARD_DRAW);
                accepted.add(CtoSMessageType.PRODUCTION);
                break;

            case MARKET_ACTION:
                accepted.add(CtoSMessageType.WHITE_MARBLE_CONVERSIONS);
                break;

            case RESOURCES_PLACEMENT:
                accepted.add(CtoSMessageType.WAREHOUSE_INSERTION);
                break;

            case BUY_DEV_ACTION:
                accepted.add(CtoSMessageType.PAYMENTS);
                break;

            case PLACE_DEV_CARD:
                accepted.add(CtoSMessageType.DEV_CARD_PLACEMENT);
                break;

            case PRODUCTION_ACTION:
                accepted.add(CtoSMessageType.PAYMENTS);
                break;

            case END_TURN:
                accepted.add(CtoSMessageType.LEADER_ACTIVATION);
                accepted.add(CtoSMessageType.LEADER_DISCARDING);
                accepted.add(CtoSMessageType.END_TURN);
                break;

            case END_MATCH:
                accepted.add(CtoSMessageType.REMATCH);
                accepted.add(CtoSMessageType.DISCONNECTION);
                accepted.remove(CtoSMessageType.SWITCH_SHELF);
                break;

            case REMATCH_OFFER:
                accepted.add(CtoSMessageType.REMATCH);
                accepted.remove(CtoSMessageType.SWITCH_SHELF);
                break;

            default:
                accepted.remove(CtoSMessageType.SWITCH_SHELF);
                break;

        }
        return accepted;
    }


    private boolean isComputable(String nickname, CtoSMessageType type) throws RetryException {
        if (turnController == null) {
            if (!acceptedMessagesMap.get(startingPhaseController.getPlayerState(nickname)).contains(type))
                throw new RetryException("This message is not accepted in this phase");
        } else if (rematchPhaseController == null) {
            if (!nickname.equals(turnController.getCurrentPlayer().getNickname()))
                return false;
            else if (!acceptedMessagesMap.get(turnController.getCurrentState()).contains(type))
                throw new RetryException("This message is not accepted in this phase");
        } else if (!type.equals(CtoSMessageType.REMATCH))
            throw new RetryException("This message is not accepted in this phase");


        return true;
    }

    public synchronized boolean startingLeader(String nickname, List<String> leaders) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.LEADERS_CHOICE);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, startingPhaseController.leadersChoice(nickname, leaders));
        //TODO: send it to the player
        if (startingPhaseController.hasEnded())
            turnController = new TurnController(match, cardMap);
        return true;

    }

    public synchronized boolean startingResources(String nickname, List<PhysicalResource> resources) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.STARTING_RESOURCES);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, startingPhaseController.startingResources(nickname, resources));
        //TODO: send it to the player
        if (startingPhaseController.hasEnded())
            turnController = new TurnController(match, cardMap);
        return true;
    }

    public synchronized boolean nextTurn(String nickname) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.END_TURN);
        if (!isComputable)
            return false;

        try {
            turnController.nextTurn();
            NextStateMessage message = new NextStateMessage(turnController.getCurrentPlayer().getNickname(), StateName.STARTING_TURN);
            //TODO: send it in broadcast
        } catch (MatchEndedException e) {
            rematchPhaseController = new RematchPhaseController(match.getPlayers());
            EndGameResultsMessage message = new EndGameResultsMessage(nickname, e.getMsg(), e.getRanking());
            //TODO: send it in broadcast
        }

        return true;
    }

    public synchronized boolean switchShelf(String nickname, int shelf1, int shelf2) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.SWITCH_SHELF);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, turnController.switchShelf(shelf1, shelf2));
        //TODO: send it to the player
        return true;

    }

    public synchronized boolean leaderActivation(String nickname, String leaderId) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.LEADER_ACTIVATION);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, turnController.leaderActivation(leaderId));
        //TODO: send it to the player

        return true;
    }

    public synchronized boolean leaderDiscarding(String nickname, String leaderId) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.LEADER_DISCARDING);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, turnController.leaderDiscarding(leaderId));
        //TODO: send it to the player

        return true;
    }

    public synchronized boolean marketDraw(String nickname, boolean row, int num) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.MARKET_DRAW);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, turnController.marketDraw(row, num));
        //TODO: send it to the player

        return true;
    }

    public synchronized boolean whiteMarblesConversion(String nickname, List<PhysicalResource> resources) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.WHITE_MARBLE_CONVERSIONS);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, turnController.whiteMarblesConversion(resources));
        //TODO: send it to the player

        return true;
    }

    public synchronized boolean warehouseInsertion(String nickname, List<PhysicalResource> resources) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.WAREHOUSE_INSERTION);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, turnController.warehouseInsertion(resources));
        //TODO: send it to the player

        return true;
    }

    public synchronized boolean devCardDraw(String nickname, int row, int column) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.DEV_CARD_DRAW);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, turnController.devCardDraw(row, column));
        //TODO: send it to the player

        return true;
    }

    public synchronized boolean payments(String nickname, List<PhysicalResource> strongboxCosts, Map<Integer, PhysicalResource> warehouseCosts) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.PAYMENTS);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, turnController.payments(strongboxCosts, warehouseCosts));
        //TODO: send it to the player

        return true;
    }

    public synchronized boolean devCardPlacement(String nickname, int column) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.DEV_CARD_PLACEMENT);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, turnController.devCardPlacement(column));
        //TODO: send it to the player

        return true;
    }
    public synchronized boolean production(String nickname, List<String> cardIds, Production productionOfUnknown) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.PRODUCTION);
        if (!isComputable)
            return false;

        NextStateMessage message = new NextStateMessage(nickname, turnController.production(cardIds, productionOfUnknown));
        //TODO: send it to the player
        return true;
    }

    public synchronized boolean response(String nickname, boolean value) throws RetryException {
        boolean isComputable = isComputable(nickname, CtoSMessageType.PRODUCTION);
        if (!isComputable)
            return false;
        rematchPhaseController.response(nickname, value);
        return true;
    }
}
