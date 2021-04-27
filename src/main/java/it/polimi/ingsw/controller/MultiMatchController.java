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
                }
            }).start();
        }
        //TODO: check if all threads are in done state else wait
        return true;
    }


}
