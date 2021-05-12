package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a message that contains the new state of a certain tile and implies a VIEW change of every player
 * on every client on the match.
 * It is possible to build the message one step at a time or all together.
 * Message structure: { nickname, number of the tile, Map<player_nickname,new value of tile> }
 */

public class VaticanReportMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.VATICAN_REPORT;
    private final int tileNumber;
    private final List<Integer> popeTiles;

    public VaticanReportMessage(String nickname, int tileNumber, List<Integer> popeTiles){
        super(nickname);
        this.tileNumber = tileNumber;
        this.popeTiles = popeTiles;
    }

    public StoCMessageType getType(){ return type; }
    public int getTileNumber(){ return tileNumber; }
    public List<Integer> getTilesState(){ return popeTiles; }

}
