package it.polimi.ingsw.view.GUI;

import javafx.scene.layout.Pane;

import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

public class RematchPhaseSceneController implements SceneController{
    public Pane basePane;

    public RematchPhaseSceneController(){
        getSceneProxy().setRematchPhaseSceneController(this);
    }

}
