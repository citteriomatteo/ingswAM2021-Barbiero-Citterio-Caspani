package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.message.ctosmessage.BinarySelectionMessage;
import it.polimi.ingsw.network.message.ctosmessage.LoginMessage;
import it.polimi.ingsw.network.message.ctosmessage.NumPlayersMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.view.ClientController.getClientController;
import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

/**
 * A scene controller used for communicating with the scenes of the login phase.
 */
public class InitSceneController implements SceneController{
    public Pane basePane;
    public TextField loginTextBox;
    public Label loginErrorLabel;
    public Button acceptButton;
    public Button declineButton;


    /**
     * Creates an instance and links it to the {@link SceneProxy}.
     */
    public InitSceneController() {
        getSceneProxy().setInitSceneController(this);
    }

    /**
     * Sends a LoginMessage with getting the nickname by the string in loginTextBox.
     */
    @FXML
    public void login() {
        String nickname = loginTextBox.getCharacters().toString();
        getClientController().setNickname(nickname);
        (new LoginMessage(nickname)).send();
    }

    /**
     * Shows a pop up with a warning message.
     * It's called when the nickname chosen is unacceptable.
     * @param message the warning message by the server.
     */
    @FXML
    public void loginError(String message){
        JavaFXGUI.popUpWarning(message);
    }

    /**
     * Gets the boolean choice by the userData associate with the button and
     * sends a BinarySelectionMessage to the server.
     * @param actionEvent the ActionEvent that calls the method.
     */
    @FXML
    public void selection(ActionEvent actionEvent){
        Node node = (Node) actionEvent.getSource();
        String data = (String) node.getUserData();
        boolean selection = Boolean.parseBoolean(data);
        (new BinarySelectionMessage(getClient().getNickname(), selection)).send();
    }

    /**
     * Gets the number of player the client wants by the userData associate with the button and
     * sends a NumPlayersMessage to the server.
     * @param actionEvent the ActionEvent that calls the method.
     */
    @FXML
    public void numPlayers(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        String data = (String) node.getUserData();
        int numPlayers = Integer.parseInt(data);
        new NumPlayersMessage(getClient().getNickname(), numPlayers).send();
    }

    /**
     * Sends a positive BinarySelectionMessage to re-enter the player in the game from which he disconnected.
     */
    @FXML
    public void reconnect() {
        new BinarySelectionMessage(getClient().getNickname(), true).send();
    }

    /**
     * Sends a negative BinarySelectionMessage to inform the server that this is not the player disconnected with the same name.
     */
    @FXML
    public void backToLogin() {
        new BinarySelectionMessage(getClient().getNickname(), false).send();
    }
}
