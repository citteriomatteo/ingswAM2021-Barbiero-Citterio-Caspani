package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.message.ctosmessage.BinarySelectionMessage;
import it.polimi.ingsw.network.message.ctosmessage.LoginMessage;
import it.polimi.ingsw.network.message.ctosmessage.NumPlayersMessage;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.view.ClientController.getClientController;
import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

public class InitSceneController {
    public TextField loginTextBox;
    public Label loginErrorLabel;
    public ImageView firstCard;
    public ImageView secondCard;
    public ImageView thirdCard;
    public ImageView fourthCard;

    public InitSceneController() {
        getSceneProxy().setInitSceneController(this);
    }

    @FXML
    public void login() {
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
    public void selection(MouseEvent actionEvent){
        Node node = (Node) actionEvent.getSource();
        String data = (String) node.getUserData();
        boolean selection = Boolean.parseBoolean(data);
        (new BinarySelectionMessage(getClient().getNickname(), selection)).send();
    }


    @FXML
    public void numPlayers(MouseEvent actionEvent) {
        Node node = (Node) actionEvent.getSource() ;
        String data = (String) node.getUserData();
        int numPlayers = Integer.parseInt(data);
        new NumPlayersMessage(getClient().getNickname(), numPlayers).send();
    }

    @FXML
    public void setFirstCard(Image image){
        firstCard.setImage(image);
    }

}
