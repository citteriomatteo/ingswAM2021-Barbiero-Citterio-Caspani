package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.essentials.ResType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.Map;

import static it.polimi.ingsw.view.GUI.SceneProxy.getSceneProxy;

public class StartingPhaseSceneController {
    private Map<ResType, Integer> resourceCounts;
    public Label servantCount;

    public StartingPhaseSceneController() {
        getSceneProxy().setStartingPhaseSceneController(this);
    }

    public void addServant() {
    }
}
