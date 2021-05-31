package it.polimi.ingsw.view.GUI;

import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

public class TurnSceneController {
    public TurnSceneController() {
        getSceneProxy().setTurnSceneController(this);
    }

}
