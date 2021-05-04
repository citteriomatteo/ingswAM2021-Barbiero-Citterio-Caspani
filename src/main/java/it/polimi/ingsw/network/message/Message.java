package it.polimi.ingsw.network.message;

/**
 * Abstract class for a general message: contains only the nickname of the player, which means:
 *  - for StoCMessages: the nickname of the receiver player;
 *  - for CtoSMessages: the nickname of the client that contacts the controller.
 */
public abstract class Message {

    private final String nickname;

    public Message(String nickname){
        this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
    }

    public abstract MessageType getType();

}
