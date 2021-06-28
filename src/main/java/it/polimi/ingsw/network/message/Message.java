package it.polimi.ingsw.network.message;

/**
 * Abstract class for a general message: contains only the nickname of the player, which means:
 *  - for StoCMessages: the nickname of the receiver player;
 *  - for CtoSMessages: the nickname of the client that contacts the controller.
 */
public abstract class Message {
    private static boolean isLocal = false;
    private final String nickname;

    /**
     * Sets the 'isLocal' variable to indicate that this match is played locally
     */
    public static void setIsLocal(){
        isLocal = true;
    }

    /**
     * Getter
     * @return the value of 'isLocal'
     */
    public static boolean getIsLocal(){
        return isLocal;
    }

    /**
     * General constructor: called by every subclass to store a nickname.
     * @param nickname the nickname
     */
    public Message(String nickname){
        this.nickname = nickname;
    }
    /**
     * Getter
     * @return the nickname
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * This method is redefined in every message and returns the specific type of the message.
     * All messages types are contained in the two enumerations called StoCMessageType and CtoSMessageType.
     * @return the type
     */
    public abstract MessageType getType();

}
