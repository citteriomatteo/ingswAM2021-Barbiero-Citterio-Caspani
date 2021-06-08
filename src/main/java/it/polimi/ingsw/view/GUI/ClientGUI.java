package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;

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
        getSceneProxy().changeScene(SceneName.ReconnectionScene);
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
        getSceneProxy().loadStartingMatch();
        getSceneProxy().disableAll(true);
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
        //useful for reconnections
        if(!getSceneProxy().turnSceneIsLoaded())
            getSceneProxy().changeScene(SceneName.GameScene);


        getSceneProxy().yourTurn(yourTurn);
        //getSceneProxy().disableOnState();
    }

    @Override
    public void drawWhiteMarbleConversionsLayout() {
        getSceneProxy().marketActionPhaseDisables();
    }

    @Override
    public void drawResPlacementLayout() {
        getSceneProxy().resourcesPlacementPhaseDisables();
    }

    @Override
    public void drawBuyDevCardLayout() {
        getSceneProxy().buyDevActionPhaseDisables();
    }

    @Override
    public void drawPlaceDevCardLayout() {
        getSceneProxy().placeDevCardPhaseDisables();
    }

    @Override
    public void drawProductionLayout() {
        getSceneProxy().productionActionPhaseDisables();
    }

    @Override
    public void drawEndTurnLayout() {
        getSceneProxy().endTurnState();
        getSceneProxy().endTurnPhaseDisables();
    }

    @Override
    public void drawEndMatchLayout() {
        //TODO
    }

    @Override
    public void drawRematchOfferLayout(String nickname) {

    }

    @Override
    public void printTokenDraw(String tokenName, int remainingTokens) {
        getSceneProxy().updateTokenDrawn(tokenName, remainingTokens);
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
                break;
            case RESOURCES_PLACEMENT:
                getSceneProxy().updateMarketBuffer(getClient().getNickname(), match.getLightPlayer(getClient().getNickname()).getMarketBuffer());
                getSceneProxy().updateWarehouse(getClient().getNickname(), match.getLightPlayer(getClient().getNickname()).getWarehouse());
                getSceneProxy().printRetry(errMessage);
                break;
            case PRODUCTION_ACTION:
            case BUY_DEV_ACTION:
                getSceneProxy().resetPayments();
                getSceneProxy().printRetry(errMessage);
            default:
                getSceneProxy().printRetry(errMessage);
                break;
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
    }

    @Override
    public void updateMarket(LightMatch match) {
        getSceneProxy().updateMarket(match.getMarket());
    }

    @Override
    public void updateCardGrid(LightMatch match) {
        getSceneProxy().updateCardGrid(match.getCardGrid());
    }

    @Override
    public void updateLorenzoMarker(LightMatch match) {
        getSceneProxy().updateFaithMarker("Lorenzo the Magnificent", match.getLorenzoMarker());
    }

    @Override
    public void updateWarehouse(String nickname, LightMatch match) {
        getSceneProxy().updateWarehouse(nickname, match.getLightPlayer(nickname).getWarehouse());
    }

    @Override
    public void updateMarketBuffer(String nickname, LightMatch match) {
        getSceneProxy().updateMarketBuffer(nickname, match.getLightPlayer(nickname).getMarketBuffer());
    }

    @Override
    public void updateStrongbox(String nickname, LightMatch match) {
        getSceneProxy().updateStrongBox(nickname, match.getLightPlayer(nickname).getStrongbox());

    }

    @Override
    public void updateFaithMarker(String nickname, LightMatch match) {
        getSceneProxy().updateFaithMarker(nickname, match.getLightPlayer(nickname).getFaithMarker());

    }

    @Override
    public void updatePopeTiles(String nickname, LightMatch match) {

    }

    @Override
    public void updateDevCardSlots(String nickname, LightMatch match) {
        getSceneProxy().updateDevCardSlots(nickname, match.getLightPlayer(nickname).getDevCardSlots());
    }

    @Override
    public void updateHandLeaders(String nickname, LightMatch match) {
        getSceneProxy().updateHandLeaders(nickname, match.getLightPlayer(nickname).getHandLeaders());

    }

    @Override
    public void updateActiveLeaders(String nickname, LightMatch match) {
        getSceneProxy().updateActiveLeaders(nickname, match.getLightPlayer(nickname).getActiveLeaders());

    }

    @Override
    public void updateWhiteMarbleConversions(String nickname, LightMatch match) {

    }

    @Override
    public void updateDiscountMap(String nickname, LightMatch match) {

    }

    @Override
    public void updateTempDevCard(String nickname, LightMatch match) {
        getSceneProxy().updateTempDevCard(match.getLightPlayer(nickname).getTempDevCard());
    }

    @Override
    public void updateDisconnections(String nickname, boolean connected) {
        getSceneProxy().updateDisconnections(nickname, connected);
    }
}
