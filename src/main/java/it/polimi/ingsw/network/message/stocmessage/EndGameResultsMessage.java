package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a message that notifies to every client the final leaderboard.
 * It can also be built step by step.
 * Message structure: { Map<nickname, final points> }
 */

public class EndGameResultsMessage extends Message {

    private final StoCMessageType type = StoCMessageType.END_GAME_RESULTS;
    private final Map<String, Integer> leaderBoard;

    public EndGameResultsMessage(String nickname, Map<String, Integer> leaderBoard){
        super(nickname);
        this.leaderBoard = leaderBoard;
    }

    /**
     * Simple constructor and functions: useful in the case you want to populate
     * the leaderboard manually and directly in the message to send.
     */
    public EndGameResultsMessage(String nickname){
        super(nickname);
        leaderBoard = new HashMap<>();
    }

    /**
     * This method allows to insert one player at a time.
     * @param nickname
     * @param points
     * @return true
     */
    public boolean insertPlayer(String nickname, int points){
        leaderBoard.put(nickname, points);
        return true;
    }

    public StoCMessageType getType(){
        return type;
    }
    public Map<String, Integer> getLeaderBoard(){
        return leaderBoard;
    }

}
