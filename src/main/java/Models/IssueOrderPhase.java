package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import Controllers.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.MapValidationException;
import Utils.CommandHandler;
import Views.MapView;

/**
 * The IssueOrderPhase class represents the phase in the game where players issue orders.
 * Extends the Phase class.
 */
public class IssueOrderPhase extends Phase{

    /**
     * Constructor for the IssueOrderPhase class.
     *
     * @param p_gameEngine the game engine
     * @param p_gameState  the current state of the game
     */
    public IssueOrderPhase(GameEngine p_gameEngine, GameState p_gameState) {
        super(p_gameEngine, p_gameState);
    }
    /**
     * Handles the assigning of countries to players during the issue order phase.
     * Overrides the method from the Phase class.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws InvalidCommand      if an invalid command is encountered
     * @throws IOException         if an I/O error occurs
     * @throws MapValidationException if an issue with map validation occurs
     */
    @Override
    protected void performingAssignCountries(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, IOException, MapValidationException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    /**
     * Creates players during the issue order phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws InvalidCommand      if an invalid command is encountered
     * @throws IOException         if an I/O error occurs
     * @throws MapValidationException if an issue with map validation occurs
     */
    @Override
    protected void creatingPlayers(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, IOException, MapValidationException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    /**
     * Performs the editing of neighbors during the issue order phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws InvalidCommand      if an invalid command is encountered
     * @throws MapValidationException if an issue with map validation occurs
     * @throws IOException         if an I/O error occurs
     */
    @Override
    protected void performingEditNeighbour(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, MapValidationException, IOException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    /**
     * Overrides the Phase class method to handle the editing of countries during the issue order phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws InvalidCommand      if an invalid command is encountered
     * @throws IOException         if an I/O error occurs
     * @throws MapValidationException if an issue with map validation occurs
     */
    @Override
    protected void performingEditCountry(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, MapValidationException, IOException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    /**
     * Validates the map during the issue order phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws MapValidationException if an issue with map validation occurs
     * @throws InvalidCommand      if an invalid command is encountered
     * @throws IOException         if an I/O error occurs
     */
    @Override
    protected void performingValidateMap(CommandHandler p_command, ModelPlayer p_player) throws MapValidationException, InvalidCommand, IOException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    /**
     * Loads the map during the issue order phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws InvalidCommand      if an invalid command is encountered
     * @throws MapValidationException if an issue with map validation occurs
     * @throws IOException         if an I/O error occurs
     */
    @Override
    protected void performingLoadMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, MapValidationException, IOException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    /**
     * Saves the map during the issue order phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws InvalidCommand      if an invalid command is encountered
     * @throws MapValidationException if an issue with map validation occurs
     * @throws IOException         if an I/O error occurs
     */
    @Override
    protected void performingSaveMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, MapValidationException, IOException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    /**
     * Performs the editing of the continent during the issue order phase.
     * Overrides the method from the Phase class.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws IOException         if an I/O error occurs
     * @throws InvalidCommand      if an invalid command is encountered
     * @throws MapValidationException if an issue with map validation occurs
     */
    @Override
    protected void performingEditContinent(CommandHandler p_command, ModelPlayer p_player) throws IOException, InvalidCommand, MapValidationException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    /**
     * Overrides the Phase class method to perform map editing during the issue order phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws IOException         if an I/O error occurs
     * @throws InvalidCommand      if an invalid command is encountered
     * @throws MapValidationException if an issue with map validation occurs
     */
    @Override
    protected void performingMapEdit(CommandHandler p_command, ModelPlayer p_player) throws IOException, InvalidCommand, MapValidationException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    /**
     * Performs the handling of card-related commands during the issue order phase.
     *
     * @param p_enteredCommand the entered command
     * @param p_player         the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void performingCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException {
        if(p_player.getD_cardsOwnedByPlayer().contains(p_enteredCommand.split(" ")[0])) {
                p_player.handleCardCommands(p_enteredCommand, d_gameState);
                d_gameEngine.setD_gameEngineLog(p_player.d_playerLog, "effect");
        }  
        p_player.checkForMoreOrders();
    }

    /**
     * Shows the map during the issue order phase.
     * Overrides the method from the Phase class.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws InvalidCommand      if an invalid command is encountered
     * @throws IOException         if an I/O error occurs
     * @throws MapValidationException if an issue with map validation occurs
     */
    @Override
    protected void performingShowMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, IOException, MapValidationException {
        MapView l_mapView = new MapView(d_gameState);
        l_mapView.showMap();

        askForOrder(p_player);
    }

    /**
     * Performs the issuing of 'advance' orders during the issue order phase.
     *
     * @param p_command the command
     * @param p_player  the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void performingAdvance(String p_command, ModelPlayer p_player) throws IOException {
        p_player.createAdvanceOrder(p_command, d_gameState);
        d_gameState.updateLog(p_player.getD_playerLog(), "effect");
        p_player.checkForMoreOrders();
    }

    /**
     * Initializes the issue order phase, allowing players to issue orders.
     */
    @Override
    public void initPhase(){
        while (d_gameEngine.getD_CurrentPhase() instanceof IssueOrderPhase) {
            issueOrders();
        }
    }

    /**
     * Performs the creation of 'deploy' orders during the issue order phase.
     *
     * @param p_command the command
     * @param p_player  the player
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void performingCreateDeploy(String p_command, ModelPlayer p_player) throws IOException {
        p_player.createDeployOrder(p_command);
        d_gameState.updateLog(p_player.getD_playerLog(), "effect");
        p_player.checkForMoreOrders();
    }

    /**
     * Handles the issuance of orders by players in the game.
     */
    protected void issueOrders(){
        // issue orders for each player
        do {
            for (ModelPlayer l_player : d_gameState.getD_players()) {
                if (l_player.getD_moreOrders() && !l_player.getPlayerName().equals("Neutral")) {
                    try {
                        l_player.issue_order(this);
                    } catch (InvalidCommand | IOException | MapValidationException l_exception) {
                        d_gameEngine.setD_gameEngineLog(l_exception.getMessage(), "effect");
                    }
                }
            }
        } while (d_playerService.checkForMoreOrders(d_gameState.getD_players()));

        d_gameEngine.setOrderExecutionPhase();
    }

    /**
     * Asks for orders from the player via the command line interface.
     *
     * @param p_player the player to issue orders for
     * @throws InvalidCommand      if the entered command is invalid
     * @throws IOException         if an I/O error occurs
     * @throws MapValidationException if an issue with map validation occurs
     */
    public void askForOrder(ModelPlayer p_player) throws InvalidCommand, IOException, MapValidationException{
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nPlease enter command to issue order for player : " + p_player.getPlayerName()
                + " or give showmap command to view current state of the game.");
        String l_commandEntered = l_reader.readLine();

        d_gameState.updateLog("(Player: "+p_player.getPlayerName()+") "+ l_commandEntered, "order");

        handleCommand(l_commandEntered, p_player);
    }

}
