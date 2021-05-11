package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.essentials.Card;
import it.polimi.ingsw.model.match.player.Player;
import it.polimi.ingsw.observer.ModelObserver;

import java.util.*;

public class CommonThingsTest {

    public Map<String, Card> getCardMap(MatchConfiguration configuration) {
        Map<String, Card> cardMap = new HashMap<>();
        for (int i = 1; i <= configuration.getAllDevCards().size(); i++)
            cardMap.put("D" + i, configuration.getAllDevCards().get(i - 1));
        for (int i = 1; i <= configuration.getAllLeaderCards().size(); i++)
            cardMap.put("L" + i, configuration.getAllLeaderCards().get(i - 1));
        return cardMap;
    }
    public void setSummaries(List<Player> players){
        ModelObserver obs = new Summary(players);
        for(Player p : players)
            p.setSummary(obs);
    }

    public void setSummary(Player player){
        ModelObserver obs = new Summary(new ArrayList<>(List.of(player)));
        player.setSummary(obs);

    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}
