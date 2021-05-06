package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a message that notifies to every client the final leaderboard.
 * It can also be built step by step.
 * Message structure: { Map<nickname, final points> }
 */

public class EndGameResultsMessage extends StoCMessage {

    private final StoCMessageType type = StoCMessageType.END_GAME_RESULTS;
    private final String msg;
    private final Map<String, Integer> leaderBoard;

    public EndGameResultsMessage(String nickname, String msg, Map<String, Integer> leaderBoard){
        super(nickname);
        this.msg = msg;
        this.leaderBoard = leaderBoard;
    }

    public StoCMessageType getType(){
        return type;
    }
    public Map<String, Integer> getLeaderBoard(){
        return leaderBoard;
    }
    public String getMsg() { return msg; }
}
