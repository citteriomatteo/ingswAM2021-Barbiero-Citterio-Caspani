package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.message.ctosmessage.BinarySelectionMessage;
import it.polimi.ingsw.network.message.ctosmessage.LoginMessage;
import it.polimi.ingsw.network.message.ctosmessage.NumPlayersMessage;
import it.polimi.ingsw.view.ClientController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.view.ClientController.getClientController;

public class SceneController {
    private final ClientGUI view;
    public TextField loginTextBox;
    public Label loginErrorLabel;

    public SceneController() {
        view = new ClientGUI();
        getClient().setController(view);

        new Thread(()-> getClient().startClient()).start();
    }

    @FXML
    public void login() {
        view.setSceneController(this);
        String nickname = loginTextBox.getCharacters().toString();
        getClientController().setNickname(nickname);
        (new LoginMessage(nickname)).send();
    }

    @FXML
    public void loginError(String message){
        loginErrorLabel.setText(message);
        loginErrorLabel.setOpacity(1);
    }

    @FXML
    public void single(){
        (new BinarySelectionMessage(getClient().getNickname(), true)).send();
    }

    @FXML
    public void multi(){
        (new BinarySelectionMessage(getClient().getNickname(), false)).send();
    }

    @FXML
    public void twoPlayer(){
        new NumPlayersMessage(getClient().getNickname(), 2).send();
    }

    @FXML
    public void threePlayer() {
        new NumPlayersMessage(getClient().getNickname(), 3).send();
    }

    @FXML
    public void fourPlayer(){
        new NumPlayersMessage(getClient().getNickname(), 4).send();
    }
}
