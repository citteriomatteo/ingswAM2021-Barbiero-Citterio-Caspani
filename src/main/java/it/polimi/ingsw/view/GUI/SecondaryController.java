package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXML;

import java.io.IOException;

public class SecondaryController {
    @FXML
    private void switchToPrimary() throws IOException {
        JavaFXGUI.setRoot("primary");
    }
}
