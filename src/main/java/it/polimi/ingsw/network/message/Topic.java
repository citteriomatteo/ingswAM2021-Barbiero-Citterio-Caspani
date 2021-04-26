package it.polimi.ingsw.network.message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * A place used for communication between different players of the same match and the MatchController itself
 */
public class Topic {
    //CAPACITY constant set the maximum dimension of the buffer for messages
    public static final int CAPACITY = 10;
    public BlockingQueue<Message> inMessages;
    public BlockingQueue<Message> outMessages;

    public Topic() {
        this.inMessages = new ArrayBlockingQueue<>(CAPACITY);
        this.outMessages = new ArrayBlockingQueue<>(CAPACITY);
    }


}
