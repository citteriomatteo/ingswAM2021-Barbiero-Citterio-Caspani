package it.polimi.ingsw.view;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.view.observer.ViewObserver;

import java.util.List;

public class View implements ViewObserver {





    //UPDATE METHODS GOT FROM ViewObserver
    @Override
    public void updateMarket(char[][] market, char sideMarble) {

    }

    @Override
    public void updateCardGrid(List<String>[][] cardGrid) {

    }

    @Override
    public void updateLorenzoMarker(int lorenzoMarker) {

    }

    @Override
    public void updateWarehouse(String nickname, List<PhysicalResource> warehouse) {

    }

    @Override
    public void updateMarketBuffer(String nickname, List<PhysicalResource> marketBuffer) {

    }

    @Override
    public void updateStrongbox(String nickname, List<PhysicalResource> strongbox) {

    }

    @Override
    public void updateFaithMarker(String nickname, int faithMarker) {

    }

    @Override
    public void updatePopeTiles(String nickname, List<Integer> popeTiles) {

    }

    @Override
    public void updateDevCardSlots(String nickname, List<String>[] devCardSlots) {

    }

    @Override
    public void updateHandLeaders(String nickname, List<String> handLeaders) {

    }

    @Override
    public void updateActiveLeaders(String nickname, List<String> handLeaders) {

    }

    @Override
    public void updateWhiteMarbleConversions(String nickname, List<PhysicalResource> whiteMarbleConversions) {

    }

    @Override
    public void updateDiscountMap(String nickname, List<PhysicalResource> discountMap) {

    }

    @Override
    public void updateTempDevCard(String nickname, String tempDevCard) {

    }
}
