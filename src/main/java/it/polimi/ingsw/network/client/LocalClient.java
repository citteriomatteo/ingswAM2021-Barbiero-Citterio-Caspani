package it.polimi.ingsw.network.client;

import it.polimi.ingsw.gameLogic.controller.InitController;
import it.polimi.ingsw.gameLogic.controller.MatchController;
import it.polimi.ingsw.gameLogic.controller.StateName;
import it.polimi.ingsw.gameLogic.model.match.player.Player;
import it.polimi.ingsw.network.message.ctosmessage.CtoSMessage;
import it.polimi.ingsw.network.message.stocmessage.NextStateMessage;
import it.polimi.ingsw.network.message.stocmessage.StoCMessage;
import it.polimi.ingsw.network.server.ControlBase;
import it.polimi.ingsw.view.ClientController;

/**
 * Singleton class for manage a local match, it contains all the game logic and the "network" stub
 */
public class LocalClient extends Client implements ControlBase {
    private static LocalClient instance;
    private static final ClientController clientController = ClientController.getClientController();
    private MatchController matchController;
    private Player player;

    /**
     * Private constructor of the LocalClient, since it is a singleton no one should create an instance of this class.
     * This constructor doesn't set any parameter
     */
    private LocalClient(){
        super();
    }

    /**
     * Gets the instance of the LocalClient singleton
     * @return the instance of the LocalClient singleton
     */
    public static LocalClient getLocalClient(){
        if(instance == null)
            instance = new LocalClient();
        return instance;
    }

    /**
     * Calls {@link Client#setView} and if cliChoice is true sets the initial state to login
     * @param cliChoice if true set up a CLI, if false set up a GUI
     */
    @Override
    public void setView(boolean cliChoice) {
        super.setView(cliChoice);

        if(cliChoice)
            write(new NextStateMessage(getNickname(), StateName.LOGIN));
    }

    /**
     * Writes an exiting message
     */
    @Override
    public void exit() {
        System.out.println("...Exiting from game");
    }

    /**
     * Computes directly the message
     * @param msg the message to compute
     * @return true if the execution succeeded
     */
    @Override
    public boolean writeMessage(CtoSMessage msg) {
        return msg.computeMessage(this);
    }

    @Override
    public ClientController getController() {
        return clientController;
    }

    /**
     * Sets the nickname of the player on clientController and creates the game logic (the player's instance and the matchController)
     * @param nickname the nickname of the player
     */
    public void createPlayer(String nickname){
        clientController.setNickname(nickname);
        setPlayer(new Player(nickname));
        setMatchController(new MatchController(getPlayer()));
    }

    @Override
    public String getNickname() {
        return super.getNickname();
    }

    /**
     * @return the current state of the player inside the match
     */
    @Override
    public StateName getCurrentState() {
        return matchController.getCurrentState(getNickname());
    }

    /**
     * @return the MatchController related to this
     */
    @Override
    public MatchController getMatchController() {
        return matchController;
    }

    /**
     * Returns nothing because this ControlBase has not an InitController
     * @return null
     */
    @Override
    public InitController getInitController() {
        return null;
    }

    /**
     * @return the player related to this client
     */
    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player related to this client
     * @param player the player related to this client
     */
    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Links the passed match controller to this client
     * @param controller the matchController to link
     */
    @Override
    public void setMatchController(MatchController controller) {
        this.matchController = controller;
    }

    /**
     * Method called at the end of the match -> if this method is called the match has comes naturally to
     * an end and the player can be disconnected without issues
     * It does nothing for local game
     */
    @Override
    public void endGame() {

    }

    /**
     * Writes something to this player, the message has to be written in one of the subclasses of {@link StoCMessage}
     * The message will be directly computed
     * @param msg the message you want to send to this player
     * @return true
     */
    @Override
    public boolean write(StoCMessage msg) {
        msg.compute(this);
        return true;
    }
}
