package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a message that contains the new state of a certain tile and implies a VIEW change of every player
 * on every client on the match.
 * It is possible to build the message one step at a time or all together.
 * Message structure: { nickname, number of the tile, Map<player_nickname,new value of tile> }
 */

public class VaticanReportMessage extends Message {

    private final StoCMessageType type = StoCMessageType.VATICAN_REPORT;
    private int tileNumber;
    private Map<String, Integer> tilesState;

    public VaticanReportMessage(String nickname, int tileNumber, Map<String, Integer> tilesState){
        super(nickname);
        this.tileNumber = tileNumber;
        this.tilesState = new HashMap<>(tilesState);
    }

    /**
     * simple constructor: in case you want to add the state of the tile one player at a time.
     * @param tileNumber
     */
    public VaticanReportMessage(String nickname, int tileNumber){
        super(nickname);
        this.tileNumber = tileNumber;
    }

    /**
     * This method inserts on the message the tile state of a single player.
     * @param nickname
     * @param state
     * @return true
     */
    public boolean insertTileState(String nickname, int state){
        tilesState.put(nickname, state);
        return true;
    }


    private StoCMessageType getType(){ return type; }
    private int getTileNumber(){ return tileNumber; }
    private Map<String,Integer> getTilesState(){ return tilesState; }

}
