package it.polimi.ingsw.model.match.player.personalBoard;

import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.exceptions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DevCardSlotsTest {

    @Test
    public void pushNewCardTest() throws InvalidQuantityException, NegativeQuantityException, InvalidAddFaithException {
        DevCardSlots slots = new DevCardSlots();

        DevelopmentCard card = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        assertDoesNotThrow(()-> slots.pushNewCard(1, card));


        DevelopmentCard card2 = new DevelopmentCard(new CardType(CardColor.YELLOW, 2),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        assertDoesNotThrow(()-> slots.pushNewCard(1, card2));


        DevelopmentCard card3 = new DevelopmentCard(new CardType(CardColor.BLUE, 3),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        assertDoesNotThrow(()-> slots.pushNewCard(1, card3));

    }

    @Test
    public void pushNewCardExceptionsTest() throws InvalidQuantityException, NegativeQuantityException, InvalidAddFaithException {
        DevCardSlots slots = new DevCardSlots();

        DevelopmentCard card = new DevelopmentCard(new CardType(CardColor.GREEN, 2),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);


        assertThrows(HighCardLevelException.class, ()-> slots.pushNewCard(1, card));
        assertThrows(InvalidOperationException.class, ()-> slots.pushNewCard(4, card));
        assertThrows(InvalidOperationException.class, ()-> slots.pushNewCard(0, card));


        DevelopmentCard card2 = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        try{
            slots.pushNewCard(1, card2);
            slots.pushNewCard(1, card2);
            slots.pushNewCard(1, card2);
        } catch (InvalidOperationException e) {
            fail();
        }
        assertThrows(FullColumnException.class, ()-> slots.pushNewCard(1, card2));
    }

    @Test
    public void getTopTest() throws InvalidQuantityException, NegativeQuantityException, InvalidAddFaithException {
        DevCardSlots slots = new DevCardSlots();

        DevelopmentCard card = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        assertDoesNotThrow(()-> slots.pushNewCard(1, card));


        DevelopmentCard card2 = new DevelopmentCard(new CardType(CardColor.YELLOW, 2),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        assertDoesNotThrow(()-> slots.pushNewCard(1, card2));


        DevelopmentCard card3 = new DevelopmentCard(new CardType(CardColor.BLUE, 3),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        assertDoesNotThrow(()-> slots.pushNewCard(1, card3));

        DevelopmentCard card4 = new DevelopmentCard(new CardType(CardColor.BLUE, 1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 3))),
                        new ArrayList<>(List.of(new FaithPoint(2)))), 2);

        assertDoesNotThrow(()-> slots.pushNewCard(3, card4));

        List<DevelopmentCard> expected = new ArrayList<>();
        expected.add(card3);
        expected.add(card4);

        List<DevelopmentCard> actual = slots.getTop();

        assertIterableEquals(expected, actual);

    }
}
