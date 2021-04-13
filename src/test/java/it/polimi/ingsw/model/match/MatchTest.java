package it.polimi.ingsw.model.match;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.*;
import it.polimi.ingsw.model.exceptions.InvalidQuantityException;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.FaithPathTest;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.gsonUtilities.GsonHandler.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchTest extends FaithPathTest {

    @Test
    public void JsonTest() {
        /*
        //Set the path where to find the file json
        String filePath = "src/test/resources/Example.json";

        //Build the parser for json file
        Gson g = cellConfig(resourceConfig(requirableConfig(effectConfig(new GsonBuilder())))).setPrettyPrinting().create();

        //%%%%%%%%%%%%%%% Writing on json file %%%%%%%%%%%%%%%%%%
        try (FileWriter writer = new FileWriter(filePath)) {
            LeaderCard slotLeader = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2),
                    new CardType(CardColor.GREEN, 1, 2))), 5,
                    new SlotEffect(new PhysicalResource(ResType.SHIELD, 2)));

            LeaderCard slotLeader2 = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2),
                    new CardType(CardColor.BLUE, 1, 2))), 7,
                    new SlotEffect(new PhysicalResource(ResType.SHIELD, 2)));

            DevelopmentCard devCard = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                    new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                            new ArrayList<>(List.of(new FaithPoint(1)))), 1),
            devCard2 = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                    new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                            new ArrayList<>(List.of(new FaithPoint(1)))), 1);

            ArrayList<Cell> path = generatePath();

            Production prod = new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                    new ArrayList<>(List.of(new FaithPoint(1))));

            MatchConfiguration config = new MatchConfiguration(Arrays.asList(devCard,devCard2),
                    Arrays.asList(slotLeader,slotLeader2), path, prod);



            g.toJson(config, writer);

        } catch (IOException | NegativeQuantityException | InvalidQuantityException | InvalidAddFaithException e) {
            e.printStackTrace();
        }



        String filePath = "src/test/resources/OrderCards.json";
        Gson g = cellConfig(resourceConfig(requirableConfig(effectConfig(new GsonBuilder())))).setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(filePath)){
            DevelopmentCard devCard = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                    new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                            new ArrayList<>(List.of(new FaithPoint(1)))), 1),
                    devCard1 = new DevelopmentCard(new CardType(CardColor.PURPLE, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 3))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,1),new PhysicalResource(ResType.SHIELD,1),new PhysicalResource(ResType.STONE,1)))), 3),
                    devCard2 = new DevelopmentCard(new CardType(CardColor.BLUE, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 3))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,1),new PhysicalResource(ResType.SHIELD,1),new PhysicalResource(ResType.COIN,1)))), 3),
                    devCard3 = new DevelopmentCard(new CardType(CardColor.YELLOW, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 3))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,1),new PhysicalResource(ResType.STONE,1),new PhysicalResource(ResType.COIN,1)))), 3),
                    devCard4 = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 2), new PhysicalResource(ResType.SHIELD,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 1),new PhysicalResource(ResType.SERVANT,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,2),new FaithPoint(1)))), 4),
                    devCard5 = new DevelopmentCard(new CardType(CardColor.PURPLE, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 2),new PhysicalResource(ResType.STONE,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1),new PhysicalResource(ResType.SHIELD,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.STONE,2),new FaithPoint(1)))), 4),
                    devCard6 = new DevelopmentCard(new CardType(CardColor.BLUE, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 2),new PhysicalResource(ResType.SERVANT,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1),new PhysicalResource(ResType.STONE,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,2),new FaithPoint(1)))), 4),
                    devCard7 = new DevelopmentCard(new CardType(CardColor.YELLOW, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 2),new PhysicalResource(ResType.SHIELD,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1),new PhysicalResource(ResType.SERVANT,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,2),new FaithPoint(1)))), 4),
                    devCard8 = new DevelopmentCard(new CardType(CardColor.GREEN, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 4))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 1))),
                                    new ArrayList<>(List.of(new FaithPoint(2)))), 5),
                    devCard9 = new DevelopmentCard(new CardType(CardColor.PURPLE, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 4))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                                    new ArrayList<>(List.of(new FaithPoint(2)))), 5),
                    devCard10 = new DevelopmentCard(new CardType(CardColor.BLUE, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 4))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 1))),
                                    new ArrayList<>(List.of(new FaithPoint(2)))), 5),
                    devCard11 = new DevelopmentCard(new CardType(CardColor.PURPLE, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 1))),
                                    new ArrayList<>(List.of(new FaithPoint(1)))), 1),
                    devCard12 = new DevelopmentCard(new CardType(CardColor.YELLOW, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 4))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1))),
                                    new ArrayList<>(List.of(new FaithPoint(2)))), 5),
                    devCard13 = new DevelopmentCard(new CardType(CardColor.GREEN, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 3),new PhysicalResource(ResType.SERVANT,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1),new PhysicalResource(ResType.SERVANT,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.STONE,3)))), 6),
                    devCard14 = new DevelopmentCard(new CardType(CardColor.PURPLE, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 3),new PhysicalResource(ResType.COIN,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1),new PhysicalResource(ResType.SERVANT,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,3)))), 6),
                    devCard15 = new DevelopmentCard(new CardType(CardColor.BLUE, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 3),new PhysicalResource(ResType.STONE,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1),new PhysicalResource(ResType.STONE,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,3)))), 6),
                    devCard16 = new DevelopmentCard(new CardType(CardColor.YELLOW, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 3),new PhysicalResource(ResType.SHIELD,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 1),new PhysicalResource(ResType.SHIELD,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,3)))), 6),
                    devCard17 = new DevelopmentCard(new CardType(CardColor.GREEN, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 5))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.STONE,2),new FaithPoint(2)))), 7),
                    devCard18 = new DevelopmentCard(new CardType(CardColor.PURPLE, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 5))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,2),new FaithPoint(2)))), 7),
                    devCard19 = new DevelopmentCard(new CardType(CardColor.BLUE, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 5))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,2),new FaithPoint(2)))), 7),
                    devCard20 = new DevelopmentCard(new CardType(CardColor.YELLOW, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 5))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,2),new FaithPoint(2)))), 7),
                    devCard21 = new DevelopmentCard(new CardType(CardColor.GREEN, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 3),new PhysicalResource(ResType.COIN,3))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,2),new FaithPoint(1)))), 8),
                    devCard22 = new DevelopmentCard(new CardType(CardColor.BLUE, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1))),
                                    new ArrayList<>(List.of(new FaithPoint(1)))), 1),
                    devCard23 = new DevelopmentCard(new CardType(CardColor.PURPLE, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 3),new PhysicalResource(ResType.SHIELD,3))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,2),new FaithPoint(1)))), 8),
                    devCard24 = new DevelopmentCard(new CardType(CardColor.BLUE, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 3),new PhysicalResource(ResType.STONE,3))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.STONE,2),new FaithPoint(1)))), 8),
                    devCard25 = new DevelopmentCard(new CardType(CardColor.YELLOW, 2),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 3),new PhysicalResource(ResType.SERVANT,3))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,2),new FaithPoint(1)))), 8),
                    devCard26 = new DevelopmentCard(new CardType(CardColor.GREEN, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 6))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.STONE,3),new FaithPoint(2)))), 9),
                    devCard27 = new DevelopmentCard(new CardType(CardColor.PURPLE, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 6))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,3),new FaithPoint(2)))), 9),
                    devCard28 = new DevelopmentCard(new CardType(CardColor.BLUE, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 6))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,3),new FaithPoint(2)))), 9),
                    devCard29 = new DevelopmentCard(new CardType(CardColor.YELLOW, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 6))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,3),new FaithPoint(2)))), 9),
                    devCard30 = new DevelopmentCard(new CardType(CardColor.GREEN, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 5),new PhysicalResource(ResType.SERVANT,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1),new PhysicalResource(ResType.SERVANT,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,2),new PhysicalResource(ResType.STONE,2), new FaithPoint(1)))), 10),
                    devCard31 = new DevelopmentCard(new CardType(CardColor.PURPLE, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 5),new PhysicalResource(ResType.COIN,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 1),new PhysicalResource(ResType.SHIELD,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,2),new PhysicalResource(ResType.SERVANT,2),new FaithPoint(1)))), 10),
                    devCard32 = new DevelopmentCard(new CardType(CardColor.BLUE, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 5),new PhysicalResource(ResType.STONE,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1), new PhysicalResource(ResType.SHIELD,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,2),new PhysicalResource(ResType.STONE,2), new FaithPoint(1)))), 10),
                    devCard33 = new DevelopmentCard(new CardType(CardColor.YELLOW, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 1))),
                                    new ArrayList<>(List.of(new FaithPoint(1)))), 1),
                    devCard34 = new DevelopmentCard(new CardType(CardColor.YELLOW, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 5),new PhysicalResource(ResType.SERVANT,2))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 1),new PhysicalResource(ResType.SERVANT,1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,2),new PhysicalResource(ResType.SHIELD,2), new FaithPoint(1)))), 10),
                    devCard35 = new DevelopmentCard(new CardType(CardColor.GREEN, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 7))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,1),new FaithPoint(3)))), 11),
                    devCard36 = new DevelopmentCard(new CardType(CardColor.PURPLE, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 7))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.STONE,1),new FaithPoint(3)))), 11),
                    devCard37 = new DevelopmentCard(new CardType(CardColor.BLUE, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 7))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,1),new FaithPoint(3)))), 11),
                    devCard38 = new DevelopmentCard(new CardType(CardColor.YELLOW, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 7))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,1),new FaithPoint(3)))), 11),
                    devCard39 = new DevelopmentCard(new CardType(CardColor.GREEN, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 4),new PhysicalResource(ResType.COIN,4))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,3),new PhysicalResource(ResType.SHIELD,1)))), 12),
                    devCard40 = new DevelopmentCard(new CardType(CardColor.PURPLE, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 4),new PhysicalResource(ResType.SHIELD,4))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.STONE,3),new PhysicalResource(ResType.SERVANT,1)))), 12),
                    devCard41 = new DevelopmentCard(new CardType(CardColor.BLUE, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 4),new PhysicalResource(ResType.STONE,4))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,1),new PhysicalResource(ResType.SHIELD,3)))), 12),
                    devCard42 = new DevelopmentCard(new CardType(CardColor.YELLOW, 3),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 4),new PhysicalResource(ResType.SERVANT,4))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.STONE,1),new PhysicalResource(ResType.SERVANT,3)))), 12),
                    devCard43 = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1),new PhysicalResource(ResType.SERVANT,1),new PhysicalResource(ResType.STONE,1))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,1)))),2),
                    devCard44 = new DevelopmentCard(new CardType(CardColor.PURPLE, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1),new PhysicalResource(ResType.SERVANT,1),new PhysicalResource(ResType.COIN,1))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,1)))),2),
                    devCard45 = new DevelopmentCard(new CardType(CardColor.BLUE, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.COIN, 1),new PhysicalResource(ResType.SERVANT,1),new PhysicalResource(ResType.STONE,1))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.STONE,1)))),2),
                    devCard46 = new DevelopmentCard(new CardType(CardColor.YELLOW, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1),new PhysicalResource(ResType.STONE,1),new PhysicalResource(ResType.COIN,1))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 1))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,1)))),2),
                    devCard47 = new DevelopmentCard(new CardType(CardColor.GREEN, 1),
                            new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD, 3))),
                            new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT, 2))),
                                    new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,1),new PhysicalResource(ResType.SHIELD,1),new PhysicalResource(ResType.STONE,1)))),3);

            LeaderCard leaderCard = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.YELLOW,0,1),
                            new CardType(CardColor.GREEN, 0,1))), 2,
                            new DiscountEffect(new PhysicalResource(ResType.SERVANT, 1))),
                        leaderCard1 = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.BLUE,0,1),
                                new CardType(CardColor.PURPLE, 0,1))), 2,
                                new DiscountEffect(new PhysicalResource(ResType.SHIELD, 1))),
                        leaderCard2 = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.GREEN,0,1),
                                new CardType(CardColor.BLUE, 0,1))), 2,
                                new DiscountEffect(new PhysicalResource(ResType.STONE, 1))),
                        leaderCard3 = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.YELLOW,0,1),
                                new CardType(CardColor.PURPLE, 0,1))), 2,
                                new DiscountEffect(new PhysicalResource(ResType.COIN, 1))),
                        leaderCard4 = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,5))),3,
                                new SlotEffect(new PhysicalResource(ResType.STONE, 2))),
                        leaderCard5 = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE,5))),3,
                                new SlotEffect(new PhysicalResource(ResType.SERVANT, 2))),
                        leaderCard6 = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,5))),3,
                                new SlotEffect(new PhysicalResource(ResType.SHIELD, 2))),
                        leaderCard7 = new LeaderCard(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,5))),3,
                                new SlotEffect(new PhysicalResource(ResType.COIN, 2))),
                        leaderCard8 = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.YELLOW,0,2),
                                new CardType(CardColor.BLUE,0,1))),5,
                                new WhiteMarbleEffect(new PhysicalResource(ResType.SERVANT,1))),
                        leaderCard9 = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.GREEN,0,2),
                                new CardType(CardColor.PURPLE,0,1))),5,
                                new WhiteMarbleEffect(new PhysicalResource(ResType.SHIELD,1))),
                        leaderCard10 = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.BLUE,0,2),
                                new CardType(CardColor.YELLOW,0,1))),5,
                                new WhiteMarbleEffect(new PhysicalResource(ResType.STONE,1))),
                        leaderCard11 = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.BLUE,0,2),
                                new CardType(CardColor.GREEN,0,1))),5,
                                new WhiteMarbleEffect(new PhysicalResource(ResType.COIN,1))),
                        leaderCard12 = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.YELLOW,2,1))),4,
                                new ProductionEffect(new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SHIELD,1))),
                                        new ArrayList<>(List.of(new PhysicalResource(ResType.UNKNOWN,1),new FaithPoint(1)))))),
                        leaderCard13 = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.BLUE,2,1))),4,
                                new ProductionEffect(new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.SERVANT,1))),
                                        new ArrayList<>(List.of(new PhysicalResource(ResType.UNKNOWN,1),new FaithPoint(1)))))),
                        leaderCard14 = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.PURPLE,2,1))),4,
                                new ProductionEffect(new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.STONE,1))),
                                        new ArrayList<>(List.of(new PhysicalResource(ResType.UNKNOWN,1),new FaithPoint(1)))))),
                        leaderCard15 = new LeaderCard(new ArrayList<>(List.of(new CardType(CardColor.GREEN,2,1))),4,
                                new ProductionEffect(new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.COIN,1))),
                                        new ArrayList<>(List.of(new PhysicalResource(ResType.UNKNOWN,1),new FaithPoint(1))))));

            ArrayList<Cell> path = generatePath();

            Production prod = new Production(new ArrayList<>(List.of(new PhysicalResource(ResType.UNKNOWN, 1),new PhysicalResource(ResType.UNKNOWN,1))),
                    new ArrayList<>(List.of(new PhysicalResource(ResType.UNKNOWN,1))));

            MatchConfiguration config = new MatchConfiguration(Arrays.asList(devCard,devCard1,devCard2,devCard3,devCard4,devCard5,devCard6,devCard7,devCard8,devCard9,
                    devCard10,devCard11,devCard12,devCard13,devCard14,devCard15,devCard16,devCard17,devCard18,devCard19,devCard20,devCard21,devCard22,devCard23,devCard24,
                    devCard25,devCard26,devCard27,devCard28,devCard29,devCard30,devCard31,devCard32,devCard33,devCard34,devCard35,devCard36,devCard37,devCard38,devCard39,
                    devCard40,devCard41,devCard42,devCard43,devCard44,devCard45,devCard46,devCard47),
                    Arrays.asList(leaderCard,leaderCard1,leaderCard2,leaderCard3,leaderCard4,leaderCard5,leaderCard6,leaderCard7,leaderCard8,leaderCard9,leaderCard10,
                            leaderCard11,leaderCard12,leaderCard13,leaderCard14,leaderCard15), path, prod);
            Collections.sort(config.getAllDevCards(),new CardComparator());
            g.toJson(config, writer);






        } catch (IOException | NegativeQuantityException | InvalidQuantityException | InvalidAddFaithException e) {
            e.printStackTrace();
        }
        //%%%%%%%%%%%%% Reading from json file &&&&&&&&&&&&&&&&&&
        //open the file
       try (FileReader reader = new FileReader(filePath)) {
            //parse the object and instantiate it
            LeaderCard extractedJson = g.fromJson(reader, LeaderCard.class);

//            System.out.println(extractedJson);
            assertEquals(extractedJson, slotLeader);

        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }
}
