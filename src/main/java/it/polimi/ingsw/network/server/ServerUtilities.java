package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.network.message.Topic;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static class fundamental for synchronizing the threads that control the clients communication and the game logic
 */
public class ServerUtilities {
    private static final Map<Player, PlayersHandler> activeClients = new ConcurrentHashMap<>();
    private static final Map<Match, Topic> communication = new ConcurrentHashMap<>();
    private static BlockingQueue<Player> pendentMatchWaiting = null;

    /**
     * Adds a new player to the global list of active Players,
     * this list can be useful for searching existing nicknames or other things
     * @param client the new client to add in the global list
     * @return true if there wasn't a previous player with the same nickname in this list
     *         false if the previous player has been substituted by the new one
     */
    public static boolean addNewPlayer(PlayersHandler client){
        return activeClients.put(client.getPlayer(), client) != null;
    }

    /**
     * Removes a player from the global list of active Players,
     * this list can be useful for searching existing nicknames or other things
     * @param removedClient the client to remove from the global list
     * @return true if the PlayersHandler was previously in the global list
     *         false if there are no such PlayersHandler in the global list
     */
    public static boolean removePlayer(PlayersHandler removedClient){
        return activeClients.remove(removedClient.getPlayer()) != null;
    }


    /**
     * Try to create a new waiting list for players to join a new match, if indeed exists another waiting list,
     * wait until the previous match is completed and then ask for the new one
     * @param submitter the player who wants to create the new match
     * @param numPlayers the number of player who have to participate in the new match
     * @return true
     */
    public static boolean searchingForPlayers(Player submitter, int numPlayers) {
         while(isThereAPendentMatch()) {
             try {
                 synchronized (pendentMatchWaiting) {
                     pendentMatchWaiting.wait();
                 }
             } catch (InterruptedException e) {
                 Thread.currentThread().interrupt();  // set interrupt flag
                 System.out.println("Something goes wrong while waiting for start a new match");
             }
         }
         ArrayBlockingQueue<Player> queue = new ArrayBlockingQueue<>(numPlayers);
         queue.add(submitter);
         pendentMatchWaiting = queue;

         return true;
    }

    /**
     * Waits until the waiting queue is full and then return the list of player who joins the match
     * @return the list of player who joins the match
     */
    public static List<Player> matchParticipants(){
        //if there aren't enough players, wait until the queue is full
        while(pendentMatchWaiting.remainingCapacity()!=0) {
            try {
                synchronized (pendentMatchWaiting) {
                    pendentMatchWaiting.wait();
                    System.out.println("Thread in matchParticipants is awaken");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // set interrupt flag
                System.out.println("Something goes wrong while waiting for players joining the new match");
            }
        }
        //when the queue is full then copy the object on a temporary pointer in order to call the notify after the pendentMatchWaiting is resettled to null
        List<Player> res = new ArrayList<>();
        BlockingQueue<Player> tempQueue = pendentMatchWaiting;
        pendentMatchWaiting = null;
        //this notifyAll() is useful for thread waiting for build their own match
        synchronized (tempQueue) {
            tempQueue.notifyAll();
        }
        tempQueue.drainTo(res);

        return res;
    }

    /**
     * Returns true if someone is already searching for players in his match.
     * if there is a pendent match no one can ask for opponents until the previous request is not filled.
     * @return true if someone is already searching for players in his match
     */
    public static boolean isThereAPendentMatch(){
        return pendentMatchWaiting != null;
    }

    /**
     * Adds the player to a waiting list for participating at the match that is forming.
     * Wait until the waiting list is filled.
     * @param player the player to insert in the match
     * @return true if the player is inserted in the match
     *         false if there are no more places for the current match
     */
    public static boolean participateToCurrentMatch(Player player){
         if(pendentMatchWaiting.offer(player)) {
             if (pendentMatchWaiting.remainingCapacity() == 0) {
                 System.out.println("last player joined the match -> awake other threads");
                 synchronized (pendentMatchWaiting) {
                     pendentMatchWaiting.notifyAll();
                 }
             }
             else {
                 try {
                     synchronized (pendentMatchWaiting) {
                         pendentMatchWaiting.wait();
                     }
                 } catch (InterruptedException e) {
                     Thread.currentThread().interrupt();  // set interrupt flag
                     System.out.println("Something goes wrong while waiting other players to start a new match");
                 }
             }
             return true;
         }
         return false;
    }







    //%%%%%%%%%% communication part %%%%%%%%%%%%%

    /**
     * Creates a new topic for communication linked to a match, if a topic linked to this match already exists,
     * all the previous messages in the topic will be lost and this method will return false
     * @param match the match you want to link the new topic to
     * @return true if a topic linked to the given match doesn't already exist
     */
    public static boolean createNewTopic(Match match){
        return communication.put(match, new Topic()) != null;
    }

}
