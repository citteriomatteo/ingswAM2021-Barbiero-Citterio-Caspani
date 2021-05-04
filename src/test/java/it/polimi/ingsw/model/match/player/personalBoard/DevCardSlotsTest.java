package it.polimi.ingsw.model.match.player.personalBoard;

import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DevCardSlotsTest {

    @Test
    public void pushNewCardTest() throws InvalidQuantityException, NegativeQuantityException {
        DevCardSlots slots = new DevCardSlots();

        DevelopmentCard card = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        Assertions.assertDoesNotThrow(()-> slots.pushNewCard(1, card));


        DevelopmentCard card2 = new DevelopmentCard(new CardType(CardColor.YELLOW, 2),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        Assertions.assertDoesNotThrow(()-> slots.pushNewCard(1, card2));


        DevelopmentCard card3 = new DevelopmentCard(new CardType(CardColor.BLUE, 3),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        Assertions.assertDoesNotThrow(()-> slots.pushNewCard(1, card3));

    }

    @Test
    public void pushNewCardExceptionsTest() throws InvalidQuantityException {
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

        DevelopmentCard card3 = new DevelopmentCard(new CardType(CardColor.GREEN, 3),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        try{
            slots.pushNewCard(1, card2);
            slots.pushNewCard(1, card);
            slots.pushNewCard(1, card3);
        } catch (InvalidOperationException e) {
            fail();
        }
        assertThrows(FullColumnException.class, ()-> slots.pushNewCard(1, card2));
    }

    @Test
    public void getTopTest() throws InvalidQuantityException, NegativeQuantityException {
        DevCardSlots slots = new DevCardSlots();

        DevelopmentCard card = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        Assertions.assertDoesNotThrow(()-> slots.pushNewCard(1, card));


        DevelopmentCard card2 = new DevelopmentCard(new CardType(CardColor.YELLOW, 2),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        Assertions.assertDoesNotThrow(()-> slots.pushNewCard(1, card2));


        DevelopmentCard card3 = new DevelopmentCard(new CardType(CardColor.BLUE, 3),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        Assertions.assertDoesNotThrow(()-> slots.pushNewCard(1, card3));

        DevelopmentCard card4 = new DevelopmentCard(new CardType(CardColor.BLUE, 1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 3))),
                        new ArrayList<>(List.of(new FaithPoint(2)))), 2);

        Assertions.assertDoesNotThrow(()-> slots.pushNewCard(3, card4));

        List<DevelopmentCard> expected = new ArrayList<>();
        expected.add(card3);
        expected.add(card4);

        List<DevelopmentCard> actual = slots.getTop();

        assertIterableEquals(expected, actual);

    }

    @Test
    public void isPlaceable() throws InvalidQuantityException, InvalidOperationException {
        DevCardSlots slots = new DevCardSlots();

        assertTrue(slots.isPlaceable(1));

        DevelopmentCard card = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        //push of a level 1 card
        slots.pushNewCard(1, card);

        //now is placeable a level 2
        assertTrue(slots.isPlaceable(2));

        //push another level 1 on the same column will throw an exception
        assertThrows(HighCardLevelException.class ,()-> slots.pushNewCard(1, card));

        //is placeable only a level 2
        assertTrue(slots.isPlaceable(2));
        assertFalse(slots.isPlaceable(3));

        DevelopmentCard card2 = new DevelopmentCard(new CardType(CardColor.BLUE, 2),
                new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                        new ArrayList<>(List.of(new FaithPoint(1)))), 1);

        //push a level 2 card in the first column
        slots.pushNewCard(1, card2);

        //Now I can't place a level 2 but i can place a level 3
        assertTrue(slots.isPlaceable(3));
        assertFalse(slots.isPlaceable(2));

        //push on the second column a level 1 and a level 2 card
        slots.pushNewCard(2, card);
        slots.pushNewCard(2, card2);

        //Now I can place a level 3 card but not a level 2
        assertTrue(slots.isPlaceable(3));
        assertFalse(slots.isPlaceable(2));
    }

    @Test
    public void getWinPointsTest() {
        DevCardSlots slots = setSituation();
        assertEquals(10, slots.getWinPoints());
    }


    @Test
    public void getCardsNumberTest(){
        DevCardSlots slots = setSituation();
        assertEquals(4, slots.getCardsNumber());
    }

    @Test
    public void isSatisfiedTest() throws InvalidQuantityException {
        DevCardSlots slots = setSituation();
        assertTrue(slots.isSatisfied(new CardType(CardColor.GREEN, 0, 4)));
        assertTrue(slots.isSatisfied(new CardType(CardColor.GREEN, 0, 1)));
        assertTrue(slots.isSatisfied(new CardType(CardColor.GREEN, 2, 1)));
        assertTrue(slots.isSatisfied(new CardType(CardColor.GREEN, 1, 2)));
        assertTrue(slots.isSatisfied(new CardType(CardColor.GREEN, 1, 3)));
        assertFalse(slots.isSatisfied(new CardType(CardColor.BLUE, 0, 1)));
        assertFalse(slots.isSatisfied(new CardType(CardColor.BLUE, 1, 2)));
        assertFalse(slots.isSatisfied(new CardType(CardColor.GREEN, 1, 4)));
        assertFalse(slots.isSatisfied(new CardType(CardColor.GREEN, 0, 5)));
    }

    //used for other tests
    public DevCardSlots setSituation(){
        DevCardSlots slots = new DevCardSlots();
        try {
            DevelopmentCard card = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                    new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                            new ArrayList<>(List.of(new FaithPoint(1)))), 2);

            slots.pushNewCard(1, card);
            slots.pushNewCard(2, card);

            DevelopmentCard card2 = new DevelopmentCard(new CardType(CardColor.GREEN, 2),
                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                    new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                            new ArrayList<>(List.of(new FaithPoint(1)))), 4);

            slots.pushNewCard(1, card2);
            slots.pushNewCard(3, card);

        }catch(Exception e){
            fail();
        }

        return slots;
    }

}
