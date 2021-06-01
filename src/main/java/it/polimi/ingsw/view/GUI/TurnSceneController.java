package it.polimi.ingsw.view.GUI;

import javafx.scene.layout.Pane;

import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

public class TurnSceneController implements SceneController{
    public Pane basePane;
    public TurnSceneController() {
        getSceneProxy().setTurnSceneController(this);
    }

    @Override
    public void disableAll() {
        basePane.setDisable(true);
    }
}
