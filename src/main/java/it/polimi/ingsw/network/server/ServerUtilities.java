package it.polimi.ingsw.network.server;

import it.polimi.ingsw.exceptions.ReconnectionException;
import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.match.player.Player;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Eager singleton class fundamental for synchronizing the threads that control the clients communication and the game logic
 */
public class ServerUtilities {
    private static final ServerUtilities instance = new ServerUtilities();
    private final Map<String, PlayerHandler> activeClients;
    private BlockingQueue<Player> pendentMatchWaiting;
    private final AtomicBoolean formingMatch;
    // Is important to save the creator of the current match for disconnection issues, when a player disconnects in the first phase ->
    // control if he is creating a match, in that case leave the possibility to the next one
    private Player host;
    private final Queue<Player> waitingPlayers;

    private ServerUtilities() {
        activeClients = new ConcurrentHashMap<>();
        pendentMatchWaiting = null;
        formingMatch = new AtomicBoolean(false);
        waitingPlayers = new LinkedList<>();
    }

    public static ServerUtilities serverCall() {
        return instance;
    }

    /**
     * Adds a new player to the global list of active Players,
     * this list can be useful for searching existing nicknames or other things
     * @param nickname the nickname of the player you want to add to the global list
     * @param client the PlayerHandler related to the new client to add in the global list
     * @return true if there wasn't a previous player with the same nickname in this list
     *         false if there is already a player with that name in the list and is connected
     * @throws ReconnectionException if there is already a player with that name in the list but he is currently disconnected
     */
    public boolean addNewPlayer(String nickname, PlayerHandler client) throws ReconnectionException {
        synchronized (activeClients) {
            if (activeClients.containsKey(nickname)) {
                if (activeClients.get(nickname).getPlayer().isConnected())
                    return false;
                throw new ReconnectionException("This nickname corresponds to a previously disconnected player");
            }
            activeClients.put(nickname, client);
            return true;
        }
    }

    /**
     * Removes a player from the global list of active Players,
     * this list can be useful for searching existing nicknames or other things
     * @param removedClient the client to remove from the global list
     * @return true if the PlayerHandler was previously in the global list
     *         false if there are no such PlayerHandler in the global list
     */
    public boolean removePlayer(PlayerHandler removedClient){
        synchronized (activeClients) {
            return activeClients.remove(removedClient.getPlayer().getNickname()) != null;
        }
    }

    public ControlBase findControlBase(String nickname){
        return activeClients.get(nickname);
    }

    //%%%%%%%%%%%%%%% MULTIPLAYER PART %%%%%%%%%%%%%

    /**
     * If returns true, the player host has reserved the right for creating the next match.
     * Returns false if someone is already searching for players in his match, in that case try to add the player to the current match and set the client in waiting.
     * If there is a pendent match no one can ask for opponents until the previous request is not filled.
     * @return false if someone is already searching for players in his match, you can't form your own
     *         true if you can create your own match, and so reserve the place, you will be the next who can ask for players
     */
    public boolean canCreateMatch(Player host){
        synchronized (formingMatch){
            if(formingMatch.get()) {
                if (!participateToCurrentMatch(host)) {
                    System.out.println("Added "+ host + " to the waiting list");
                    waitingPlayers.add(host);
                    findControlBase(host.getNickname()).getInitController().setWaiting();
                }
                return false;
            }
            System.out.println("The next match will be created by "+ host);
            this.host = host;
            formingMatch.set(true);
            return true;
        }
    }

    /**
     * Create a new waiting list for players to join a new match, the submitter has to be inserted inside a
     * {@link ServerUtilities#canCreateMatch(Player)} before and the method should had returned true
     * @param submitter the player who wants to create the new match
     * @param numPlayers the number of players who have to participate in the new match
     */
    public void searchingForPlayers(Player submitter, int numPlayers) {
        synchronized (formingMatch) {
            if(!submitter.equals(host)){
                System.out.println("A player without authorization has tried to create a match");
                System.exit(1);
            }

            pendentMatchWaiting = new ArrayBlockingQueue<>(numPlayers);
            pendentMatchWaiting.add(submitter);

            searchingPlayersInWaitingList(numPlayers-1);
        }
    }

    private void searchingPlayersInWaitingList(int numPlayers){
        Player p;
        for (int i = 0; i < Math.min(numPlayers, waitingPlayers.size()); i++) {
            System.out.println("adding player " + (i+2) + " to the forming match from the waiting list");
            p = waitingPlayers.peek();
            if(participateToCurrentMatch(p))
                waitingPlayers.remove();
        }
        if(nextHostFromWaitingList())
            System.out.println("The new host is set --> " + host);
    }

    /**
     * Takes the next player in the waiting list and try to give him the possibility to create the next match
     * @return true if there is almost a player inside the waiting list, and the first became the next host
     */
    private boolean nextHostFromWaitingList(){
        Player p = waitingPlayers.peek();
        if(p == null)
            return false;
        if (canCreateMatch(p)){
            waitingPlayers.remove();
            findControlBase(p.getNickname()).getInitController().createMatch();
            return true;
        }
        return false;
    }

    /**
     * Returns the list of players who joins the match and empty the waiting queue, now someone else can create is own match.
     * This method is automatically called when a waiting list is full and so doesn't need to wait for filling the queue
     * @return the list of players who joins the match
     */
    public List<Player> matchParticipants(){
        synchronized (formingMatch) {
            if (pendentMatchWaiting.remainingCapacity() != 0) {
                System.out.println("Error inside the server, tried to form a match when there aren't enough players in list");
                System.exit(1);
            }

            List<Player> res = new ArrayList<>(pendentMatchWaiting);
            //finished to prepare this match -> someone else can do his own
            formingMatch.set(false);

            return res;
        }
    }



    /**
     * Adds the player to a waiting list for participating at the match that is forming.
     * When the waiting list is filled, the players in the list can start the game
     * @param player the player to insert in the match
     * @return true if the player is inserted in the match
     *         false if there are no more places for the current match
     */
    private boolean participateToCurrentMatch(Player player){
        synchronized (formingMatch) {
            if (pendentMatchWaiting != null && pendentMatchWaiting.offer(player)) {
                // the player entered the queue
                findControlBase(player.getNickname()).getInitController().setWaiting();
                if (pendentMatchWaiting.remainingCapacity() == 0) {
                    System.out.println("last player joined the match -> start game");
                    List<Player> wakingList = new ArrayList<>(pendentMatchWaiting);
                    Collections.reverse(wakingList);
                    for (Player p : wakingList)
                        findControlBase(p.getNickname()).getInitController().startGame();
                }
                return true;
            }
            return false;
        }
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
