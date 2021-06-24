package it.polimi.ingsw.gameLogic.model.match;

import it.polimi.ingsw.gameLogic.model.essentials.Card;
import it.polimi.ingsw.gameLogic.model.essentials.Production;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import it.polimi.ingsw.gameLogic.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.gameLogic.model.observer.ModelObserver;
import it.polimi.ingsw.jsonUtilities.MyJsonParser;
import it.polimi.ingsw.jsonUtilities.Parser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.*;

public class CommonThingsTest {
    private static final boolean showPrint = false;

    public Map<String, Card> getCardMap(MatchConfiguration configuration) {
        Map<String, Card> cardMap = new HashMap<>();
        for (int i = 1; i <= configuration.getAllDevCards().size(); i++)
            cardMap.put("D" + i, configuration.getAllDevCards().get(i - 1));
        for (int i = 1; i <= configuration.getAllLeaderCards().size(); i++)
            cardMap.put("L" + i, configuration.getAllLeaderCards().get(i - 1));
        return cardMap;
    }
    public void setSummaries(List<Player> players, Map<String, Card> cardMap, List<Cell> faithPath, Production basicProd){
        ModelObserver obs = new Summary(players, cardMap, faithPath, basicProd);
        for(Player p : players)
            p.setSummary(obs);
    }

    public void setSummary(Player player, Map<String, Card> cardMap, List<Cell> faithPath, Production basicProd){
        ModelObserver obs = new Summary(new ArrayList<>(List.of(player)), cardMap, faithPath, basicProd);
        player.setSummary(obs);

    }

    //if static variable ShowPrint is false redirect the output to not show system.out.println on screen
    public void muteOutput() {

        if(!showPrint) {
            try {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                System.setOut(new PrintStream(buffer));
            } catch (Exception ignored) { }
        }
    }

    /**
     * Function used to read the configuration from the json file at 'config' filePath
     * @param config the file path of the configuration file
     * @return the object read in the json
     */
    public MatchConfiguration assignConfiguration(String config){
        Parser parser = MyJsonParser.getParser();
        try {
            FileReader reader = new FileReader(config);
            return parser.readMatchConfiguration(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Application shutdown due to an internal error " );
            System.exit(1);
            return null;
        }
    }
}
