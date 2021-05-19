package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Cli implements View
{

    private String lastLayout;
    private static final Integer DEFAULT_SHIFT = 5, ZERO_SHIFT = 0;

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
        lastLayout = "Welcome! Login first.";
        System.out.println(lastLayout);
    }

    @Override
    public void drawReconnectionLayout() {
        lastLayout = "There's already a disconnected player with this nickname. Is it you?";
        System.out.println(lastLayout);
    }

    @Override
    public void drawNewPlayerLayout() {
        lastLayout = "Want to play single or multi?";
        System.out.println(lastLayout);
    }

    @Override
    public void drawNumPlayersLayout() {
        lastLayout = "Choose the number of players you want to play with.";
        System.out.println(lastLayout);
    }

    @Override
    public void drawConfigurationChoice() {
        lastLayout = "Want to play with the default configuration?";
        System.out.println(lastLayout);
    }

    @Override
    public void drawConfigurationLayout() {
        //TODO
    }

    @Override
    public void drawWaitingLayout() {
        lastLayout = "Wait for other players ...";
    }

    @Override
    public void drawLeadersChoiceLayout() {
        lastLayout = "Choose two Leaders.";
        System.out.println(lastLayout);
    }

    @Override
    public void drawResourcesChoiceLayout() {
        lastLayout = "Choose your resources.";
        System.out.println(lastLayout);
    }

    @Override
    public void drawYourTurnLayout(boolean yourTurn) {
        if(yourTurn)
            lastLayout = "It's your turn. Make a move!";
        else
            lastLayout = "Your turn is finished. Wait.";
        System.out.println(lastLayout);
    }

    @Override
    public void drawWhiteMarbleConversionsLayout() {
        lastLayout = "Choose how to convert the white marbles.";
        System.out.println(lastLayout);
    }

    @Override
    public void drawResPlacementLayout() {
        lastLayout = "Place the resources you've got.";
        System.out.println(lastLayout);
    }

    @Override
    public void drawBuyDevCardLayout() {
        lastLayout = "Pay the card you've chosen.";
        System.out.println(lastLayout);
    }

    @Override
    public void drawPlaceDevCardLayout() {
        lastLayout = "Place the card you've chosen.";
        System.out.println(lastLayout);
    }

    @Override
    public void drawProductionLayout() {
        lastLayout = "Produce.";
        System.out.println(lastLayout);
    }

    @Override
    public void drawEndTurnLayout() {
        lastLayout = "Discard/activate a leader, switch some shelves or finish your turn.";
        System.out.println(lastLayout);
    }

    @Override
    public void printLastRound(){
        lastLayout = "It's the last round. Hurry up!";
        System.out.println(lastLayout);
    }


    @Override
    public void drawEndMatchLayout() {
        lastLayout = "Match has ended.";
        System.out.println(lastLayout);
    }

    @Override
    public void drawRematchOfferLayout(String nickname) {
        lastLayout = nickname + " has offered a rematch. Accept?";
        System.out.println(lastLayout);
    }

    @Override
    public void printRetry(String errMessage) {
        lastLayout = errMessage;
        System.out.println(lastLayout);
    }


    @Override
    public void updateMatch(LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateMarket(LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateCardGrid(LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateLorenzoMarker(LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateWarehouse(String nickname, LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateMarketBuffer(String nickname, LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateStrongbox(String nickname, LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateFaithMarker(String nickname, LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updatePopeTiles(String nickname, LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateDevCardSlots(String nickname, LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateHandLeaders(String nickname, LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateActiveLeaders(String nickname, LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateWhiteMarbleConversions(String nickname, LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateDiscountMap(String nickname, LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
    }

    @Override
    public void updateTempDevCard(String nickname, LightMatch match) {
        showAll(match.getPlayersSummary().stream().map(LightPlayer::getNickname).collect(Collectors.toList()), match);
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
        clearScreen();

        StringBuilder div = new StringBuilder();
        for(int i = 0; i < 400; i++)
            div.append("═");
        div.append("\n");

        System.out.println(div);

        showCommonThings(match);

        System.out.println(div);

        List<StringBuilder> boards = new ArrayList<>();
        for(String nickname : playersToPrint) {
            StringBuilder board = new StringBuilder();
            boards.add(board);
            showBoard(nickname, match, board);
        }
        StringBuilder allBoardsLined = new StringBuilder();
        Integer max = 0;
        for(StringBuilder sb : boards)
            allBoardsLined = new StringBuilder(mergePrintingObjects(allBoardsLined, sb, max, DEFAULT_SHIFT));

        System.out.println(allBoardsLined + "\n");

        System.out.println(div);

    }

    private void showCommonThings(LightMatch match){
        StringBuilder marketGraphic = new StringBuilder();
        showMarket(match.getMarket(), match.getSideMarble(), marketGraphic);

        StringBuilder gridGraphic = new StringBuilder();
        showCardGrid(match.getCardGrid(), gridGraphic);

        System.out.println(mergePrintingObjects(marketGraphic, gridGraphic, 0, DEFAULT_SHIFT));

        StringBuilder pathGraphic = new StringBuilder();
        showFaithPath(match, pathGraphic);

        for(LightPlayer p : match.getPlayersSummary()) {
            StringBuilder tilesGraphic = new StringBuilder();
            showPopeTiles(p, tilesGraphic);
            System.out.println(tilesGraphic);
        }

    }

    private void showMarket(char[][] market, char sideMarble, StringBuilder marketGraphic){
        StringBuilder str = new StringBuilder();
        str.append("\nMARKET STATUS:\n");
        for(int i = 0; i<market.length; i++) {
            for (int j = 0; j < market[i].length; j++) {
                str.append("[ ");
                addColouredMarble(market[i][j], str);
                str.append(" ]");
            }
            if (i == 0) {
                str.append("   [ ");
                addColouredMarble(sideMarble, str);
                str.append(" ]");
            }
            str.append("\n");
        }
        marketGraphic.append(str);
    }

    private void showCardGrid(List<String>[][] cardGrid, StringBuilder gridGraphic){
        StringBuilder grid = new StringBuilder();
        grid.append("\nCARD GRID:\n");

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
        gridGraphic.append(grid);
    }

    private void showFaithPath(LightMatch match, StringBuilder pathGraphic){
        System.out.println("\nFAITH PATH:\n");
        for(int i = 0; i < match.getFaithPath().size(); i++)
            pathGraphic = mergePrintingObjects(pathGraphic, getPrintedCell(match, i, match.getFaithPath().get(i)), 0, ZERO_SHIFT);
        System.out.println(pathGraphic);
    }

    //CLI PRINT METHODS OF PLAYER'S THINGS:

    private void showBoard(String nickname, LightMatch match, StringBuilder board){
        board.append("\n----- PERSONAL BOARD OF ").append(match.getPlayerSummary(nickname).getColor()).append(nickname).append(ColorCli.CLEAR);
        if(!match.getPlayerSummary(nickname).isConnected())
            board.append(ColorCli.RED).append(" (OFFLINE) ").append(ColorCli.CLEAR);
        board.append(" ----- \n");
        showDevCardSlots(match.getPlayerSummary(nickname), board);
        showWarehouse(match.getPlayerSummary(nickname), board);
        showStrongbox(match.getPlayerSummary(nickname),board);
        showHandLeaders(match.getPlayerSummary(nickname), board);
        showActiveLeaders(match.getPlayerSummary(nickname), board);
        if(match.getPlayerSummary(nickname).getTempDevCard() != null)
            showTempDevCard(nickname, match, board);

    }

    private void showWarehouse(LightPlayer player, StringBuilder board){
        StringBuilder wh = new StringBuilder();
        wh.append("\nWAREHOUSE:\n");
        for(int i = 0; i < player.getWarehouse().size(); i++) {
            PhysicalResource currRes = player.getWarehouse().get(i);
            wh.append("| ");
            int j;
            for (j = 0; j < currRes.getQuantity(); j++) {
                addColouredResource(currRes, wh);
                putSomeDistance(wh, 7 - currRes.getType().toString().length());
                wh.append(" |");

            }
            for(int k = j; k < i+1; k++) {
                wh.append(ColorCli.RED).append(" ------ ").append(ColorCli.CLEAR);
                wh.append(" |");
            }
            wh.append("\n");
        }
        board.append(wh);

        wh = new StringBuilder();
        wh.append("\nMARKET BUFFER: ");
        if(player.getMarketBuffer().size() == 0)
            wh.append("[").append(ColorCli.RED).append("EMPTY").append(ColorCli.CLEAR).append("]\n");
        else
            wh.append("\n");

        for(PhysicalResource rb : player.getMarketBuffer()){
            wh.append("| ");
            addColouredResource(rb, wh);
            wh.append(" |\n");
        }
        board.append(wh);

    }

    private void showStrongbox(LightPlayer player, StringBuilder board){
        StringBuilder sb = new StringBuilder();
        sb.append(" STRONGBOX:\n");

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
            putSomeDistance(sb, 7 - currRes.getType().toString().length());
            sb.append(" | ");

            sb.append(" ").append(currRes.getQuantity()).append(" ");

            //insert maxLength-currResQuantity Length spaces
            putSomeDistance(sb, (maxLength - String.valueOf(currRes.getQuantity()).length()));

            sb.append(" |\n");
        }
        board.append(putInColoredFrame(sb,ColorCli.CLEAR.toString()));
    }

    private void showDevCardSlots(LightPlayer player, StringBuilder board){
        StringBuilder dvs = new StringBuilder();
        dvs.append("\nDEVELOPMENT CARD SLOTS:\n");
        List<String>[] slots = player.getDevCardSlots();

        //double for to get the max length of ids (for indentation!)
        int spacesToLeave = 3;
        for(int i = 0; i<3; i++)
            for (int j = 0; j < slots[i].size(); j++)
                if(slots[i].get(j).length() > spacesToLeave)
                    spacesToLeave = slots[i].get(j).length();


        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                dvs.append(" | ");
                if(i >= slots[j].size())
                    dvs.append("-".repeat(spacesToLeave));
                else{
                    dvs.append(slots[j].get(i));
                    putSomeDistance(dvs,spacesToLeave);
                }
                dvs.append(" | ");
            }
            dvs.append("\n");
        }
        board.append(dvs);

    }

    private void showPopeTiles(LightPlayer player, StringBuilder board){

        board.append(player.getColor()).append(player.getNickname()).append(ColorCli.CLEAR).append("'s pope tiles: ");

        for(int i = 0; i < player.getPopeTiles().size(); i++){
            board.append("[ ").append(i+2).append(" ");
            switch(player.getPopeTiles().get(i)){
                case 0: //not reached tile
                    board.append(" - ");
                    break;
                case 1: //upside tile
                    board.append(ColorCli.GREEN).append(" ✪ ").append(ColorCli.CLEAR);
                    break;
                case 2: //discarded tail
                    board.append(ColorCli.RED).append(" X ").append(ColorCli.CLEAR);
                    break;
            }
            board.append("]");
        }
        board.append("\n");

    }

    private void showHandLeaders(LightPlayer player, StringBuilder board){
        StringBuilder hl = new StringBuilder();
        hl.append("\nHAND LEADERS: [ ");
        if(player.getHandLeaders().size() == 0)
            hl.append(ColorCli.RED).append("EMPTY").append(ColorCli.CLEAR).append("]");

        else{
            int i;
            for(i = 0; i < player.getHandLeaders().size()-1; i++)
                hl.append(player.getHandLeaders().get(i)).append(" , ");
            hl.append(player.getHandLeaders().get(i)).append(" ]\n");
        }

        board.append(hl);

    }

    private void showActiveLeaders(LightPlayer player, StringBuilder board){
        StringBuilder al = new StringBuilder();
        al.append("\nACTIVE LEADERS: [ ");
        if(player.getActiveLeaders().size() == 0)
            al.append(ColorCli.RED).append("EMPTY").append(ColorCli.CLEAR).append(" ]");

        else{
            int i;
            for(i = 0; i < player.getActiveLeaders().size()-1; i++)
                al.append(player.getActiveLeaders().get(i)).append(" , ");
            al.append(player.getActiveLeaders().get(i)).append(" ]\n");
        }

        board.append(al);
    }

    public void showTempDevCard(String nickname, LightMatch match, StringBuilder board){
        board.append("\nDEVELOPMENT CARD TO PAY: [ ");
            board.append(match.getPlayerSummary(nickname).getTempDevCard() + " ]");
        board.append("\n");
    }

    @Override
    public void drawCard(Card card, String id) {
        StringBuilder cardStr = new StringBuilder();
        if(card.isLeader()){
            cardStr.append(" (LEADER CARD ").append(id).append(")\n");
            cardStr.append("Requirements \n");
            for(Requirable r : ((LeaderCard) card).getRequirements()) {
                if(r.isAResource()){
                    PhysicalResource req = (PhysicalResource) r;
                    cardStr.append((char)186 + " Resource: ");
                    addColouredResource(req, cardStr);
                    cardStr.append(", ").append(req.getQuantity()).append("\n");
                }
                else{
                    CardType type = (CardType) r;
                    cardStr.append((char)186 + " Card Type: ");
                    cardStr.append("█ ".repeat(Math.max(0, type.getQuantity())));
                    cardStr.append(type.getColor().toString());
                    if(type.getLevel() > 0)
                        cardStr.append(", Lv.").append(type.getLevel());
                    cardStr.append("\n");
                }
            }
            cardStr.append(((LeaderCard) card).getEffect().toCLIString());
            cardStr.append((char) 186).append(" Win Points ").append(((LeaderCard) card).getWinPoints());
        }
        else { //TODO: finish!
            System.out.println(" (DEVELOPMENT CARD " + id + ")");
            System.out.println("|| Price ->");
            for(PhysicalResource r : ((DevelopmentCard) card).getPrice())
                System.out.println("|| - " + r.toString() + " ||");
            System.out.println("|| Production ->");
            System.out.println("|| -  Costs ->");
            for(PhysicalResource r : ((DevelopmentCard) card).getProduction().getCost())
                System.out.println("|| - " + r.toString() + " ||");
            System.out.println("|| -  Earnings ->");
            for(Resource r : ((DevelopmentCard) card).getProduction().getEarnings())
                System.out.println("|| - " + r.toString() + " ||");
            System.out.println("|| Win Points -> " + ((DevelopmentCard) card).getWinPoints() + " ||");
        }
        System.out.println(putInColoredFrame(cardStr, ColorCli.CLEAR.toString()));
    }

    //STYLING METHODS:

    public StringBuilder getPrintedCell(LightMatch match, int pos, String cell){
        StringBuilder str = new StringBuilder();
        String[] elements = cell.split("-");

        //color choice for the cell
        String color = ColorCli.WHITE.toString();
        if(Integer.parseInt(elements[1]) > 0)
            color = ColorCli.YELLOW.toString();
        if(Boolean.parseBoolean(elements[2]))
            color = ColorCli.RED.toString();

        //"win points" row
        str.append(elements[0]);
        putSomeDistance(str, 6-String.valueOf(elements[0]).length());
        str.append("\n");

        //"crosses" row
        int crosses = 0;
        for(LightPlayer p : match.getPlayersSummary())
            if(p.getFaithMarker() == pos) {
                str.append(p.getColor()).append("┼");
                crosses++;
            }
        if(match.getLorenzoMarker() == pos) {
            str.append(ColorCli.GREY_BOLD).append("┼");
            crosses++;
        }
        putSomeDistance(str, 5-crosses);
        str.append("\n");

        //"position of cell" row
        putSomeDistance(str, 6-String.valueOf(pos).length());
        str.append(pos);
        str.append("\n");


        return putInColoredFrame(str,color);
    }

    public static void addColouredResource(PhysicalResource resource, StringBuilder str) {
        switch(resource.getType()){
            case COIN:
                str.append(ColorCli.YELLOW).append(resource.getType()).append(ColorCli.CLEAR);
                break;
            case STONE:
                str.append(ColorCli.GREY_BOLD).append(resource.getType()).append(ColorCli.CLEAR);
                break;
            case SHIELD:
                str.append(ColorCli.BLUE).append(resource.getType()).append(ColorCli.CLEAR);
                break;
            case SERVANT:
                str.append(ColorCli.GREEN).append(resource.getType()).append(ColorCli.CLEAR);
                break;
        }
    }

    public static void addColouredMarble(char marble, StringBuilder str){
        String marblePoint = "●";
        switch(marble){
            case 'w':
                str.append(ColorCli.WHITE).append(marblePoint).append(ColorCli.CLEAR);
                break;
            case 'y':
                str.append(ColorCli.YELLOW).append(marblePoint).append(ColorCli.CLEAR);
                break;
            case 'g':
                str.append(ColorCli.GREY_BOLD).append(marblePoint).append(ColorCli.CLEAR);
                break;
            case 'b':
                str.append(ColorCli.BLUE).append(marblePoint).append(ColorCli.CLEAR);
                break;
            case 'r':
                str.append(ColorCli.RED).append(marblePoint).append(ColorCli.CLEAR);
                break;
            case 'p':
                str.append(ColorCli.PURPLE).append(marblePoint).append(ColorCli.CLEAR);
                break;
        }
    }

    private StringBuilder mergePrintingObjects(StringBuilder str1, StringBuilder str2, Integer maxCols, int SHIFT){

        if(str1.length() == 0)
            return str2;
        if(str2.length() == 0)
            return str1;

        String[] rows1 = str1.toString().split("\n");
        //int maxCols = 0;
        for(String row1 : rows1) {
            int partialMax = noANSIOccurrencesSize(row1);
            if (partialMax > maxCols)
                maxCols = partialMax;
        }

        String[] rows2 = str2.toString().split("\n");
        for(String row2 : rows2) {
            int partialMax = noANSIOccurrencesSize(row2);
            if (partialMax > maxCols)
                maxCols = partialMax;
        }

        StringBuilder mergedOne = new StringBuilder();
        for(int i = 0; i < Math.max(rows1.length, rows2.length); i++){
            if(i >= rows1.length)
                putSomeDistance(mergedOne, maxCols + SHIFT);
            else {
                mergedOne.append(rows1[i]);
                putSomeDistance(mergedOne, maxCols - noANSIOccurrencesSize(rows1[i]) + SHIFT);
            }
            if(i >= rows2.length)
                putSomeDistance(mergedOne, maxCols + SHIFT);
            else {
                mergedOne.append(rows2[i]);
                putSomeDistance(mergedOne, maxCols - noANSIOccurrencesSize(rows1[i]) + SHIFT);
            }
            mergedOne.append("\n");
        }
        return mergedOne;
    }

    private void putSomeDistance(StringBuilder str, int distance){
        str.append(" ".repeat(Math.max(0, distance)));
    }

    private int noANSIOccurrencesSize(String str){
        String str2 = str;
        for(ColorCli escape : ColorCli.values())
            str2 = str2.replaceAll(escape.toRegexString(), "");
        return str2.length();
    }

    private void appendLastLayout(StringBuilder str){
        str.append("\n").append(lastLayout).append("\n");
    }

    private StringBuilder putInColoredFrame(StringBuilder str, String color){
        StringBuilder finalStr = new StringBuilder();

        int maxCols = 0;
        String[] rows = str.toString().split("\n");
        for(String row : rows)
            if(noANSIOccurrencesSize(row) > maxCols)
                maxCols = noANSIOccurrencesSize(row);

        //first row
        finalStr.append(color).append("╔");
        for(int i = 0; i < maxCols+2; i++)
            finalStr.append("═");
        finalStr.append("╗\n").append(ColorCli.CLEAR);

        //stringBuilder rows processing
        for(String row : rows){
            finalStr.append(color).append("║ ").append(ColorCli.CLEAR);
            finalStr.append(row);
            putSomeDistance(finalStr, maxCols-noANSIOccurrencesSize(row));
            finalStr.append(color).append(" ║\n").append(ColorCli.CLEAR);
        }

        //last row
        finalStr.append(color).append("╚");
        for(int i = 0; i < maxCols+2; i++)
            finalStr.append("═");
        finalStr.append("╝\n").append(ColorCli.CLEAR);

        return finalStr;
    }

    private void clearScreen(){
        for(int i = 0; i < 50; i++)
            System.out.println();
    }

}