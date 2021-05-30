package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.message.ctosmessage.BinarySelectionMessage;
import it.polimi.ingsw.network.message.ctosmessage.LoginMessage;
import it.polimi.ingsw.network.message.ctosmessage.NumPlayersMessage;
import it.polimi.ingsw.view.ClientController;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
        view = (ClientGUI) getClientController().getView(); //todo fix this not ideal cast
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
    public void numPlayers(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource() ;
        String data = (String) node.getUserData();
        int numPlayers = Integer.parseInt(data);
        new NumPlayersMessage(getClient().getNickname(), numPlayers).send();
    }
}
