package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.network.message.ctosmessage.LeadersChoiceMessage;
import it.polimi.ingsw.network.message.stocmessage.HandLeadersStateMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;
import it.polimi.ingsw.view.observer.ViewObserver;

import java.util.List;
import java.util.Map;

public interface View extends ViewObserver {

    void showAll(LightMatch match);
    void drawCards(List<String> id);

    void printTitle();
    void drawLoginLayout();
    void drawReconnectionLayout();
    void drawNewPlayerLayout();
    void drawNumPlayersLayout();
    void drawConfigurationChoice();
    void drawConfigurationLayout();
    void drawWaitingLayout();
    void drawStartingPhaseDone();
    void drawLeadersChoiceLayout();
    void drawResourcesChoiceLayout(int yourPosition);
    void drawYourTurnLayout(boolean yourTurn);
    void drawWhiteMarbleConversionsLayout();
    void drawResPlacementLayout();
    void drawBuyDevCardLayout();
    void drawPlaceDevCardLayout();
    void drawProductionLayout();
    void drawEndTurnLayout();
    void drawEndMatchLayout();
    void drawRematchOfferLayout(String nickname);

    void printTokenDraw(String tokenName, int remainingTokens);
    void printLastRound();
    void setLastRound(boolean lastRound);
    void printMatchResults(String message, Map<String, Integer> ranking);
    void printRetry(String errMessage, StateName currentState, LightMatch match);
    void printDiscountMap(LightPlayer player);
    void printWhiteMarbleConversions(LightPlayer player);
}
