package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.essentials.CardColor;
import it.polimi.ingsw.model.essentials.CardType;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.essentials.leader.DiscountEffect;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.essentials.leader.SlotEffect;
import it.polimi.ingsw.model.essentials.leader.WhiteMarbleEffect;
import it.polimi.ingsw.exceptions.InvalidQuantityException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderStackTest {
    @Test
    public void drawTest() throws InvalidQuantityException {

        List<LeaderCard> list = populatedList();

        int dim = list.size();
        LeaderStack stack = new LeaderStack(list);

        assertEquals(dim,stack.draw(dim+5).size());
        assertEquals(0, stack.draw(1).size());

        list = populatedList();
        stack = new LeaderStack(list);

        for (int i = 0; i < dim; i++) {
            assertEquals(1, stack.draw(1).size());
        }

        assertEquals(0, stack.draw(1).size());


    }

    private List<LeaderCard> populatedList() throws InvalidQuantityException {
        LeaderCard leader = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2),
                new CardType(CardColor.GREEN, 1, 2))), 5,
                new SlotEffect(new PhysicalResource(ResType.SHIELD, 2)));

        List<LeaderCard> list = new ArrayList<>();

        list.add(leader);
        leader = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1),
                new CardType(CardColor.BLUE, 1, 2))), 5,
                new SlotEffect(new PhysicalResource(ResType.SHIELD, 2)));
        list.add(leader);

        leader = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2),
                new CardType(CardColor.GREEN, 1, 2))), 5,
                new WhiteMarbleEffect(new PhysicalResource(ResType.SHIELD, 2)));
        list.add(leader);

        leader = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2),
                new CardType(CardColor.GREEN, 1, 2))), 5,
                new WhiteMarbleEffect(new PhysicalResource(ResType.SHIELD, 2)));
        list.add(leader);

        leader = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2),
                new CardType(CardColor.GREEN, 1, 2))), 5,
                new SlotEffect(new PhysicalResource(ResType.SHIELD, 2)));
        list.add(leader);

        leader = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2),
                new CardType(CardColor.GREEN, 1, 2))), 5,
                new DiscountEffect(new PhysicalResource(ResType.SHIELD, 3)));
        list.add(leader);

        return list;
    }
}
