package it.polimi.ingsw.view.observer;

import it.polimi.ingsw.model.essentials.PhysicalResource;

import java.util.List;

/**
 * This interface implements the Observer-side of the paradigm.
 * The class that implements it, has to redefine each method adding View-update procedures.
 */
public interface ViewObserver {

    void updateMarket(char[][] market, char sideMarble);
    void updateCardGrid(List<String>[][] cardGrid);
    void updateLorenzoMarker(int lorenzoMarker);

    void updateWarehouse(String nickname, List<PhysicalResource> warehouse);
    void updateMarketBuffer(String nickname, List<PhysicalResource> marketBuffer);
    void updateStrongbox(String nickname, List<PhysicalResource> strongbox);
    void updateFaithMarker(String nickname, int faithMarker);
    void updatePopeTiles(String nickname, List<Integer> popeTiles);
    void updateDevCardSlots(String nickname, List<String>[] devCardSlots);
    void updateHandLeaders(String nickname, List<String> handLeaders);
    void updateActiveLeaders(String nickname, List<String> handLeaders);
    void updateWhiteMarbleConversions(String nickname, List<PhysicalResource> whiteMarbleConversions);
    void updateDiscountMap(String nickname, List<PhysicalResource> discountMap);
    void updateTempDevCard(String nickname, String tempDevCard);



}
