package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.controller.StateName;
import it.polimi.ingsw.model.essentials.*;
import it.polimi.ingsw.model.essentials.leader.LeaderCard;
import it.polimi.ingsw.model.essentials.leader.SlotEffect;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.lightmodel.LightMatch;
import it.polimi.ingsw.view.lightmodel.LightPlayer;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.view.ClientController.getClientController;
import static it.polimi.ingsw.view.lightmodel.LightMatch.getCardMap;

public class ClientCLI implements View
{
    //todo: insert match reference for showAll()
    private String lastLayout;
    private boolean lastRound = false;
    private static final Integer DEFAULT_SHIFT = 12, ZERO_SHIFT = 0;

    public ClientCLI(){
    }

    /**
     * Print the string builder doing a sort of animation and changing color
     * @param strBuilder the StringBuilder you want to animate
     */
    private void animation (StringBuilder strBuilder){
        StringBuilder temp = new StringBuilder();
        String[] rows = strBuilder.toString().split("\n");

        int maxLength = 0;
        for(String row : rows)
            maxLength = Math.max(maxLength, row.length());

        for (int i = 1; i < maxLength/2; i++) {
            for (String row : rows) {
                temp.append(row, 0, i).append(" ".repeat(Math.max(0,row.length()-2*i))).append(row, row.length()-i, row.length()).append("\n");
            }
            clearScreen();
            temp.insert(0, ColorCli.values()[i%ColorCli.values().length]).append(ColorCli.CLEAR);
            System.out.println(temp);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            temp = new StringBuilder();
        }

        clearScreen();

    }

    @Override
    public void printTitle(){
        StringBuilder str = new StringBuilder();

        str.append(
                "███╗   ███╗ █████╗ ███████╗████████╗███████╗██████╗ ███████╗     ██████╗ ███████╗    ██████╗ ███████╗███╗   ██╗ █████╗ ██╗███████╗███████╗ █████╗ ███╗   ██╗ ██████╗███████╗\n" +
                "████╗ ████║██╔══██╗██╔════╝╚══██╔══╝██╔════╝██╔══██╗██╔════╝    ██╔═══██╗██╔════╝    ██╔══██╗██╔════╝████╗  ██║██╔══██╗██║██╔════╝██╔════╝██╔══██╗████╗  ██║██╔════╝██╔════╝\n" +
                "██╔████╔██║███████║███████╗   ██║   █████╗  ██████╔╝███████╗    ██║   ██║█████╗      ██████╔╝█████╗  ██╔██╗ ██║███████║██║███████╗███████╗███████║██╔██╗ ██║██║     █████╗  \n" +
                "██║╚██╔╝██║██╔══██║╚════██║   ██║   ██╔══╝  ██╔══██╗╚════██║    ██║   ██║██╔══╝      ██╔══██╗██╔══╝  ██║╚██╗██║██╔══██║██║╚════██║╚════██║██╔══██║██║╚██╗██║██║     ██╔══╝  \n" +
                "██║ ╚═╝ ██║██║  ██║███████║   ██║   ███████╗██║  ██║███████║    ╚██████╔╝██║         ██║  ██║███████╗██║ ╚████║██║  ██║██║███████║███████║██║  ██║██║ ╚████║╚██████╗███████╗\n" +
                "╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝     ╚═════╝ ╚═╝         ╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝╚══════╝\n" +
                "                                                                                                                                                                            \n")
                ;

        // attempt for animation, remove comment to see
        // animation(str);

        str.insert(0, ColorCli.YELLOW_BOLD).append(ColorCli.CLEAR);
        str.append("Welcome!\n");
        System.out.println(str);
    }

    @Override
    public void drawLoginLayout() {
        lastLayout = "Insert the command \"login\" followed by your nickname";
        System.out.println(lastLayout);
    }

    @Override
    public void drawReconnectionLayout() {
        lastLayout = "There's already a disconnected player with this nickname. Is it you?";
        System.out.println(lastLayout);
    }

