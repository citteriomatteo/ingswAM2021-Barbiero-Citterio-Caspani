package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

import static it.polimi.ingsw.network.client.Client.getClient;

public class JavaFXGUI extends Application {
    private static Scene scene;
    private static Stage stage;
    private static Stage popUpStage;
    private static Scene popUpScene;

    @Override
    public void start(Stage stage) throws IOException {
        // Show the scene containing the root layout.
        JavaFXGUI.stage = stage;
        scene = new Scene(loadFXML(SceneName.LoginScene.name()));
        stage.setScene(scene);
        stage.setResizable(false);
        InputStream imageStream = getClass().getResourceAsStream("images/punchBoard/inkwell.png");
        if(imageStream != null)
            stage.getIcons().add(new Image(imageStream));
        stage.setTitle("Masters of Renaissance Board Game");

        setUpWarningStage();

        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        Parent next = loadFXML(fxml);
        scene.setRoot(next);
        stage.sizeToScene();
    }

    /**
     * Creates an additional stage for warning messages that will show the specific error message on necessity without showing it
     */
    private void setUpWarningStage() throws IOException {
        JavaFXGUI.popUpStage = new Stage();
        popUpScene = new Scene(loadFXML(SceneName.WarningScene.name()));

        popUpStage.setScene(popUpScene);
        popUpStage.setResizable(false);
        InputStream imageStream = JavaFXGUI.class.getResourceAsStream("images/warning.png");
        if(imageStream != null)
            popUpStage.getIcons().add(new Image(imageStream));
        popUpStage.setTitle("Warning");
        popUpStage.setAlwaysOnTop(true);
    }

    /**
     * Shows a pop-up stage for warning messages showing the specific error passed
     * @param error the string you want to show
     */
    static void popUpWarning(String error){
        Pane root = (Pane)popUpScene.getRoot();
        Label label = (Label) SceneProxy.getChildById(root, "errorLabel");
        label.setText(error);

        popUpStage.show();
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
