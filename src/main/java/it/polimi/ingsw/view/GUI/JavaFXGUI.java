package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

import static it.polimi.ingsw.network.client.Client.getClient;

public class JavaFXGUI extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {


        // Show the scene containing the root layout.
        scene = new Scene(loadFXML("loginScene"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("CTRL+f"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/inkwell.png")));
        stage.setTitle("Masters of Renaissance Board Game");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFXGUI.class.getResource("fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() {
        Platform.exit();
        getClient().exit();
        System.exit(0);
    }

}
