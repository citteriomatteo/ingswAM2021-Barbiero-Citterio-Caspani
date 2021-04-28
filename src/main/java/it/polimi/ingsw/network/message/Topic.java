package it.polimi.ingsw.network.message;

import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A place used for communication between different players of the same match and the MatchController itself
 * Every playerHandler of player in the match has to be Observer of this Object,
 * to know when a message is ready to be read. The MatchController instead blocks on reading.
 */
public class Topic extends Observable {
    //CAPACITY constant set the maximum dimension of the buffer for messages
    private static final int CAPACITY = 10;
    private final BlockingQueue<Message> inMessages;   //CtoS
    private final BlockingQueue<Message> outMessages;  //StoC
    private final AtomicInteger activePlayersInMatch;
    private final AtomicInteger views;

    /**
     * Builds the topic object initializing the buffers for messages and setting the active players (not disconnected)
     * in the match linked to this topic
     * @param activePlayersInMatch the active players in the match linked to this topic
     */
    public Topic(int activePlayersInMatch) {
        this.inMessages = new ArrayBlockingQueue<>(CAPACITY);
        this.outMessages = new ArrayBlockingQueue<>(CAPACITY);
        this.activePlayersInMatch = new AtomicInteger(activePlayersInMatch);
        views = new AtomicInteger();
    }

    /**
     * Decrements the number of active players inside the match linked to this topic
     * and returns the new number of active players
     * @return the new number of active players
     */
    public int playerDisconnectionOccurred(){
        return activePlayersInMatch.decrementAndGet();
    }

    //%%%%%%%%%%% Push messages %%%%%%%%%%%%%

    /**
     * Tries to add a CtoS message in this topic,
     * if the queue of messages is full, wait since someone else pulls out another CtoS message
     * @param msg the message you want to push
     * @return true if the message has been inserted, false if something goes wrong while waiting for free space
     */
    public boolean pushCtoSMessage(Message msg){
        try {
            inMessages.put(msg);
            return true;
        } catch (InterruptedException e) {
            System.out.println("Something goes wrong while waiting for free space to push a message");
            e.printStackTrace();
            //TODO: interruption
        }
        return false;
    }
    /**
     * Tries to add a StoC message in this topic,
     * if the queue of messages is full, wait since someone else pulls out another StoC message.
     * After the insertion, Observers (Clients) will be notified.
     * @param msg the message you want to push
     * @return true if the message has been inserted, false if something goes wrong while waiting for free space
     */
    public boolean pushStoCMessage(Message msg){
        try {
            outMessages.put(msg);
            System.out.println("Message pushed inside of topic");
            setChanged();
            notifyObservers();
            System.out.println("notified " + countObservers() + " observers");
            return true;
        } catch (InterruptedException e) {
            System.out.println("Something goes wrong while waiting for free space to push a message");
            e.printStackTrace();
            //TODO: interruption
        }
        return false;
    }

    //%%%%%%%%%%% Pull messages %%%%%%%%%%%%%%
    /**
     * Takes the first possible message from the inMessages queue in this topic,
     * if there aren't messages to be pulled wait since someone pushes a message.
     * @return the first message in the CtoS queue or null
     * @throws InterruptedException if interrupted while waiting
     */
    public Message pullCtoSMessage() throws InterruptedException {
        return inMessages.take();
    }

    /**
     * @return true if are there any messages in the CtoS queue
     */
    public boolean areThereCtoSMessages(){
        return !inMessages.isEmpty();
    }

    /**
     * Takes the first possible message from the outMessages queue in this topic,
     * if there aren't messages to be pulled returns null.
     * If you are not the last player of the match the message remains inside the queue,
     * ready to be pulled by the others, otherwise remove the message from the queue
     * then controls if are there any other messages to be read, in that case, notifies all the Observers
     * @return the first message in the StoC queue or null
     */
    public Message pullStoCMessage(){
        if(views.incrementAndGet() == activePlayersInMatch.get()) {
            Message msg = outMessages.poll();
            views.set(0);
            if(!outMessages.isEmpty()) {
                setChanged();
                notifyObservers();
            }
            return msg;
        }

        return outMessages.peek();
    }
}
