package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXML;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        ClientGUI.setRoot("secondary");
    }
}
