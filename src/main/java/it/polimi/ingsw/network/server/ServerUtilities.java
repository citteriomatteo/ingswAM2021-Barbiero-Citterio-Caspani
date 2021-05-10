package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.match.player.Player;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Static class fundamental for synchronizing the threads that control the clients communication and the game logic
 */
public class ServerUtilities {
    private static final Map<String, PlayerHandler> activeClients = new ConcurrentHashMap<>();
    private static BlockingQueue<Player> pendentMatchWaiting = null;

    /**
     * Adds a new player to the global list of active Players,
     * this list can be useful for searching existing nicknames or other things
     * @param client the new client to add in the global list
     * @return true if there wasn't a previous player with the same nickname in this list
     *         false if the previous player has been substituted by the new one
     */
    public static boolean addNewPlayer(PlayerHandler client){
        return activeClients.put(client.getPlayer().getNickname(), client) != null;
    }

    /**
     * Removes a player from the global list of active Players,
     * this list can be useful for searching existing nicknames or other things
     * @param removedClient the client to remove from the global list
     * @return true if the PlayerHandler was previously in the global list
     *         false if there are no such PlayerHandler in the global list
     */
    public static boolean removePlayer(PlayerHandler removedClient){
        return activeClients.remove(removedClient.getPlayer().getNickname()) != null;
    }

    public static ControlBase findControlBase(String nickname){
        return activeClients.get(nickname);
    }

    //%%%%%%%%%%%%%%% MULTIPLAYER PART %%%%%%%%%%%%%

    /**
     * Tries to create a new waiting list for players to join a new match, if indeed exists another waiting list,
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
     * Waits until the waiting queue is full and then returns the list of players who joins the match
     * @return the list of players who joins the match
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
                System.exit(1);
            }
        }
        //when the queue is full then copy the object on a temporary pointer
        // in order to call the notify after the pendentMatchWaiting is resettled to null
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
                     System.exit(1);
                 }
             }
             return true;
         }
         return false;
    }


//    //!!!!!!!!!!!!!TODO: REMOVE ME!!!!!!!!!!!!!!!!!!!
//    //only for test purpose
//    private static void sendAMessageWithDelay(int delay, Match match) {
//
//        Timer t = new Timer();
//        TimerTask tt = new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("Sending a StoC Message in the first match");
//                StoCMessage msg = new TokenDrawMessage("giorgio", "yellow token");
//     //           msg.send(match);
//            }
//        };
//        System.out.println("sending a message in " + delay/1000 + " seconds");
//        t.schedule(tt, delay);
//    }
//    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
}
