package it.polimi.ingsw.view;

import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.network.message.ctosmessage.LeadersChoiceMessage;
import it.polimi.ingsw.network.message.stocmessage.HandLeadersStateMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.observer.ViewObserver;

import java.util.Map;

public interface View extends ViewObserver {


    void drawCard(Card card, String id);
    void viewEnemy(String nickname, LightMatch match);

    void printTitle();
    void drawLoginLayout();
    void drawReconnectionLayout();
    void drawNewPlayerLayout();
    void drawNumPlayersLayout();
    void drawConfigurationChoice();
    void drawConfigurationLayout();
    void drawWaitingLayout();
    void drawLeadersChoiceLayout();
    void drawResourcesChoiceLayout();
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
    void printMatchResults(String message, Map<String, Integer> ranking);
    void printRetry(String errMessage);
}
