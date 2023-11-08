package Models;

import java.io.IOException;

import Controllers.GameEngine;
import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Services.MapService;
import Services.PlayerService;
import Utils.CommandHandler;

/**
 * The abstract Phase class represents a phase in the game, defining methods and operations that different phases will implement.
 */
public abstract class Phase {

    GameState d_gameState;
    GameEngine d_gameEngine;
    MapService d_mapService = new MapService();
    PlayerService d_playerService = new PlayerService();

    boolean l_isMapLoaded;

    /**
     * Constructor for the abstract Phase class.
     *
     * @param p_gameEngine the game engine
     * @param p_gameState  the current state of the game
     */
    public Phase(GameEngine p_gameEngine, GameState p_gameState){
        d_gameEngine = p_gameEngine;
        d_gameState = p_gameState;
    }

    public void setD_gameState(GameState p_gameState) {
        d_gameState = p_gameState;
    }

    public GameState getD_gameState() {
        return d_gameState;
    }

    /**
     * Handles the entered command by delegating to appropriate command handlers based on the root command.
     *
     * @param p_enteredCommand the command to handle
     * @throws MapValidationException if an issue with map validation occurs
     * @throws CommandValidationException if an invalid command is encountered
     * @throws IOException if an I/O error occurs
     */
    public void handleCommand(String p_enteredCommand) throws MapValidationException, CommandValidationException, IOException {
        commandHandler(p_enteredCommand, null);
    }

    /**
     * Overloaded method to handle commands with player-specific context.
     *
     * @param p_enteredCommand the command to handle
     * @param p_player the player issuing the command
     * @throws MapValidationException if an issue with map validation occurs
     * @throws CommandValidationException if an invalid command is encountered
     * @throws IOException if an I/O error occurs
     */
    public void handleCommand(String p_enteredCommand, ModelPlayer p_player) throws MapValidationException, CommandValidationException, IOException {
        commandHandler(p_enteredCommand, p_player);
    }

    /**
     * Internal method to handle commands and delegate to specific command handlers.
     *
     * @param p_enteredCommand the command to handle
     * @param p_player the player issuing the command
     * @throws MapValidationException if an issue with map validation occurs
     * @throws CommandValidationException if an invalid command is encountered
     * @throws IOException if an I/O error occurs
     */
    private void commandHandler(String p_enteredCommand, ModelPlayer p_player) throws MapValidationException, CommandValidationException, IOException {
        CommandHandler l_command = new CommandHandler(p_enteredCommand);
        String l_rootCommand = l_command.getRootCommand();
        l_isMapLoaded = d_gameState.getD_map() != null;

        d_gameState.updateLog(l_command.getD_command(), "command");

        switch (l_rootCommand) {
            case "editmap": {
                performingMapEdit(l_command, p_player);
                break;
            }
            case "editcontinent": {
                performingEditContinent(l_command, p_player);
                break;
            }
            case "savemap": {
                performingSaveMap(l_command, p_player);
                break;
            }
            case "loadmap": {
                performingLoadMap(l_command, p_player);
                break;
            }
            case "validatemap": {
                performingValidateMap(l_command, p_player);
                break;
            }
            case "editcountry": {
                performingEditCountry(l_command, p_player);
                break;
            }
            case "editneighbor": {
                performingEditNeighbour(l_command, p_player);
                break;
            }
            case "gameplayer": {
                creatingPlayers(l_command, p_player);
                break;
            }
            case "assigncountries": {
                performingAssignCountries(l_command, p_player);
                break;
            }
            case "showmap": {
                performingShowMap(l_command, p_player);
                break;
            }
            case "deploy": {
                performingCreateDeploy(p_enteredCommand, p_player);
                break;
            }
            case "advance": {
                performingAdvance(p_enteredCommand, p_player);
                break;
            }
            case "airlift":
            case "blockade":
            case "negotiate":
            case "bomb":
            {
                performingCardHandle(p_enteredCommand, p_player);
                break;
            }

            case "exit": {
                d_gameEngine.setD_gameEngineLog("Exit Command Entered, Game Ends!", "effect");
                System.exit(0);
                break;
            }
            default: {
                d_gameEngine.setD_gameEngineLog("Invalid Command", "effect");
                break;
            }
        }
    }

