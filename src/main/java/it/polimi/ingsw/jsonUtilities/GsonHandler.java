package it.polimi.ingsw.jsonUtilities;

import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.*;
import it.polimi.ingsw.model.match.market.*;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.VaticanReportCell;
import it.polimi.ingsw.network.message.ctosmessage.*;
import it.polimi.ingsw.network.message.ctosmessage.BinarySelectionMessage;
import it.polimi.ingsw.network.message.stocmessage.*;

/**
 * Class that expose static methods used for better build the GsonBuilder,
 * to create the Gson object just call on the returned object .create()
 * @see GsonBuilder
 */
public class GsonHandler {
    /**
     * @param builder a generic GsonBuilder
     * @return a gson builder ready to parse an object that implements Resource Interface
     */
    public static GsonBuilder resourceConfig(GsonBuilder builder){

        return builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                .of(Resource.class, "resource")
                .registerSubtype(PhysicalResource.class, "physicalResource")
                .registerSubtype(FaithPoint.class, "faithPoint"));
    }

    /**
     * @param builder a generic GsonBuilder
     * @return a gson builder ready to parse an object that could be a Cell or a subclass
     */
    public static GsonBuilder cellConfig(GsonBuilder builder){

        return builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                .of(Cell.class, "Cell")
                .registerSubtype(VaticanReportCell.class, "vaticanReportCell")
                .registerSubtype(Cell.class, "cell"));
    }

    /**
     * @param builder a generic GsonBuilder
     * @return a gson builder ready to parse an object that implements Effect Interface
     */
    public static GsonBuilder effectConfig(GsonBuilder builder){

        return builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                .of(Effect.class, "name")
                .registerSubtype(DiscountEffect.class, "discountEffect")
                .registerSubtype(ProductionEffect.class, "productionEffect")
                .registerSubtype(SlotEffect.class, "slotEffect")
                .registerSubtype(WhiteMarbleEffect.class, "whiteMarbleEffect"));
    }

    /**
     * @param builder a generic GsonBuilder
     * @return a gson builder ready to parse an object that implements Requirable Interface
     */
    public static GsonBuilder requirableConfig(GsonBuilder builder){

        return builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                .of(Requirable.class, "requirement")
                .registerSubtype(PhysicalResource.class, "physicalResource")
                .registerSubtype(CardType.class, "cardType"));
    }

    /**
     * @param builder a generic GsonBuilder
     * @return a gson builder ready to parse an object that extends Marble abstract object
     */
    public static GsonBuilder marbleConfig(GsonBuilder builder){

        return builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                .of(Marble.class, "color")
                .registerSubtype(BlueMarble.class, "blue")
                .registerSubtype(GreyMarble.class, "grey")
                .registerSubtype(PurpleMarble.class, "purple")
                .registerSubtype(RedMarble.class, "red")
                .registerSubtype(WhiteMarble.class, "white")
                .registerSubtype(YellowMarble.class, "yellow"));
    }

    /**
     * @param builder a generic GsonBuilder
     * @return a gson builder ready to parse every object of the Model infrastructure of this game
     */
    public static GsonBuilder completeModelConfig(GsonBuilder builder){

        return marbleConfig(cellConfig(resourceConfig(requirableConfig(effectConfig(builder)))));
    }

    /**
     * returns a gson builder ready to parse a Message object from Client to Server.
     * Remember to use toJson() with one extra parameter indicating the class Message when you are serializing a message.
     *
     * @param builder a generic GsonBuilder
     * @return a gson builder ready to parse a Message object from Client to Server
     */
    public static GsonBuilder cToSMessageConfig(GsonBuilder builder){

        GsonBuilder res = builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                        .of(CtoSMessage.class, "Message")
                        .registerSubtype(BinarySelectionMessage.class, "binarySelection")
                        .registerSubtype(ConfigureMessage.class, "configuration")
                        .registerSubtype(DevCardDrawMessage.class, "devCardDraw")
                        .registerSubtype(DevCardPlacementMessage.class, "devCardPlacement")
                        .registerSubtype(EndTurnMessage.class, "endTurn")
                        .registerSubtype(LeaderActivationMessage.class, "leaderActivation")
                        .registerSubtype(LeaderDiscardingMessage.class, "leaderDiscarding")
                        .registerSubtype(LeadersChoiceMessage.class, "leadersChoice")
                        .registerSubtype(LoginMessage.class, "login")
                        .registerSubtype(MarketDrawMessage.class, "marketDraw")
                        .registerSubtype(NumPlayersMessage.class, "numPlayers")
                        .registerSubtype(PaymentsMessage.class, "payments")
                        .registerSubtype(ProductionMessage.class, "production")
                        .registerSubtype(RematchMessage.class, "rematch")
                        .registerSubtype(StartingResourcesMessage.class, "startingResource")
                        .registerSubtype(SwitchShelfMessage.class, "switchShelf")
                        .registerSubtype(WarehouseInsertionMessage.class, "warehouseInsertion")
                        .registerSubtype(WhiteMarblesConversionMessage.class, "whiteMarblesConversion"));
        return completeModelConfig(res);
    }

    /**
     * returns a gson builder ready to parse a Message object from Server to Client.
     * Remember to use toJson() with one extra parameter indicating the class Message when you are serializing a message.
     * @param builder a generic GsonBuilder
     * @return a gson builder ready to parse a Message object from Server to Client
     */
    public static GsonBuilder sToCMessageConfig(GsonBuilder builder){

        GsonBuilder res = builder.registerTypeAdapterFactory(RuntimeTypeAdapterFactory
                        .of(StoCMessage.class, "Message")
                        .registerSubtype(it.polimi.ingsw.network.message.stocmessage.BinarySelectionMessage.class, "binarySelection")
                        .registerSubtype(SummaryMessage.class, "Summary")
                        .registerSubtype(CardGridChangeMessage.class, "cardGridChange")
                        .registerSubtype(DevCardSlotChangeMessage.class, "devCardSlotChange")
                        .registerSubtype(DevCardDrawnMessage.class, "DevCardDrawn")
                        .registerSubtype(HandLeadersStateMessage.class, "HandLeadersState")
                        .registerSubtype(ActivatedLeaderMessage.class, "activatedLeader")
                        .registerSubtype(UpdatedDiscountMapMessage.class, "updatedDiscountMap")
                        .registerSubtype(WhiteMarbleConversionMessage.class, "newWhiteMarbleConversion")
                        .registerSubtype(DiscardedLeaderMessage.class, "discardedLeader")
                        .registerSubtype(EndGameResultsMessage.class, "endGameResults")
                        .registerSubtype(MarketChangeMessage.class, "marketChange")
                        .registerSubtype(NewFaithPositionMessage.class, "newFaithPosition")
                        .registerSubtype(NextStateMessage.class, "nextState")
                        .registerSubtype(RematchOfferedMessage.class, "rematchOffered")
                        .registerSubtype(RetryMessage.class, "retry")
                        .registerSubtype(StrongboxChangeMessage.class, "strongboxChange")
                        .registerSubtype(TokenDrawMessage.class, "tokenDraw")
                        .registerSubtype(LastRoundMessage.class, "LastRound")
                        .registerSubtype(VaticanReportMessage.class, "vaticanReport")
                        .registerSubtype(WarehouseChangeMessage.class, "warehouseChange")
                        .registerSubtype(MarketBufferChangeMessage.class, "marketBufferChange")
                        .registerSubtype(GoodbyeMessage.class, "Goodbye"));

        return marbleConfig(res);
    }

}
