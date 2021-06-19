package it.polimi.ingsw.network.message.stocmessage;

import it.polimi.ingsw.network.client.Client;

/**
 * This class implements the message that the server sends to every client in the match
 * when someone makes a deal on the market.
 * Message structure: { nickname, side marble, row/column changed, row/column boolean, chosen row/column }
 */
public class MarketChangeMessage extends StoCMessage {

    private static final StoCMessageType type = StoCMessageType.MARKET_CHANGE;
    private final char sideMarble;
    private final char[][] newMarket;

    /**
     * Constructor of a market change's message.
     * @param nickname the receiver
     * @param sideMarble the slide marble
     * @param newMarket the new market structure
     */
    public MarketChangeMessage(String nickname, char sideMarble, char[][] newMarket)
    {
        super(nickname);
        this.sideMarble = sideMarble;
        this.newMarket = newMarket;
    }

    @Override
    public boolean compute(Client client){
        client.getController().getMatch().setMarket(newMarket, sideMarble);
        return true;
    }

    @Override
    public StoCMessageType getType(){ return type; }

    /**
     * Getter
     * @return the slide marble
     */
    public Character getSideMarble(){ return sideMarble; }
    /**
     * Getter
     * @return the new market version
     */
    public char[][] getNewMarket(){ return newMarket; }

}