    /**
     * Performs the handling of card-related commands during the phase.
     *
     * @param p_enteredCommand the entered command
     * @param p_player         the player
     * @throws IOException if an I/O error occurs
     */
    protected abstract void performingCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException;

    /**
     * Performs the display of the game map during the phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws CommandValidationException      if an invalid command is encountered
     * @throws IOException         if an I/O error occurs
     * @throws MapValidationException if an issue with map validation occurs
     */
    protected abstract void performingShowMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, IOException, MapValidationException;

    /**
     * Performs the issuance of 'advance' orders during the phase.
     *
     * @param p_command the command
     * @param p_player  the player
     * @throws IOException if an I/O error occurs
     */
    protected abstract void performingAdvance(String p_command, ModelPlayer p_player) throws IOException;

    /**
     * Performs the creation of 'deploy' orders during the phase.
     *
     * @param p_command the command
     * @param p_player  the player
     * @throws IOException if an I/O error occurs
     */
    protected abstract void performingCreateDeploy(String p_command, ModelPlayer p_player) throws IOException;

    /**
     * Performs the assignment of countries to players during the phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws CommandValidationException      if an invalid command is encountered
     * @throws IOException         if an I/O error occurs
     * @throws MapValidationException if an issue with map validation occurs
     */
    protected abstract void performingAssignCountries(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, IOException, MapValidationException;

    /**
     * Performs the editing of neighbors during the phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws CommandValidationException      if an invalid command is encountered
     * @throws MapValidationException if an issue with map validation occurs
     * @throws IOException         if an I/O error occurs
     */
    protected abstract void performingEditNeighbour(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException;

    /**
     * Performs the editing of countries during the phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws CommandValidationException      if an invalid command is encountered
     * @throws MapValidationException if an issue with map validation occurs
     * @throws IOException         if an I/O error occurs
     */
    protected abstract void performingEditCountry(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException;

    /**
     * Performs map validation during the phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws MapValidationException if an issue with map validation occurs
     * @throws CommandValidationException      if an invalid command is encountered
     * @throws IOException         if an I/O error occurs
     */
    protected abstract void performingValidateMap(CommandHandler p_command, ModelPlayer p_player) throws MapValidationException, CommandValidationException, IOException;

    /**
     * Performs map loading during the phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws CommandValidationException      if an invalid command is encountered
     * @throws MapValidationException if an issue with map validation occurs
     * @throws IOException         if an I/O error occurs
     */
    protected abstract void performingLoadMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException;

    /**
     * Performs map saving during the phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws CommandValidationException      if an invalid command is encountered
     * @throws MapValidationException if an issue with map validation occurs
     * @throws IOException         if an I/O error occurs
     */
    protected abstract void performingSaveMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException;

    /**
     * Performs the editing of continents during the phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws IOException         if an I/O error occurs
     * @throws CommandValidationException      if an invalid command is encountered
     * @throws MapValidationException if an issue with map validation occurs
     */
    protected abstract void performingEditContinent(CommandHandler p_command, ModelPlayer p_player) throws IOException, CommandValidationException, MapValidationException;

    /**
     * Performs map editing during the phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws IOException         if an I/O error occurs
     * @throws CommandValidationException      if an invalid command is encountered
     * @throws MapValidationException if an issue with map validation occurs
     */
    protected abstract void performingMapEdit(CommandHandler p_command, ModelPlayer p_player) throws IOException, CommandValidationException, MapValidationException;

    /**
     * Creates players during the phase.
     *
     * @param p_command the command handler
     * @param p_player  the player
     * @throws CommandValidationException      if an invalid command is encountered
     * @throws IOException         if an I/O error occurs
     * @throws MapValidationException if an issue with map validation occurs
     */
    protected abstract void creatingPlayers(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, IOException, MapValidationException;

    /**
     * Handles the printing of an invalid command message in the current state.
     */
    public void printInvalidCommandInState(){
        d_gameEngine.setD_gameEngineLog("Invalid Command in Current State", "effect");
    }

    /**
     * Initializes the phase, to be implemented in concrete phase classes.
     */
    public abstract void initPhase();
}
