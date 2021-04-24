package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.network.message.Topic;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerUtilities {
    //I use a vector because it implements add and remove like atomic actions
    private static final List<PlayersHandler> activeClients = new Vector<>();
    private static final Map<Match, Topic> communication = new ConcurrentHashMap<>();

    static boolean addNewPlayer(PlayersHandler client){
        return activeClients.add(client);
    }
    static boolean removePlayer(PlayersHandler removedClient){
        return activeClients.remove(removedClient);
    }

    /**
     * Creates a new topic for communication linked to a match, if a topic linked to this match already exists,
     * all the previous messages in the topic will be lost and this method will return false
     * @param match the match you want to link the new topic to
     * @return true if a topic linked to the given match doesn't already exist
     */
    static boolean createNewTopic(Match match){
        return communication.put(match, new Topic()) != null;
    }



}
