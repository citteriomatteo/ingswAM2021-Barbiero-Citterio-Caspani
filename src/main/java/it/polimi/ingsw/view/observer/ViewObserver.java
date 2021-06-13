package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.view.lightmodel.LightMatch;

/**
 * This interface implements the Observer-side of the client paradigm.
 * The class that implements it, has to redefine each method adding View-update procedures.
 */
public interface ViewObserver {

    /**
     * Set to update the LightMatch situation and call a graphic update.
     * @param match the match
     */
    void updateMatch(LightMatch match);

    /**
     * Set to update the light market and call a graphic update.
     * @param match the match
     */
    void updateMarket(LightMatch match);
    /**
     * Set to update the light card grid and call a graphic update.
     * @param match the match
     */
    void updateCardGrid(LightMatch match);
    /**
     * Set to update the light card grid and call a graphic update (in single-player matches).
     * @param match the match
     */
    void updateLorenzoMarker(LightMatch match);

    /**
     * Set to update the light warehouse and call a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateWarehouse(String nickname, LightMatch match);
    /**
     * Set to update the light market buffer and call a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateMarketBuffer(String nickname, LightMatch match);
    /**
     * Set to update the light strongbox and call a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateStrongbox(String nickname, LightMatch match);
    /**
     * Set to update the faith marker position and call a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateFaithMarker(String nickname, LightMatch match);
    /**
     * Set to update the light pope tiles' state and call a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updatePopeTiles(String nickname, LightMatch match);
    /**
     * Set to update the light dev card slots and call a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateDevCardSlots(String nickname, LightMatch match);
    /**
     * Set to update the light hand leaders' state and call a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateHandLeaders(String nickname, LightMatch match);
    /**
     * Set to update the light active leaders' state and call a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateActiveLeaders(String nickname, LightMatch match);
    /**
     * Set to update the white marble conversions' map.
     * @param nickname the nickname
     * @param match the match
     */
    void updateWhiteMarbleConversions(String nickname, LightMatch match);
    /**
     * Set to update the discounts' map.
     * @param nickname the nickname
     * @param match the match
     */
    void updateDiscountMap(String nickname, LightMatch match);
    /**
     * Set to update the white temporary dev card and call a graphic update.
     * @param nickname the nickname
     * @param match the match
     */
    void updateTempDevCard(String nickname, LightMatch match);
    /**
     * Set to update the connection state of a player and call a graphic update.
     * @param nickname the player that has connected/disconnected
     * @param connected the connection state boolean
     */
    void updateDisconnections(String nickname, boolean connected);
}
