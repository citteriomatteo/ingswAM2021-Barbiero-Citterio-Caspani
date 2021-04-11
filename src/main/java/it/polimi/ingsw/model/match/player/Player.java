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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player implements Adder, Verificator {
    private String nickname;
    private boolean connected;
    private Match match;
    private List<LeaderCard> handLeaders;
    private PersonalBoard personalBoard;

    public Player(String nickname, List<LeaderCard> handLeaders) throws NegativeQuantityException {
        this.nickname = nickname;
        this.connected = true;
        this.handLeaders = handLeaders;
        ArrayList<Cell> path = new ArrayList<>();
        List<Integer> wp = Arrays.asList(0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,20);
        List<Integer> vrs = Arrays.asList(0,0,0,0,0,1,1,1,1,0,0,0,2,2,2,2,2,0,0,3,3,3,3,3,3);
        for(int i=0; i<25; i++) {
            if (i == 8 || i == 16 || i == 24)
                path.add(new VaticanReportCell(wp.get(i), vrs.get(i)));
            else
                path.add(new Cell(wp.get(i), vrs.get(i)));
        }

        ArrayList<PhysicalResource> costs = new ArrayList<>();
        costs.add(new PhysicalResource(ResType.UNKNOWN,2));
        ArrayList<Resource> earnings = new ArrayList<>();
        earnings.add(new PhysicalResource(ResType.UNKNOWN,1));
        Production production = new Production(costs,earnings);
        personalBoard = new PersonalBoard(path,0,production);

    }

    //this constructor implements the SingleFaithPath
    public Player(String nickname) throws NegativeQuantityException{
        this.nickname = nickname;
        this.connected = true;
        this.handLeaders = null;
        ArrayList<Cell> path = new ArrayList<>();
        List<Integer> wp = Arrays.asList(0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,20);
        List<Integer> vrs = Arrays.asList(0,0,0,0,0,1,1,1,1,0,0,0,2,2,2,2,2,0,0,3,3,3,3,3,3);
        for(int i=0; i<25; i++) {
            if (i == 8 || i == 16 || i == 24)
                path.add(new VaticanReportCell(wp.get(i), vrs.get(i)));
            else
                path.add(new Cell(wp.get(i), vrs.get(i)));
        }

        ArrayList<PhysicalResource> costs = new ArrayList<>();
        costs.add(new PhysicalResource(ResType.UNKNOWN,2));
        ArrayList<Resource> earnings = new ArrayList<>();
        earnings.add(new PhysicalResource(ResType.UNKNOWN,1));
        Production production = new Production(costs,earnings);
        personalBoard = new PersonalBoard(path,production);

    }


    public void setMatch(Match match) {
        if(this.match == null)
            this.match = match;
        else
            return;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isConnected() {
        return connected;
    }

    public Match getMatch() {
        return match;
    }

    public List<LeaderCard> getHandLeaders() {
        return handLeaders;
    }

    public PersonalBoard getPersonalBoard() {
        return personalBoard;
    }

    @Override
    public boolean addFaithPoints(int quantity) throws FaithPathCreationException, MatchEndedException {
        return personalBoard.getFaithPath().addFaithPoints(quantity,match);
    }

    @Override
    public boolean addToStrongBox(PhysicalResource resource) {
        return personalBoard.getStrongBox().put(resource);
    }

    @Override
    public boolean addToWarehouse(PhysicalResource resource) throws NegativeQuantityException {
        return personalBoard.getWarehouse().marketDraw(resource);
    }

    @Override
    public boolean verifyResources(PhysicalResource physicalResource) {
        int numInWarehouse;
        int numInStrongbox;
        numInWarehouse = personalBoard.getWarehouse().getNumberOf(physicalResource.getType());
        numInStrongbox = personalBoard.getStrongBox().getNumberOf(physicalResource.getType());

        if( numInWarehouse >= physicalResource.getQuantity())
            return true;

        else if (numInStrongbox >= physicalResource.getQuantity())
                return true;

        else if(numInStrongbox + numInWarehouse >= physicalResource.getQuantity())
            return true;

        else
            return false;
    }

    @Override
    public boolean verifyCard(CardType card) {
        return personalBoard.getDevCardSlots().isSatisfied(card);
    }


}
