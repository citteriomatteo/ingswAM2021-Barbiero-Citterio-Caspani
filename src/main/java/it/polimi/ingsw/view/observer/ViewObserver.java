package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.view.lightmodel.LightMatch;

/**
 * This interface implements the Observer-side of the paradigm.
 * The class that implements it, has to redefine each method adding View-update procedures.
 */
public interface ViewObserver {

    void updateMatch(LightMatch match);

    void updateMarket(LightMatch match);
    void updateCardGrid(LightMatch match);
    void updateLorenzoMarker(LightMatch match);

    void updateWarehouse(String nickname, LightMatch match);
    void updateMarketBuffer(String nickname, LightMatch match);
    void updateStrongbox(String nickname, LightMatch match);
    void updateFaithMarker(String nickname, LightMatch match);
    void updatePopeTiles(String nickname, LightMatch match);
    void updateDevCardSlots(String nickname, LightMatch match);
    void updateHandLeaders(String nickname, LightMatch match);
    void updateActiveLeaders(String nickname, LightMatch match);
    void updateWhiteMarbleConversions(String nickname, LightMatch match);
    void updateDiscountMap(String nickname, LightMatch match);
    void updateTempDevCard(String nickname, LightMatch match);



}
