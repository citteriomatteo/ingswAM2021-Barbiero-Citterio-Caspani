package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.ClientController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;
import javafx.application.Platform;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.view.ClientController.getClientController;
import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

public class ClientGUI implements View {

    @Override
    public void showAll(LightMatch match) {

    }

    @Override
    public void drawCards(List<String> id) {

    }

    @Override
    public void printTitle() {

    }

    @Override
    public void drawLoginLayout() {

    }

    @Override
    public void drawReconnectionLayout() {

    }

    @Override
    public void drawNewPlayerLayout() {
        getSceneProxy().changeScene(SceneName.ModeSelectionScene);

    }

    @Override
    public void drawNumPlayersLayout() {
        getSceneProxy().changeScene(SceneName.NumPlayerScene);
    }

    @Override
    public void drawConfigurationChoice() {
        getSceneProxy().changeScene(SceneName.ConfigurationChoiceScene);
    }

    @Override
    public void drawConfigurationLayout() {

    }

    @Override
    public void drawWaitingLayout() {
        getSceneProxy().changeScene(SceneName.WaitingScene);
    }

    @Override
    public void drawStartingPhaseDone() {
        getSceneProxy().changeScene(SceneName.GameScene);
        getSceneProxy().disableAll();
    }

    @Override
    public void drawLeadersChoiceLayout() {
        getSceneProxy().changeScene(SceneName.LeadersChoiceScene);
        getSceneProxy().loadLeaderCards(getClientController().getMatch().getLightPlayer(getClient().getNickname()).getHandLeaders());

    }

    @Override
    public void drawResourcesChoiceLayout(int yourPosition) {
        getSceneProxy().changeScene(SceneName.StartingResourceScene);
        getSceneProxy().loadStartingResources((yourPosition)/2);
    }

    @Override
    public void drawYourTurnLayout(boolean yourTurn) {
        getSceneProxy().changeScene(SceneName.GameScene);
        if(!yourTurn)
            getSceneProxy().disableAll();
        else
            getSceneProxy().loadStartingTurn();
    }

    @Override
    public void drawWhiteMarbleConversionsLayout() {

    }

    @Override
    public void drawResPlacementLayout() {

    }

    @Override
    public void drawBuyDevCardLayout() {

    }

    @Override
    public void drawPlaceDevCardLayout() {

    }

    @Override
    public void drawProductionLayout() {

    }

    @Override
    public void drawEndTurnLayout() {

    }

    @Override
    public void drawEndMatchLayout() {

    }

    @Override
    public void drawRematchOfferLayout(String nickname) {

    }

    @Override
    public void printTokenDraw(String tokenName, int remainingTokens) {

    }

    @Override
    public void printLastRound() {

    }

    @Override
    public void setLastRound(boolean lastRound) {

    }

    @Override
    public void printMatchResults(String message, Map<String, Integer> ranking) {

    }

    @Override
    public void printRetry(String errMessage, StateName currentState, LightMatch match) {
        switch (currentState){
            case LOGIN:
                getSceneProxy().loginError(errMessage);
                break;
            case WAITING_LEADERS:
                getSceneProxy().leadersChoiceError(errMessage);
                getSceneProxy().leadersChoiceError(errMessage);
        }
    }

    @Override
    public void printDiscountMap(LightPlayer player) {

    }

    @Override
    public void printWhiteMarbleConversions(LightPlayer player) {

    }

    @Override
    public void updateMatch(LightMatch match) {
        getSceneProxy().setCardMap(LightMatch.getCardMap());
        getSceneProxy().setMarblesMap();

    }

    @Override
    public void updateMarket(LightMatch match) {

    }

    @Override
    public void updateCardGrid(LightMatch match) {

    }

    @Override
    public void updateLorenzoMarker(LightMatch match) {

    }

    @Override
    public void updateWarehouse(String nickname, LightMatch match) {

    }

    @Override
    public void updateMarketBuffer(String nickname, LightMatch match) {

    }

    @Override
    public void updateStrongbox(String nickname, LightMatch match) {

    }

    @Override
    public void updateFaithMarker(String nickname, LightMatch match) {

    }

    @Override
    public void updatePopeTiles(String nickname, LightMatch match) {

    }

    @Override
    public void updateDevCardSlots(String nickname, LightMatch match) {

    }

    @Override
    public void updateHandLeaders(String nickname, LightMatch match) {

    }

    @Override
    public void updateActiveLeaders(String nickname, LightMatch match) {

    }

    @Override
    public void updateWhiteMarbleConversions(String nickname, LightMatch match) {

    }

    @Override
    public void updateDiscountMap(String nickname, LightMatch match) {

    }

    @Override
    public void updateTempDevCard(String nickname, LightMatch match) {

    }
}
