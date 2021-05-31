package it.polimi.ingsw.view.GUI;


import javafx.application.Platform;

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
    }

    public void setStartingPhaseSceneController(StartingPhaseSceneController startingPhaseSceneController) {
        this.startingPhaseSceneController = startingPhaseSceneController;
    }

    public void setTurnSceneController(TurnSceneController turnSceneController) {
        this.turnSceneController = turnSceneController;
    }

    public void setRematchPhaseSceneController(RematchPhaseSceneController rematchPhaseSceneController) {
        this.rematchPhaseSceneController = rematchPhaseSceneController;
    }

    public void loginError(String errMessage) {
        Platform.runLater(()->initSceneController.loginError(errMessage));
    }
}
