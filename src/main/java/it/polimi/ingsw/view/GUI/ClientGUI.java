package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;
import javafx.application.Platform;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ClientGUI implements View {
    private SceneController sceneController;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

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
        Platform.runLater(()-> {
            try {
                JavaFXGUI.setRoot(SceneName.ModeSelectionScene.name());
            } catch (IOException e) {
                e.printStackTrace();
                //todo: fix
            }
        });

    }

    @Override
    public void drawNumPlayersLayout() {
        Platform.runLater(()->{
        try {
            JavaFXGUI.setRoot(SceneName.NumPlayerScene.name());
        } catch (IOException e) {
            e.printStackTrace();
            //todo: fix
        }
    });

    }

    @Override
    public void drawConfigurationChoice() {
        Platform.runLater(()->{
            try {
                JavaFXGUI.setRoot(SceneName.ConfigurationChoiceScene.name());
            } catch (IOException e) {
                e.printStackTrace();
                //todo: fix
            }
        });

    }

    @Override
    public void drawConfigurationLayout() {

    }

    @Override
    public void drawWaitingLayout() {
        Platform.runLater(()->{
            try {
                JavaFXGUI.setRoot(SceneName.WaitingScene.name());
            } catch (IOException e) {
                e.printStackTrace();
                //todo: fix
            }
        });

    }

    @Override
    public void drawLeadersChoiceLayout() {
        Platform.runLater(()->{
            try {
                JavaFXGUI.setRoot(SceneName.LeadersChoiceScene.name());
            } catch (IOException e) {
                e.printStackTrace();
                //todo: fix
            }
        });
    }

    @Override
    public void drawResourcesChoiceLayout(int yourPosition) {

    }

    @Override
    public void drawYourTurnLayout(boolean yourTurn) {
        Platform.runLater(()->{
            try {
                JavaFXGUI.setRoot(SceneName.GameScene.name());
            } catch (IOException e) {
                e.printStackTrace();
                //todo: fix
            }
        });
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
                Platform.runLater(()->sceneController.loginError(errMessage));
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
