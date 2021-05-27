package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.message.ctosmessage.LoginMessage;
import it.polimi.ingsw.view.ClientController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static it.polimi.ingsw.network.client.Client.getClient;

public class SceneController {
    private final ClientGUI view;
    public TextField loginTextBox;
    public Label loginErrorLabel;

    public SceneController() {
        view = new ClientGUI();
        ClientController clientController = new ClientController(view);
        getClient().setController(clientController);

        new Thread(()-> getClient().startClient()).start();
    }

    @FXML
    public void login() {
        view.setSceneController(this);
        (new LoginMessage(loginTextBox.getCharacters().toString())).send();
    }

    @FXML
    public void loginError(String message){
        loginErrorLabel.setText(message);
        loginErrorLabel.setOpacity(1);
    }
}
