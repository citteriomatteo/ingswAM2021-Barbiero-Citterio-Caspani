package it.polimi.ingsw.gameLogic.model.match;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.gameLogic.model.match.MatchConfiguration.assignConfiguration;
import static org.junit.jupiter.api.Assertions.*;

public class MatchConfigurationTest {

    @Test
    public void settingConfiguration(){
        MatchConfiguration configuration = assignConfiguration("src/test/resources/TotalFreeConfiguration.json");

        MatchConfiguration configuration2 = new MatchConfiguration();

        configuration2.setAllDevCards(configuration.getAllDevCards());
        configuration2.setAllLeaderCards(configuration.getAllLeaderCards());
        configuration2.setBasicProduction(configuration.getBasicProduction());
        configuration2.setCustomPath(configuration.getCustomPath());

        MatchConfiguration configuration3 = new MatchConfiguration(configuration.getAllDevCards(), configuration.getAllLeaderCards(), configuration.getCustomPath(), configuration.getBasicProduction());

        assertEquals(configuration, configuration3);
        assertEquals(configuration, configuration2);
    }
}
