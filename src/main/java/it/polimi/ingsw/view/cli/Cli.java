package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.Map;

public class Cli implements View
{

    public Cli(){
        printTitle();
    }

    @Override
    public void printTitle(){
        StringBuilder str = new StringBuilder();

        str.append(ColorCli.YELLOW_BOLD).append("                        __  __               _____   _______   ______   _____     _____                   \n" +
                "                       |  \\/  |     /\\      / ____| |__   __| |  ____| |  __ \\   / ____|                  \n" +
                "                       | \\  / |    /  \\    | (___      | |    | |__    | |__) | | (___                    \n" +
                "                       | |\\/| |   / /\\ \\    \\___ \\     | |    |  __|   |  _  /   \\___ \\                   \n" +
                "                       | |  | |  / ____ \\   ____) |    | |    | |____  | | \\ \\   ____) |                  \n" +
                "                       |_|  |_| /_/    \\_\\ |_____/_    |_|___ |______| |_|  \\_\\ |_____/                   \n" +
                "                                              / __ \\  |  ____|                                            \n" +
                "                                             | |  | | | |__                                               \n" +
                "                                             | |  | | |  __|                                              \n" +
                "                                             | |__| | | |                                                 \n" +
                "        _____    ______   _   _              _\\____/  |_|__    _____              _   _    _____   ______ \n" +
                "       |  __ \\  |  ____| | \\ | |     /\\     |_   _|  / ____|  / ____|     /\\     | \\ | |  / ____| |  ____|\n" +
                "       | |__) | | |__    |  \\| |    /  \\      | |   | (___   | (___      /  \\    |  \\| | | |      | |__   \n" +
                "       |  _  /  |  __|   | . ` |   / /\\ \\     | |    \\___ \\   \\___ \\    / /\\ \\   | . ` | | |      |  __|  \n" +
                "       | | \\ \\  | |____  | |\\  |  / ____ \\   _| |_   ____) |  ____) |  / ____ \\  | |\\  | | |____  | |____ \n" +
                "       |_|  \\_\\ |______| |_| \\_| /_/    \\_\\ |_____| |_____/  |_____/  /_/    \\_\\ |_| \\_|  \\_____| |______|\n" +
                "                                                                                                          \n" +
                "                                                                                                          ").append(ColorCli.CLEAR);
        System.out.println(str);
    }

    @Override
    public void drawLoginLayout() {

        System.out.println("sei nel login");

    }

    @Override
    public void drawReconnectionLayout() {
        System.out.println("sei nel login");
    }

    @Override
    public void drawNewPlayerLayout() {
        System.out.println("single o multi?");
    }

    @Override
    public void drawNumPlayersLayout() {
        System.out.println("scegli il numero di player");
    }

    @Override
    public void drawConfigurationChoice() {
        System.out.println("config default?");
    }

    @Override
    public void drawConfigurationLayout() {

    }

    @Override
    public void drawWaitingLayout() {
        System.out.println("aspetta che si connettano altri...");
    }

    @Override
    public void drawLeadersChoiceLayout() {

    }

    @Override
    public void drawResourcesChoiceLayout() {

    }

    @Override
    public void drawYourTurnLayout(boolean yourTurn) {

    }

    @Override
    public void drawWhiteMarbleConversionsLayout() {

    }

    @Override
    public void drawResPlacementLayout() {

    }

    @Override
    public void drawBuyDevCardLayout() {

    }

    @Override
    public void drawPlaceDevCardLayout() {

    }

    @Override
    public void drawProductionLayout() {

    }

    @Override
    public void drawEndTurnLayout() {

    }

    @Override
    public void printLastRound(){

    }


    @Override
    public void drawEndMatchLayout() {

    }

    @Override
    public void drawRematchOfferLayout(String nickname) {

    }

    @Override
    public void printRetry(String errMessage) {
        System.out.println(errMessage);
    }


    private void drawResource(PhysicalResource resource){
        

    }

    public static void main(String args[]){
        Cli cli = new Cli();
    }

    @Override
    public void updateMarket(char[][] market, char sideMarble) {

    }

    @Override
    public void updateCardGrid(List<String>[][] cardGrid) {

    }

    @Override
    public void updateLorenzoMarker(int lorenzoMarker) {

    }

    @Override
    public void updateWarehouse(String nickname, List<PhysicalResource> warehouse) {

    }

    @Override
    public void updateMarketBuffer(String nickname, List<PhysicalResource> marketBuffer) {

    }

    @Override
    public void updateStrongbox(String nickname, List<PhysicalResource> strongbox) {

    }

    @Override
    public void updateFaithMarker(String nickname, int faithMarker) {

    }

    @Override
    public void updatePopeTiles(String nickname, List<Integer> popeTiles) {

    }

    @Override
    public void updateDevCardSlots(String nickname, List<String>[] devCardSlots) {

    }

    @Override
    public void updateHandLeaders(String nickname, List<String> handLeaders) {

    }

    @Override
    public void updateActiveLeaders(String nickname, List<String> activeLeaders) {

    }

    @Override
    public void updateWhiteMarbleConversions(String nickname, List<PhysicalResource> whiteMarbleConversions) {

    }

    @Override
    public void updateDiscountMap(String nickname, List<PhysicalResource> discountMap) {

    }

    @Override
    public void updateTempDevCard(String nickname, String tempDevCard) {

    }

    @Override
    public void printMatchResults(String nickname, Map<String, Integer> ranking){

    }

    @Override
    public void printTokenDraw(String tokenName, int remainingTokens){

    }
}
