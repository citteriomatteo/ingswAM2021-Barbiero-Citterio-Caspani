package it.polimi.ingsw.network.message;

import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A place used for communication between different players of the same match and the MatchController itself
 * Every player in the match and the matchController itself have to be Observer of this Object
 * to know when a message is ready to be read
 */
public class Topic extends Observable {
    //CAPACITY constant set the maximum dimension of the buffer for messages
    private static final int CAPACITY = 10;
    private final BlockingQueue<Message> inMessages;   //CtoS
    private final BlockingQueue<Message> outMessages;  //StoC
    private final int playersInMatch;
    private final AtomicInteger views;

    public Topic(int playersInMatch) {
        this.inMessages = new ArrayBlockingQueue<>(CAPACITY);
        this.outMessages = new ArrayBlockingQueue<>(CAPACITY);
        this.playersInMatch = playersInMatch;
        views = new AtomicInteger();
    }

    //%%%%%%%%%%% Push messages %%%%%%%%%%%%%

    /**
     * Try to add a CtoS message in this topic,
     * if the queue of messages is full, wait since someone else pulls out another CtoS message
     * @param msg the message you want to push
     * @return true if the message has been inserted, false if something goes wrong while waiting for free space
     */
    public boolean pushCtoSMessage(Message msg){
        try {
            inMessages.put(msg);
            setChanged();
            notifyObservers("CtoS");
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Try to add a StoC message in this topic,
     * if the queue of messages is full, wait since someone else pulls out another StoC message
     * @param msg the message you want to push
     * @return true if the message has been inserted, false if something goes wrong while waiting for free space
     */
    public boolean pushStoCMessage(Message msg){
        try {
            outMessages.put(msg);
            System.out.println("Message pushed inside of topic");
            setChanged();
            notifyObservers("StoC");
            System.out.println("notified " + countObservers() + " observers");
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    //%%%%%%%%%%% Pull messages %%%%%%%%%%%%%%
    /**
     * Take the first possible message from the inMessages queue in this topic,
     * if there aren't messages to be pulled returns null.
     * Then controls if is there any other messages to be read, in that case, notifies all the Observers
     * @return the first message in the CtoS queue or null
     */
    public Message pullCtoSMessage(){
        Message msg = inMessages.poll();
        if(!inMessages.isEmpty()) {
            setChanged();
            notifyObservers("CtoS");
        }
        return msg;
    }

    /**
     * Take the first possible message from the outMessages queue in this topic,
     * if there aren't messages to be pulled returns null.
     * If you are not the last player of the match the message remains inside the queue,
     * ready to be pulled by the others, otherwise remove the message from the queue
     * then controls if is there any other messages to be read, in that case, notifies all the Observers
     * @return the first message in the StoC queue or null
     */
    public Message pullStoCMessage(){
        if(views.incrementAndGet() == playersInMatch) {
            Message msg = outMessages.poll();
            views.set(0);
            if(!outMessages.isEmpty()) {
                setChanged();
                notifyObservers("StoC");
            }
            return msg;
        }

        return outMessages.peek();
    }
}
