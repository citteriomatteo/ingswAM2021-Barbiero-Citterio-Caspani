package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.match.MatchConfiguration;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.Topic;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessageType;
import it.polimi.ingsw.network.message.ctosmessage.LeadersChoiceMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.polimi.ingsw.model.exceptions.SingleMatchException;
import it.polimi.ingsw.model.exceptions.WrongSettingException;
import it.polimi.ingsw.model.match.MultiMatch;
import it.polimi.ingsw.model.match.player.Player;

import java.util.List;

import static it.polimi.ingsw.network.server.ServerUtilities.createNewTopic;

public class MultiMatchController extends Thread implements MatchController {
    private MultiMatch match;

    public MultiMatchController(List<Player> playersInMatch) throws SingleMatchException {
        try {
            match = new MultiMatch(playersInMatch, "src/test/resources/StandardConfiguration.json");
            createNewTopic(match);
            System.out.println("created the multiplayer match");
        }
         catch (WrongSettingException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {


    }
//    private final MatchConfiguration configuration;
//    private MultiMatch match;
//    private final Topic topic;
//    private StateName currentState;
//    private boolean lastRound;
//    private final Player firstPlayer;
//    private Player currentPlayer;
//    private final Map<String, Card> cardMap;
//    private final TurnController turnController;
//
//    public MultiMatchController(MatchConfiguration configuration, MultiMatch match, Topic topic ) {
//        this.configuration = configuration;
//        this.topic = topic;
//        this.match = match;
//        this.firstPlayer = match.getCurrentPlayer();
//        this.currentPlayer = match.getCurrentPlayer();
//        this.lastRound = false;
//        this.currentState = StateName.WAITING_LEADERS;
//        this.cardMap = new HashMap<>();
//        for (int i=1; i<=configuration.getAllDevCards().size(); i++)
//            cardMap.put("D"+i,configuration.getAllDevCards().get(i-1));
//        for (int i=1; i<=configuration.getAllLeaderCards().size(); i++)
//            cardMap.put("L"+i,configuration.getAllLeaderCards().get(i-1));
//
//        this.turnController = new TurnController(firstPlayer,match,cardMap);
//
//        startingPhase();
//    }
//
//    private boolean startingPhase(){
//        List<StateName> playersState = new ArrayList<>();
//        for(Player player : match.getPlayers()){
//            playersState.add(StateName.WAITING_LEADERS);
//            new Thread(()->{
//                int i = match.getPlayers().indexOf(player);
//                while(!playersState.get(i).equals(StateName.STARTING_PHASE_DONE)){
//                    synchronized (topic.inMessages){
//
//                        Message inMessage = topic.inMessages.peek();
//                        if(!inMessage.getNickname().equals(player.getNickname())) {
//                            try {
//                                topic.inMessages.wait();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                                //TODO:
//                            }
//                        }
//                        else {
//                            inMessage = topic.inMessages.poll();
//                            switch (playersState.get(i)){
//                                case WAITING_LEADERS:
//                                    if(!inMessage.getType().equals(CtoSMessageType.LEADERS_CHOICE)){
//                                        //TODO: create a retry message
//                                    }
//                                    else{
//                                        LeadersChoiceMessage message = (LeadersChoiceMessage) inMessage;
//                                        if(message.getLeaders().size() != 2 ||
//                                                message.getLeaders().stream().filter((x)->!player.getHandLeaders().contains(cardMap.get(x))).count() != 0){
//                                            //TODO: create a retry message
//                                        }
//                                        else
//                                        {
//                                            List<LeaderCard> leaders = new ArrayList<>();
//                                            for(String Id : message.getLeaders())
//                                                leaders.add((LeaderCard) cardMap.get(Id));
//                                            player.setHandLeaders(leaders);
//
//                                            playersState.set(i,(i==0 ? StateName.STARTING_PHASE_DONE : StateName.WAITING_RESOURCES));
//                                        }
//
//                                    }
//
//                            }
//
//                        }
//
//                    }
//                }
//            }).start();
//        }
//        //TODO: check if all threads are in done state else wait
//        return true;
//    }
//

}