    @Override
    public void drawNewPlayerLayout() {
        lastLayout = "Write \"single\" or \"yes|(y)\" if you want to play a single player match, \"multi\" or \"no|(n)\" if you prefer to play in multiplayer mode";
        System.out.println(lastLayout);
    }

    @Override
    public void drawNumPlayersLayout() {
        lastLayout = "Choose the number of players you want to play with.\n" +
                "Write \"numplayers|(np)\" followed by the number of players that will form the match you want to make";
        System.out.println(lastLayout);
    }

    @Override
    public void drawConfigurationChoice() {
        lastLayout = "Do you want to play with the default configuration?";
        System.out.println(lastLayout);
    }

    @Override
    public void drawConfigurationLayout() {

    }

    @Override
    public void drawWaitingLayout() {
        lastLayout = "Waiting...";
        System.out.println(lastLayout);
    }

    @Override
    public void drawStartingPhaseDone() {

    }

    @Override
    public void drawLeadersChoiceLayout() {
        lastLayout = "Choose two Leaders";
        System.out.println(lastLayout);
    }

    @Override
    public void drawResourcesChoiceLayout(int yourPosition) {
        int numResources = yourPosition/2;
        lastLayout = "You are the "+yourPosition+"° player, you have the right to choose "+numResources+" resource" + (numResources>1 ? "s" : "");
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
        lastLayout = "Choose how to convert the white marbles";
        System.out.println(lastLayout);
    }

    @Override
    public void drawResPlacementLayout() {

        lastLayout = "Place the resources you've got";
        System.out.println(lastLayout);
    }

    @Override
    public void drawBuyDevCardLayout() {
        lastLayout = "Pay the card you've chosen";
        System.out.println(lastLayout);
    }

    @Override
    public void drawPlaceDevCardLayout() {
        lastLayout = "Place the card you've chosen";
        System.out.println(lastLayout);
    }

    @Override
    public void drawProductionLayout() {
        lastLayout = "Choose where to pay the resources from for your production";
        //System.out.println(lastLayout);
    }

    @Override
    public void drawEndTurnLayout() {
        lastLayout = "Discard/activate a leader, switch some shelves or finish your turn";
        //System.out.println(lastLayout);
    }

    @Override
    public void printLastRound(){
        //lastLayout = "It's the last round. Hurry up!";
        lastRound = true;
    }


    @Override
    public void drawEndMatchLayout() {

    }

    @Override
    public void drawRematchOfferLayout(String message) {
        System.out.println(message + " offered a rematch.\nAccept? [y/n]");
    }

    @Override
    public void drawGoodbyeLayout(String msg) {
        System.out.println(msg);
    }

    @Override
    public void printRetry(String errMessage, StateName currentState, LightMatch match) {
        //todo: fix this adding a errorLabel variable to be printed after the show all and removed at the change state
        String message = "Message from server: " + errMessage;
        if(match!=null) {
            lastLayout = message;
            showAll(match);
        }
        else
            System.out.println(message);
    }


