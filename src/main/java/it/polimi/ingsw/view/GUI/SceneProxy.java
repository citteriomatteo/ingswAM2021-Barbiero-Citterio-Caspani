package it.polimi.ingsw.view.GUI;


import javafx.application.Platform;

import java.io.IOException;

import static it.polimi.ingsw.network.client.Client.getClient;

public class SceneProxy {
    private static final SceneProxy instance = new SceneProxy();
    private InitSceneController initSceneController;
    private StartingPhaseSceneController startingPhaseSceneController;
    private TurnSceneController turnSceneController;
    private RematchPhaseSceneController rematchPhaseSceneController;


    public static SceneProxy getSceneProxy(){ return instance; }

    //%%%%%%%%%%%%%%%%%%%%%%%%SETTER%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    public void setInitSceneController(InitSceneController initSceneController) {
        this.initSceneController = initSceneController;
        this.startingPhaseSceneController = null;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = null;
    }

    public void setStartingPhaseSceneController(StartingPhaseSceneController startingPhaseSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = startingPhaseSceneController;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = null;

    }

    public void setTurnSceneController(TurnSceneController turnSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = null;
        this.turnSceneController = turnSceneController;
        this.rematchPhaseSceneController = null;

    }

    public void setRematchPhaseSceneController(RematchPhaseSceneController rematchPhaseSceneController) {
        this.initSceneController = null;
        this.startingPhaseSceneController = null;
        this.turnSceneController = null;
        this.rematchPhaseSceneController = rematchPhaseSceneController;

    }

    public void changeScene(SceneName scene){
        Platform.runLater(()->{
            try {
                JavaFXGUI.setRoot(scene.name());
            } catch (IOException e) {
                e.printStackTrace();
                getClient().exit();
            }
        });
    }

    public void loginError(String errMessage) {
        Platform.runLater(()->initSceneController.loginError(errMessage));
    }
}
