package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.view.lightmodel.LightMatch;

/**
 * This interface implements the Observer-side of the client paradigm.
 * The class that implements it, has to redefine each method adding View-update procedures.
 */
public interface ViewObserver {

    /**
     * Updates the LightMatch situation and calls a graphic update.
     * @param match the match
     */
    void updateMatch(LightMatch match);

    /**
     * Updates the light market and calls a graphic update.
     * @param match the match
     */
    void updateMarket(LightMatch match);
    /**
     * Updates the light card grid and calls a graphic update.
     * @param match the match
     */
    void updateCardGrid(LightMatch match);
    /**
     * Updates the light card grid and calls a graphic update (in single-player matches).
     * @param match the match
     */
    void updateLorenzoMarker(LightMatch match);

    /**
     * Updates the light warehouse and calls a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateWarehouse(String nickname, LightMatch match);
    /**
     * Updates the light market buffer and calls a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateMarketBuffer(String nickname, LightMatch match);
    /**
     * Updates the light strongbox and calls a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateStrongbox(String nickname, LightMatch match);
    /**
     * Updates the faith marker position and calls a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateFaithMarker(String nickname, LightMatch match);
    /**
     * Updates the light pope tiles' state and calls a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updatePopeTiles(String nickname, LightMatch match);
    /**
     * Updates the light dev card slots and calls a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateDevCardSlots(String nickname, LightMatch match);
    /**
     * Updates the light hand leaders' state and calls a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateHandLeaders(String nickname, LightMatch match);
    /**
     * Updates the light active leaders' state and calls a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateActiveLeaders(String nickname, LightMatch match);
    /**
     * Updates the white marble conversions' map.
     * @param nickname the nickname
     * @param match the match
     */
    void updateWhiteMarbleConversions(String nickname, LightMatch match);
    /**
     * Updates the discounts' map.
     * @param nickname the nickname
     * @param match the match
     */
    void updateDiscountMap(String nickname, LightMatch match);
    /**
     * Updates the white temporary dev card and calls a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateTempDevCard(String nickname, LightMatch match);
    /**
     * Updates the connection state of a player and calls a graphic update.
     * @param nickname the player that has connected/disconnected
     * @param connected the connection state boolean
     */
    void updateDisconnections(String nickname, boolean connected);
}
