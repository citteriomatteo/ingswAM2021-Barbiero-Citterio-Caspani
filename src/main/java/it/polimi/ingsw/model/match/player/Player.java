package it.polimi.ingsw.model.match.player;

import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.exceptions.FaithPathCreationException;
import it.polimi.ingsw.model.exceptions.MatchEndedException;
import it.polimi.ingsw.model.exceptions.NegativeQuantityException;
import it.polimi.ingsw.model.match.Match;
import it.polimi.ingsw.model.match.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.Cell;
import it.polimi.ingsw.model.match.player.personalBoard.faithPath.VaticanReportCell;

import java.util.*;

public class Player implements Adder, Verificator
{
    private final String nickname;
    private boolean connected;
    private Match match;
    private List<LeaderCard> handLeaders;
    private PersonalBoard personalBoard;
    private Production tempProduction;

    public Player(String nickname)
    {
        this.nickname = nickname;
        connected = true;
    }

    //ALL SETTERS:
    public boolean setHandLeaders(ArrayList<LeaderCard> handLeaders)
    {
        if(this.handLeaders == null)
        {
            this.handLeaders = handLeaders;
            return true;
        }
        return false;
    }

    public boolean setMatch(Match match) throws NegativeQuantityException
    {
        if (this.match == null)
        {
            this.match = match;
            return true;
        }
        return false;
    }

    public boolean setPersonalBoard(PersonalBoard personalBoard) {
        if (match != null) {
            this.personalBoard = personalBoard;
            return true;
        }
        return false;
    }

    //ALL GETTERS:
    public Match getMatch() { return match; }
    public String getNickname() { return nickname; }
    public boolean isConnected() { return connected; }
    public List<LeaderCard> getHandLeaders() { return handLeaders; }
    public PersonalBoard getPersonalBoard() { return personalBoard; }
    public List<PhysicalResource> getWhiteMarbleConversions() { return getPersonalBoard().getWhiteMarbleConversions(); }


    //"ADDER" INTERFACE METHODS:
    @Override
    public boolean addFaithPoints(int quantity) throws FaithPathCreationException, MatchEndedException
    {
        return personalBoard.getFaithPath().addFaithPoints(quantity, match);
    }

    @Override
    public boolean addToStrongBox(PhysicalResource resource) {
        return personalBoard.getStrongBox().put(resource);
    }

    @Override
    public boolean addToWarehouse(PhysicalResource resource) throws NegativeQuantityException {
        return personalBoard.getWarehouse().marketDraw(resource);
    }

    //"VERIFICATOR" INTERFACE METHODS:
    @Override
    public boolean verifyResources(PhysicalResource physicalResource) {
        int numInWarehouse;
        int numInStrongbox;
        numInWarehouse = personalBoard.getWarehouse().getNumberOf(physicalResource.getType());
        numInStrongbox = personalBoard.getStrongBox().getNumberOf(physicalResource.getType());

        if (numInWarehouse >= physicalResource.getQuantity())
            return true;

        else if (numInStrongbox >= physicalResource.getQuantity())
            return true;

        else if (numInStrongbox + numInWarehouse >= physicalResource.getQuantity())
            return true;

        else
            return false;
    }

    @Override
    public boolean verifyCard(CardType card) {
        return personalBoard.getDevCardSlots().isSatisfied(card);
    }

    @Override
    public boolean verifyPlaceability(int cardLevel)
    {
        return personalBoard.getDevCardSlots().isPlaceable(cardLevel);
    }

    //OTHER METHODS:
    public boolean connect() {connected=true; return true;}
    public boolean disconnect() {connected=false; return true;}

    public boolean leadersChoice(ArrayList<LeaderCard> choseLeaders)
    {
        //TODO
        return true;
    }

    public boolean activateLeader(LeaderCard leader)
    {
        //TODO
        return true;
    }

    public boolean discardUselessLeader(LeaderCard leader)
    {
        //TODO
        return true;
    }

    //this method does a market action and returns the number of white marbles.
    public int marketDeal(boolean row, int number)
    {
        //TODO
        return 0;
    }

    public boolean takeDevelopmentCard(int gridR, int gridC, int slot)
    {
        //TODO
        return true;
    }

    public boolean isProducible(Production production)
    {
        //TODO
        return true;
    }

    public boolean payFromWarehouse(int shelf, PhysicalResource res)
    {
        //TODO
        return true;
    }

    public boolean payFromStrongbox(PhysicalResource res)
    {
        //TODO
        return true;
    }

    public boolean produce()
    {
        //TODO
        return true;
    }

    public int totalWinPoints()
    {
        //TODO
        return 0;
    }

}