    @Override
    public void updateMatch(LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateMarket(LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateCardGrid(LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateLorenzoMarker(LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateWarehouse(String nickname, LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateMarketBuffer(String nickname, LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateStrongbox(String nickname, LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateFaithMarker(String nickname, LightMatch match) {
        showAll(match);
    }

    @Override
    public void updatePopeTiles(String nickname, LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateDevCardSlots(String nickname, LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateHandLeaders(String nickname, LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateActiveLeaders(String nickname, LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateWhiteMarbleConversions(String nickname, LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateDiscountMap(String nickname, LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateTempDevCard(String nickname, LightMatch match) {
        showAll(match);
    }

    @Override
    public void updateDisconnections(String nickname, boolean connected) {
        showAll(getClientController().getMatch());
        if(connected)
            System.out.println("Player " +nickname+ " is back in the game."); //todo: use a controller function that can inform the view, not directly the print
        else
            System.out.println("Player " +nickname+ " has left the match.");
    }

    @Override
    public void printMatchResults(String nickname, Map<String, Integer> ranking){
        //LinkedHashMap preserve the ordering of elements in which they are inserted
        LinkedHashMap<String, Integer> sortedRanking = new LinkedHashMap<>();

        //Use Comparator.reverseOrder() for reverse ordering
        ranking.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedRanking.put(x.getKey(), x.getValue()));

        StringBuilder results = new StringBuilder();
        results.append("Match has ended, this is the ultimate ranking:\n");
        int i = 1;
        for (String player : sortedRanking.keySet()){
            results.append(i).append(") ").append(player).append(": ").append(sortedRanking.get(player)).append("\n");
            i++;
        }

        System.out.println(putInColoredFrame(results, ColorCli.CLEAR.toString()));

        System.out.println("Do you want to play a rematch? [y/n]");

    }

    @Override
    public void printTokenDraw(String tokenName, int remainingTokens){
        System.out.println("The "+tokenName+" has been drawn: "+remainingTokens+" remaining.");
    }


    //CLI PRINT METHODS OF COMMON THINGS:

    @Override
    public void showAll(LightMatch match){

        List<String> playersToPrint = match.getLightPlayers().stream().map(LightPlayer::getNickname).collect(Collectors.toList());
        clearScreen();

        StringBuilder div = new StringBuilder();
        div.append("═".repeat(400));
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
        for(int i=0; i<boards.size(); i++)
            allBoardsLined = new StringBuilder(mergePrintingObjects(allBoardsLined, boards.get(i), max, DEFAULT_SHIFT, i));

        if(lastRound) {
            allBoardsLined = putInColoredFrame(allBoardsLined, ColorCli.RED.toString());
            allBoardsLined.append(ColorCli.RED).append("\nIt's the last round. Hurry up!").append(ColorCli.CLEAR);
            System.out.println(allBoardsLined + "\n");
        }
        else
            System.out.println(allBoardsLined + "\n");

        System.out.println(div);

        System.out.println(lastLayout);

    }

    private void showCommonThings(LightMatch match){
        StringBuilder marketGraphic = new StringBuilder();
        showMarket(match.getMarket(), match.getSideMarble(), marketGraphic);

        StringBuilder gridGraphic = new StringBuilder();
        showCardGrid(match.getCardGrid(), gridGraphic);

        StringBuilder commonThings = mergePrintingObjects(marketGraphic, gridGraphic, 0, DEFAULT_SHIFT, 1);

        StringBuilder basicProdGraphic = new StringBuilder();
        showBasicProduction(match.getBasicProd(), basicProdGraphic);

        System.out.println(mergePrintingObjects(commonThings, basicProdGraphic, 0, DEFAULT_SHIFT, 2));

        StringBuilder pathGraphic = new StringBuilder();
        showFaithPath(match, pathGraphic);

        for(LightPlayer p : match.getLightPlayers()) {
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
        String topCardCode;
        for (List<String>[] column : cardGrid)
            for (List<String> element : column) {
                topCardCode = element.get(0);
                if (topCardCode != null && topCardCode.length() > spacesToLeave)
                    spacesToLeave = topCardCode.length();
            }

        ColorCli[] columnColors = {ColorCli.GREEN, ColorCli.BLUE, ColorCli.YELLOW, ColorCli.PURPLE};

        for (int i = cardGrid.length-1; i >= 0; i--) {
            List<String>[] lists = cardGrid[i];
            for (int j = 0; j < lists.length; j++) {
                StringBuilder points = new StringBuilder();
                DevelopmentCard card = (DevelopmentCard) getCardMap().get(lists[j].get(0));

                grid.append(columnColors[j]);

                grid.append("[ ");
                grid.append(lists[j].get(0) == null ? ColorCli.RED.paint("X") : lists[j].get(0));
                grid.append(" ".repeat(Math.max(0, spacesToLeave - noANSIOccurrencesSize((lists[j].get(0) == null ? ColorCli.RED.paint("X") : lists[j].get(0))))));
                grid.append(" (").append(lists[j].get(1)).append(") ] ");
                grid.append(ColorCli.CLEAR);
            }
            for(int k = 0; k <= i; k++)
                grid.append("○");
            grid.append("\n");
        }
        gridGraphic.append(grid);
    }

    private void showBasicProduction(Production basicProd, StringBuilder basicProdGraphic){
        basicProdGraphic.append("\nBASIC PRODUCTION:\n");
        StringBuilder costs = new StringBuilder();
        for(Resource r : basicProd.getCost())
            for (int i = 0; i < r.getQuantity(); i++) {
                costs.append("[");
                addColouredResource(r, costs);
                costs.append("] ");
            }
        StringBuilder arrow = new StringBuilder("↓\n");

        StringBuilder earnings = new StringBuilder();
        for(Resource r : basicProd.getEarnings())
            for (int i = 0; i < r.getQuantity(); i++) {
                earnings.append("[");
                addColouredResource(r, earnings);
                earnings.append("] ");
            }

        int firstRealSize = noANSIOccurrencesSize(costs.toString());
        int secondRealSize = noANSIOccurrencesSize(earnings.toString());

        if(firstRealSize < secondRealSize) {
            putSomeDistance(basicProdGraphic, (secondRealSize- firstRealSize) / 2);
            basicProdGraphic.append(costs).append("\n");
            putSomeDistance(basicProdGraphic, noANSIOccurrencesSize(earnings.toString()) / 2 - 1);
            basicProdGraphic.append(arrow);
        }
        else {
            basicProdGraphic.append(costs).append("\n");
            putSomeDistance(basicProdGraphic, firstRealSize / 2 - 1);
            basicProdGraphic.append(arrow);
            putSomeDistance(basicProdGraphic, (firstRealSize - secondRealSize) / 2);
        }

        basicProdGraphic.append(earnings).append("\n");

    }


    private void showFaithPath(LightMatch match, StringBuilder pathGraphic){
        System.out.println("\nFAITH PATH:\n");
        for(int i = 0; i < match.getFaithPath().size(); i++)
            pathGraphic = mergePrintingObjects(pathGraphic, getPrintedCell(match, i, match.getFaithPath().get(i)), 0, ZERO_SHIFT, i+1);
        System.out.println(pathGraphic);
    }

    //CLI PRINT METHODS OF PLAYER'S THINGS:

    private void showBoard(String nickname, LightMatch match, StringBuilder board){
        board.append("\n----- PERSONAL BOARD OF ").append(match.getLightPlayer(nickname).getColor()).append(nickname).append(ColorCli.CLEAR);
        if(!match.getLightPlayer(nickname).isConnected())
            board.append(ColorCli.RED).append(" (OFFLINE) ").append(ColorCli.CLEAR);
        board.append(" ----- \n");
        showDevCardSlots(match.getLightPlayer(nickname), board);
        showWarehouse(match.getLightPlayer(nickname), board);
        showStrongbox(match.getLightPlayer(nickname),board);
        showHandLeaders(match.getLightPlayer(nickname), board);
        showActiveLeaders(match.getLightPlayer(nickname), board);
        if(match.getLightPlayer(nickname).getTempDevCard() != null)
            showTempDevCard(nickname, match, board);

    }

    private void showWarehouse(LightPlayer player, StringBuilder board){
        StringBuilder wh = new StringBuilder();
        wh.append("\nWAREHOUSE:\n");
        for(int i = 0; i < player.getWarehouse().size() && i < 3; i++) {
            PhysicalResource currRes = player.getWarehouse().get(i);
            wh.append("| ");
            int j;
            for (j = 0; j < currRes.getQuantity(); j++) {
                addColouredResource(currRes, wh);
                putSomeDistance(wh, 8 - currRes.getType().toString().length());
                wh.append(" | ");
            }
            for(int k = j; k < i+1; k++) {
                wh.append(ColorCli.RED).append(" ------ ").append(ColorCli.CLEAR);
                wh.append(" |");
            }
            wh.append("\n");
        }
        wh.append("\n");
        board.append(wh);


        if(player.getWarehouse().size() > 3){

            int leaderInvolvedPos = 0;

            wh= new StringBuilder();
            wh.append("\nEXTRA SHELVES:\n");
            for(int i = 3; i < player.getWarehouse().size(); i++) {
                PhysicalResource currRes = player.getWarehouse().get(i);
                addColouredResource(currRes, wh);
                wh.append(": |");
                int j;
                for (j = 0; j < currRes.getQuantity(); j++) {
                    addColouredResource(currRes, wh);
                    putSomeDistance(wh, 8 - currRes.getType().toString().length());
                    wh.append(" | ");
                }

                //this is the procedure to find the leader involved with the current extra shelf
                boolean found=false;
                LeaderCard actualLeader = null;
                for(int m = leaderInvolvedPos; m < getCardMap().keySet().size() && leaderInvolvedPos < player.getActiveLeaders().size() && !found; m++) {
                    if(getCardMap().get(new ArrayList<>(getCardMap().keySet()).get(m)).isLeader()) {
                        actualLeader = (LeaderCard) getCardMap().get(new ArrayList<>(getCardMap().keySet()).get(m));
                        if (actualLeader.getEffect().getEffectSymbol().equals(ColorCli.RED + "≡" + ColorCli.CLEAR)) {
                            leaderInvolvedPos ++;
                            found = true;
                        }
                    }
                }

                //prints a number of empty blocks that is the same as the involved slotLeader's offered spaces.
                for(int k = j; k < ((SlotEffect) actualLeader.getEffect()).getExtraShelf().getQuantity(); k++) {
                    wh.append(ColorCli.RED).append(" ------ ").append(ColorCli.CLEAR);
                    wh.append(" |");
                }
                wh.append("\n");
                }
            board.append(wh);
        }



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
        wh.append("\n");
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
        board.append("\nDEVELOPMENT CARD SLOTS:\n");
        List<String>[] slots = player.getDevCardSlots();

        //double for to get the max length of ids (for indentation!)
        int spacesToLeave = 3;
        for(int i = 0; i<3; i++)
            for (int j = 0; j < slots[i].size(); j++)
                if(slots[i].get(j).length() > spacesToLeave)
                    spacesToLeave = slots[i].get(j).length();

        for(int i = 2; i >= 0; i--){
            for(int j = 0; j < 3; j++){
                dvs.append(" | ");
                if(i >= slots[j].size())
                    dvs.append("-".repeat(spacesToLeave));
                else{
                    dvs.append(slots[j].get(i));
                    putSomeDistance(dvs,spacesToLeave-slots[j].get(i).length());
                }
                dvs.append(" | ");
            }
            dvs.append("\n");
        }
        board.append(putInColoredFrame(dvs, ColorCli.CLEAR.toString()));

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
            hl.append(ColorCli.RED).append("EMPTY").append(ColorCli.CLEAR).append("]\n");

        else{
            int i;
            for(i = 0; i < player.getHandLeaders().size()-1; i++) {

                if(player.getHandLeaders().get(i).equals("-1"))
                    hl.append("?");
                else
                    addLeaderSymbol(player.getHandLeaders().get(i), hl);
                hl.append(", ");

            }

            if(player.getHandLeaders().get(i).equals("-1"))
                hl.append("?");
            else
                addLeaderSymbol(player.getHandLeaders().get(i), hl);
            hl.append(" ]\n");
        }

        board.append(hl);

    }

    private void showActiveLeaders(LightPlayer player, StringBuilder board){
        StringBuilder al = new StringBuilder();
        al.append("\nACTIVE LEADERS: [ ");
        if(player.getActiveLeaders().size() == 0)
            al.append(ColorCli.RED).append("EMPTY").append(ColorCli.CLEAR).append(" ]\n");

        else{
            int i;
            for(i = 0; i < player.getActiveLeaders().size()-1; i++) {
                addLeaderSymbol(player.getActiveLeaders().get(i), al);
                al.append(", ");
            }
            addLeaderSymbol(player.getActiveLeaders().get(i),al);
            al.append(" ]\n");
        }

        board.append(al);
    }

    public void showTempDevCard(String nickname, LightMatch match, StringBuilder board){
        board.append("\nDEVELOPMENT CARD TO PAY: ");
        board.append("[ ").append(match.getLightPlayer(nickname).getTempDevCard()).append(" ]");
        board.append("\n");
    }

    @Override
    public void drawCards(List<String> ids) {
        StringBuilder cards = new StringBuilder();
        for(String id : ids) {
            Card card = getCardMap().get(id);
            if (card.isLeader())
                cards = mergePrintingObjects(cards, drawLeaderCard((LeaderCard) card, id), 0, ZERO_SHIFT+1, ids.indexOf(id)+1);
            else
                cards = mergePrintingObjects(cards, drawDevCard((DevelopmentCard) card, id), 0, ZERO_SHIFT+1, ids.indexOf(id)+1);
        }
        System.out.println(cards);
    }

    private StringBuilder drawLeaderCard(LeaderCard card, String id){
        StringBuilder cardStr = new StringBuilder();
        cardStr.append(" (LEADER CARD ").append(id).append(")\n");
        cardStr.append("Requirements \n");
        for(Requirable r : card.getRequirements()) {
            if(r.isAResource()){
                PhysicalResource req = (PhysicalResource) r;
                cardStr.append((char)186 + " Resource: ");
                addColouredResource(req, cardStr);
                cardStr.append(", ").append(req.getQuantity()).append("\n");
            }
            else{
                CardType type = (CardType) r;
                cardStr.append((char)186 + " Card Type: ");
                appendColoredCardType(type, cardStr);
                cardStr.append("\n");
            }
        }
        cardStr.append(card.getEffect().toCLIString());
        cardStr.append((char) 186).append(" Win Points ").append(card.getWinPoints());

        //System.out.println(putInColoredFrame(cardStr, ColorCli.CLEAR.toString()));
        return putInColoredFrame(cardStr, ColorCli.CLEAR.toString());
    }
    private StringBuilder drawDevCard(DevelopmentCard card, String id){
        StringBuilder cardStr = new StringBuilder();

        cardStr.append(" (DEVELOPMENT CARD ").append(id).append(")\n");
        cardStr.append("Price \n");
        for(PhysicalResource r : card.getPrice()){
            cardStr.append((char)186).append(" ");
            addColouredResource(r, cardStr);
            cardStr.append(",").append(r.getQuantity());
            cardStr.append("\n");
        }
        cardStr.append("Production \n");
        cardStr.append((char)186+"Costs: ");
        putSomeDistance(cardStr, 3);
        for(PhysicalResource r : card.getProduction().getCost()){
            cardStr.append("[");
            addColouredResource(r, cardStr);
            cardStr.append(",").append(r.getQuantity());
            cardStr.append("]");
        }
        cardStr.append("\n"+(char)186+"Earnings: ");
        for(Resource r : card.getProduction().getEarnings()){
            cardStr.append("[");
            addColouredResource(r, cardStr);
            cardStr.append(",").append(r.getQuantity());
            cardStr.append("]");
        }

        cardStr.append("\n"+(char)186).append("Win Points: ");
        cardStr.append(card.getWinPoints()).append("\n");

        //System.out.println(putInColoredFrame(cardStr, ColorCli.CLEAR.toString()));
        return putInColoredFrame(cardStr, ColorCli.CLEAR.toString());
    }

    @Override
    public void printDiscountMap(LightPlayer player){

        StringBuilder dm = new StringBuilder(" Discount Map: \n");
        boolean foundOne = false;

        for(PhysicalResource r : player.getDiscountMap()){
            if(r.getQuantity() > 0) {
                foundOne = true;
                addColouredResource(r, dm);
                dm.append(": ");
                putSomeDistance(dm, 7-r.getType().toString().length());
                dm.append(r.getQuantity());
            }
        }
        if(!foundOne)
            dm.append(ColorCli.RED).append("\n   NO DISCOUNTS YET").append(ColorCli.CLEAR);
        System.out.println(putInColoredFrame(dm, ColorCli.CLEAR.toString()));

    }

    @Override
    public void printWhiteMarbleConversions(LightPlayer player) {
        StringBuilder dm = new StringBuilder(" White marble conversions: \n");
        boolean foundOne = false;

        for(PhysicalResource r : player.getWhiteMarbleConversions()){
            if(r.getQuantity() > 0) {
                dm.append(ColorCli.WHITE).append("●").append(ColorCli.CLEAR).append("  →  ");
                foundOne = true;
                addColouredResource(r, dm);
                dm.append(",").append(r.getQuantity());
            }
        }
        if(!foundOne)
            dm.append(ColorCli.RED).append("\n  NO CONVERSIONS YET").append(ColorCli.CLEAR);
        System.out.println(putInColoredFrame(dm, ColorCli.CLEAR.toString()));

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
        for(LightPlayer p : match.getLightPlayers())
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

    private String coloredCardColor(CardColor color){
        String sign = "█ ";
        switch (color){
            case BLUE:
                return ColorCli.BLUE.paint(sign);
            case GREEN:
                return ColorCli.GREEN.paint(sign);
            case PURPLE:
                return ColorCli.PURPLE.paint(sign);
            case YELLOW:
                return ColorCli.YELLOW.paint(sign);
            default:
                return sign;
        }
    }

    private void appendColoredCardType(CardType type, StringBuilder str){
        str.append(coloredCardColor(type.getColor()).repeat(Math.max(0, type.getQuantity())));
        str.append(type.getColor().toString());
        if(type.getLevel() > 0)
            str.append(", Lv.").append(type.getLevel());
    }

    private void addLeaderSymbol(String cardId, StringBuilder str){
        String symbol = "";
        if(!cardId.equals("-1"))
            symbol = ((LeaderCard) getCardMap().get(cardId.toUpperCase())).getEffect().getEffectSymbol();
        str.append(cardId).append(symbol);
    }

    public static void addColouredResource(Resource resource, StringBuilder str) {
        if(resource.isPhysical()) {
            ResType type = ((PhysicalResource) resource).getType();
            switch (type) {
                case COIN:
                    str.append(ColorCli.YELLOW).append(type).append(ColorCli.CLEAR);
                    break;
                case STONE:
                    str.append(ColorCli.GREY_BOLD).append(type).append(ColorCli.CLEAR);
                    break;
                case SHIELD:
                    str.append(ColorCli.BLUE).append(type).append(ColorCli.CLEAR);
                    break;
                case SERVANT:
                    str.append(ColorCli.PURPLE).append(type).append(ColorCli.CLEAR);
                    break;
                case UNKNOWN:
                    str.append(ColorCli.WHITE).append("?").append(ColorCli.CLEAR);
                    break;
            }
        }
        else
            str.append(ColorCli.RED).append("┼").append(ColorCli.CLEAR);

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

    private StringBuilder mergePrintingObjects(StringBuilder str1, StringBuilder str2, Integer maxCols, int SHIFT, int mergeCounter){

        if(str1.length() == 0)
            return str2;
        if(str2.length() == 0)
            return str1;

        String[] rows1 = str1.toString().split("\n");

        String[] rows2 = str2.toString().split("\n");
        for(String row2 : rows2) {
            int partialMax = noANSIOccurrencesSize(row2);
            if (partialMax > maxCols)
                maxCols = partialMax;
        }

        StringBuilder mergedOne = new StringBuilder();
        for(int i = 0; i < Math.max(rows1.length, rows2.length); i++){
            if(i >= rows1.length)
                putSomeDistance(mergedOne, maxCols * (mergeCounter) + SHIFT * (mergeCounter));

            else {
                mergedOne.append(rows1[i]);
                putSomeDistance(mergedOne, maxCols - noANSIOccurrencesSize(rows1[i]) + SHIFT);
            }
            if(i >= rows2.length)
                putSomeDistance(mergedOne, maxCols*(mergeCounter-1) + SHIFT*(mergeCounter-1));
            else {
                mergedOne.append(rows2[i]);
                putSomeDistance(mergedOne, maxCols - noANSIOccurrencesSize(rows2[i]) + SHIFT);
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

    @Override
    public void setLastRound(boolean lastRound) { this.lastRound = lastRound; }


}