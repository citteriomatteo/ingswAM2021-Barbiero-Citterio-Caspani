package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Cli implements View
{

    public Cli(){ }

    @Override
    public void printTitle(){
        StringBuilder str = new StringBuilder();

        str.append(ColorCli.YELLOW_BOLD).append("███╗   ███╗ █████╗ ███████╗████████╗███████╗██████╗ ███████╗     ██████╗ ███████╗    ██████╗ ███████╗███╗   ██╗ █████╗ ██╗███████╗███████╗ █████╗ ███╗   ██╗ ██████╗███████╗\n" +
                "████╗ ████║██╔══██╗██╔════╝╚══██╔══╝██╔════╝██╔══██╗██╔════╝    ██╔═══██╗██╔════╝    ██╔══██╗██╔════╝████╗  ██║██╔══██╗██║██╔════╝██╔════╝██╔══██╗████╗  ██║██╔════╝██╔════╝\n" +
                "██╔████╔██║███████║███████╗   ██║   █████╗  ██████╔╝███████╗    ██║   ██║█████╗      ██████╔╝█████╗  ██╔██╗ ██║███████║██║███████╗███████╗███████║██╔██╗ ██║██║     █████╗  \n" +
                "██║╚██╔╝██║██╔══██║╚════██║   ██║   ██╔══╝  ██╔══██╗╚════██║    ██║   ██║██╔══╝      ██╔══██╗██╔══╝  ██║╚██╗██║██╔══██║██║╚════██║╚════██║██╔══██║██║╚██╗██║██║     ██╔══╝  \n" +
                "██║ ╚═╝ ██║██║  ██║███████║   ██║   ███████╗██║  ██║███████║    ╚██████╔╝██║         ██║  ██║███████╗██║ ╚████║██║  ██║██║███████║███████║██║  ██║██║ ╚████║╚██████╗███████╗\n" +
                "╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝     ╚═════╝ ╚═╝         ╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝╚══════╝\n" +
                "                                                                                                                                                                            \n").append(ColorCli.CLEAR);
        System.out.println(str);
    }

    @Override
    public void drawLoginLayout() {
        System.out.println("Welcome! Login first.");
    }

    @Override
    public void drawReconnectionLayout() {
        System.out.println("sei nel login");
    }

    @Override
    public void drawNewPlayerLayout() {
        System.out.println("Want to play single or multi?");
    }

    @Override
    public void drawNumPlayersLayout() {
        System.out.println("Choose the number of players you want to play with.");
    }

    @Override
    public void drawConfigurationChoice() {
        System.out.println("Want to play with the default configuration?");
    }

    @Override
    public void drawConfigurationLayout() {

    }

    @Override
    public void drawWaitingLayout() {
        System.out.println("Wait for other players ...");
    }

    @Override
    public void drawLeadersChoiceLayout() {
        System.out.println("Choose two Leaders.");
    }

    @Override
    public void drawResourcesChoiceLayout() {
        System.out.println("Choose your resources.");
    }

    @Override
    public void drawYourTurnLayout(boolean yourTurn) {
        if(yourTurn)
            System.out.println("It's your turn. Make a move!");
        else
            System.out.println("Your turn is finished. Wait.");
    }

    @Override
    public void drawWhiteMarbleConversionsLayout() {
        System.out.println("Choose how to convert the white marbles.");
    }

    @Override
    public void drawResPlacementLayout() {
        System.out.println("Place the resources you've got.");
    }

    @Override
    public void drawBuyDevCardLayout() {
        System.out.println("Pay the card you've chosen.");
    }

    @Override
    public void drawPlaceDevCardLayout() {
        System.out.println("Place the card you've chosen.");
    }

    @Override
    public void drawProductionLayout() {
        System.out.println("Produce.");
    }

    @Override
    public void drawEndTurnLayout() {
        System.out.println("Discard/activate a leader, switch some shelves or finish your turn.");
    }

    @Override
    public void printLastRound(){
        System.out.println("It's the last round. Hurry up!");
    }


    @Override
    public void drawEndMatchLayout() {
        System.out.println("Match has ended.");
    }

    @Override
    public void drawRematchOfferLayout(String nickname) {
        System.out.println(nickname + " has offered a rematch. Accept?");
    }

    @Override
    public void printRetry(String errMessage) {
        System.out.println(errMessage);
    }


    @Override
    public void updateMarket(LightMatch match) {
        showAll(match.getPlayersSummary().stream().map((x)->x.getNickname()).collect(Collectors.toList()), match);
    }

    @Override
    public void updateCardGrid(LightMatch match) {
        showAll(match.getPlayersSummary().stream().map((x)->x.getNickname()).collect(Collectors.toList()), match);
    }

    @Override
    public void updateLorenzoMarker(LightMatch match) {
        showAll(match.getPlayersSummary().stream().map((x)->x.getNickname()).collect(Collectors.toList()), match);
    }

    @Override
    public void updateWarehouse(String nickname, LightMatch match) {
        showAll(List.of(nickname), match);
    }

    @Override
    public void updateMarketBuffer(String nickname, LightMatch match) {
        showAll(List.of(nickname), match);
    }

    @Override
    public void updateStrongbox(String nickname, LightMatch match) {
        showAll(List.of(nickname), match);
    }

    @Override
    public void updateFaithMarker(String nickname, LightMatch match) {
        showAll(List.of(nickname), match);
    }

    @Override
    public void updatePopeTiles(String nickname, LightMatch match) {
        showAll(List.of(nickname), match);
    }

    @Override
    public void updateDevCardSlots(String nickname, LightMatch match) {
        showAll(List.of(nickname), match);
    }

    @Override
    public void updateHandLeaders(String nickname, LightMatch match) {
        showAll(List.of(nickname), match);
    }

    @Override
    public void updateActiveLeaders(String nickname, LightMatch match) {
        showAll(List.of(nickname), match);
    }

    @Override
    public void updateWhiteMarbleConversions(String nickname, LightMatch match) {
        showAll(List.of(nickname), match);
    }

    @Override
    public void updateDiscountMap(String nickname, LightMatch match) {
        showAll(List.of(nickname), match);
    }

    @Override
    public void updateTempDevCard(String nickname, LightMatch match) {
        showAll(List.of(nickname), match);
    }

    @Override
    public void printMatchResults(String nickname, Map<String, Integer> ranking){

    }

    @Override
    public void printTokenDraw(String tokenName, int remainingTokens){
        System.out.println("The "+tokenName+" has been drawn: "+remainingTokens+" remaining.");
    }


    //CLI PRINT METHODS OF COMMON THINGS:

    private void showAll(List<String> playersToPrint, LightMatch match){
        showCommonThings(match);
        for(String nickname : playersToPrint)
            showBoard(nickname, match);
    }

    private void showCommonThings(LightMatch match){
        showMarket(match.getMarket(), match.getSideMarble());
        showCardGrid(match.getCardGrid());
    }

    private void showMarket(char[][] market, char sideMarble){
        StringBuilder str = new StringBuilder();
        str.append("\nMARKET STATUS:\n");
        for(int i = 0; i<market.length; i++) {
            for (int j = 0; j < market[i].length; j++) {
                str.append("[ ");
                addColouredMarble(market[i][j], str);
                str.append(" ]");
            }
            if (i == 0) {
                str.append("      [ ");
                addColouredMarble(sideMarble, str);
                str.append(" ]");
            }
            str.append("\n");
        }
        System.out.println(str);
    }

    private void showCardGrid(List<String>[][] cardGrid){
        StringBuilder grid = new StringBuilder();
        System.out.println("\nCARD GRID:");

        //double for to get the max length of ids (for indentation!)
        int spacesToLeave = -1;
        for(int i = 0; i<cardGrid.length; i++)
            for (int j = 0; j < cardGrid[i].length; j++)
                if(cardGrid[i][j].get(0).length() > spacesToLeave)
                    spacesToLeave = cardGrid[i][j].get(0).length();


        for (List<String>[] lists : cardGrid) {
            for (int j = 0; j < lists.length; j++) {
                grid.append("[ " + lists[j].get(0));
                grid.append(" ".repeat(Math.max(0, spacesToLeave - lists[j].get(0).length())));
                grid.append(",").append(lists[j].get(1)).append(" ]");
            }
            grid.append("\n");
        }
        System.out.println(grid);

    }



    //CLI PRINT METHODS OF PLAYER'S THINGS:

    private void showBoard(String nickname, LightMatch match){
        System.out.println("\n ----- PERSONAL BOARD OF "+ nickname + " ----- ");
        showFaithPath(nickname, match);
        showWarehouse(match.getPlayerSummary(nickname));
        showStrongbox(match.getPlayerSummary(nickname));
        showHandLeaders(match.getPlayerSummary(nickname));
        showActiveLeaders(match.getPlayerSummary(nickname));
        if(match.getPlayerSummary(nickname).getTempDevCard() != null)
            showTempDevCard(nickname, match);

    }

    private void showWarehouse(LightPlayer player){
        StringBuilder wh = new StringBuilder();
        wh.append("\nWAREHOUSE:\n");
        for(int i = 0; i < player.getWarehouse().size(); i++) {
            PhysicalResource currRes = player.getWarehouse().get(i);
            wh.append("| ");
            int j;
            for (j = 0; j < currRes.getQuantity(); j++) {
                addColouredResource(currRes, wh);
                for(int numSpaces = 7 - currRes.getType().toString().length(); numSpaces>0; numSpaces--)
                    wh.append(" ");
                wh.append(" |");

            }
            for(int k = j; k < i+1; k++) {
                wh.append(ColorCli.RED).append(" ------ ").append(ColorCli.CLEAR);
                wh.append(" |");
            }
            wh.append("\n");
        }
        System.out.println(wh);

        wh = new StringBuilder();
        wh.append("\nMARKET BUFFER: ");
        if(player.getMarketBuffer().size() == 0)
            wh.append("[").append(ColorCli.RED).append("EMPTY").append(ColorCli.CLEAR).append("]");
        else
            wh.append("\n");

        for(PhysicalResource rb : player.getMarketBuffer()){
            wh.append("| ");
            addColouredResource(rb, wh);
            wh.append(" |\n");
        }
        System.out.println(wh);

    }

    private void showStrongbox(LightPlayer player){
        StringBuilder sb = new StringBuilder();
        sb.append("\nSTRONGBOX:\n");

        //for spaces indentation
        int maxLength = 0;
        for(PhysicalResource currRes : player.getStrongbox()) {
            int partialLength = String.valueOf(currRes.getQuantity()).length();
            if (partialLength > maxLength)
                maxLength = partialLength;
        }

        for(PhysicalResource currRes : player.getStrongbox()){
            sb.append("| ");
            addColouredResource(currRes, sb);
            for(int numSpaces = 7 - currRes.getType().toString().length(); numSpaces>0; numSpaces--)
                sb.append(" ");
            sb.append(" |");

            sb.append(" " + currRes.getQuantity() + " ");

            //insert maxLength-currResQuantity Length spaces
            for(int i = 0; i < (maxLength - String.valueOf(currRes.getQuantity()).length()); i++)
                sb.append(" ");

            sb.append(" |\n");
        }

        System.out.println(sb);


    }

    //TODO: fare bene
    private void showFaithPath(String nickname, LightMatch match){
        System.out.println("\nFAITH PATH:");
        System.out.println("Your position: " + match.getPlayerSummary(nickname).getFaithMarker());
        if(match.getLorenzoMarker() != -1)
            System.out.println("Lorenzo's position: " + match.getLorenzoMarker());
    }

    private void showHandLeaders(LightPlayer player){
        StringBuilder hl = new StringBuilder();
        hl.append("\nHAND LEADERS: [");
        if(player.getHandLeaders().size() == 0)
            hl.append("[").append(ColorCli.RED).append("EMPTY").append(ColorCli.CLEAR).append("]");

        else{
            int i;
            for(i = 0; i < player.getHandLeaders().size()-1; i++)
                hl.append(player.getHandLeaders().get(i)).append(" , ");
            hl.append(player.getHandLeaders().get(i)).append(" ]\n");
        }

        System.out.println(hl);

    }
    private void showActiveLeaders(LightPlayer player){
        StringBuilder al = new StringBuilder();
        al.append("\nACTIVE LEADERS: [ ");
        if(player.getActiveLeaders().size() == 0)
            al.append("[").append(ColorCli.RED).append("EMPTY").append(ColorCli.CLEAR).append("]");

        else{
            int i;
            for(i = 0; i < player.getActiveLeaders().size()-1; i++)
                al.append(player.getActiveLeaders().get(i)).append(" , ");
            al.append(player.getActiveLeaders().get(i)).append(" ]\n");
        }

        System.out.println(al);
    }

    public void showTempDevCard(String nickname, LightMatch match){
        System.out.print("\nDEVELOPMENT CARD TO PAY: [ ");
            System.out.println(match.getPlayerSummary(nickname).getTempDevCard() + " ]");
        System.out.println();
    }

    @Override
    public void drawCard(Card card, String id) {

        if(card.isLeader()){
            System.out.println(" (LEADER CARD " + id + ")");
            System.out.println("|| Requirements -> ");
            for(Requirable r : ((LeaderCard) card).getRequirements())
                System.out.println("|| - " + r.toString() + " ||");
            System.out.println("|| Effect -> " + ((LeaderCard) card).getEffect().toString() + " ||");
            System.out.println("|| Win Points -> " + ((LeaderCard) card).getWinPoints() + " ||");
        }
        else {
            System.out.println(" (DEVELOPMENT CARD " + id + ")");
            System.out.println("|| Price ->");
            for(PhysicalResource r : ((DevelopmentCard) card).getPrice())
                System.out.println("|| - " + r.toString() + " ||");
            System.out.println("|| Production ->");
            System.out.println("|| -  Costs ->");
            for(PhysicalResource r : ((DevelopmentCard) card).getProduction().getCost())
                System.out.println("|| - " + r.toString() + " ||");
            System.out.println("|| -  Costs ->");
            for(Resource r : ((DevelopmentCard) card).getProduction().getEarnings())
                System.out.println("|| - " + r.toString() + " ||");
            System.out.println("|| Win Points -> " + ((DevelopmentCard) card).getWinPoints() + " ||");
        }

    }


    @Override
    public void viewEnemy(String nickname, LightMatch match){
        showBoard(nickname, match);
    }

    private void addColouredResource(PhysicalResource resource, StringBuilder str) {
        switch(resource.getType()){
            case COIN:
                str.append(ColorCli.YELLOW).append(resource.getType()).append(ColorCli.CLEAR);
                break;
            case STONE:
                str.append(ColorCli.WHITE).append(resource.getType()).append(ColorCli.CLEAR);
                break;
            case SHIELD:
                str.append(ColorCli.BLUE).append(resource.getType()).append(ColorCli.CLEAR);
                break;
            case SERVANT:
                str.append(ColorCli.GREEN).append(resource.getType()).append(ColorCli.CLEAR);
                break;
        }
    }

    private void addColouredMarble(char marble, StringBuilder str){
        switch(marble){
            case 'w':
                str.append(ColorCli.WHITE).append("\u2022").append(ColorCli.CLEAR);
                break;
            case 'y':
                str.append(ColorCli.YELLOW).append("\u2022").append(ColorCli.CLEAR);
                break;
            case 'g':
                str.append(ColorCli.CYAN).append("\u2022").append(ColorCli.CLEAR);
                break;
            case 'b':
                str.append(ColorCli.BLUE).append("\u2022").append(ColorCli.CLEAR);
                break;
            case 'r':
                str.append(ColorCli.RED).append("\u2022").append(ColorCli.CLEAR);
                break;
            case 'p':
                str.append(ColorCli.PURPLE).append("\u2022").append(ColorCli.CLEAR);
                break;
        }
    }

}