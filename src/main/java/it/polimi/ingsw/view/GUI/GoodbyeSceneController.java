package it.polimi.ingsw.view.GUI;

import javafx.scene.control.Label;

import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

/**
 * A scene controller used to communicate with the goodbye scene.
 */
public class GoodbyeSceneController{
    public Label errorMessage;

    /**
     * Creates an instance and links it to the {@link SceneProxy}.
     */
    public GoodbyeSceneController() {
        getSceneProxy().setGoodbyePhaseSceneController(this);
    }

    /**
     * Prints the goodbye message on the scene
     * @param msg the goodbye message.
     */
    public void printGoodbyeMessage(String msg) {
        errorMessage.setText(msg);
    }

}
