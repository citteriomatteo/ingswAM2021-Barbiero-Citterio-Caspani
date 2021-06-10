package it.polimi.ingsw.view.GUI;

import javafx.scene.control.Label;

import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

public class GoodbyeSceneController implements SceneController {

    public Label errorMessage;

    public GoodbyeSceneController() {
        getSceneProxy().setGoodbyePhaseSceneController(this);
    }

    public void printGoodbyeMessage(String msg) {
        System.out.println("arrived: "+msg);
        errorMessage.setText(msg);
    }

}
