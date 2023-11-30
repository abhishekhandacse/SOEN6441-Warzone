package Models;

import java.io.IOException;
import java.io.Serializable;

import Controllers.GameEngine;
import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Services.MapService;
import Services.PlayerService;
import Utils.CommandHandler;

/**
 * Represents a game phase in the game engine.
 * This abstract class provides a framework for different game phases, such as
 * editing the map, issuing orders, etc.
 */

public abstract class Phase implements Serializable {

    /**
     * final value for serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * object for GameState
     */
    GameState d_gameState;

    /**
     * object for GameEngine
     */
    GameEngine d_gameEngine;

    /**
     * boolean value for map 
     */
    boolean l_isMapLoaded;

    /**
     * object for ModelPlayer
     */
    ModelPlayer d_player = new ModelPlayer();

    /**
     * object for Tournament
     */
    Tournament d_tournament = new Tournament();

    /**
     * object for MapService
     */
    MapService d_mapService = new MapService();

    /**
     * object for PlayerService
     */
    PlayerService d_playerService = new PlayerService();

    /**
     * Handles the command entered by the player.
     *
     * @param p_enteredCommand The command entered by the player.
     * @throws MapValidationException     If map validation fails.
     * @throws CommandValidationException If command validation fails.
     * @throws IOException                If an I/O error occurs.
     */
    private void commandHandler(String p_enteredCommand, ModelPlayer p_player)
            throws MapValidationException, CommandValidationException, IOException {
        CommandHandler l_command = new CommandHandler(p_enteredCommand);
        String l_rootCommand = l_command.getRootCommand();
        l_isMapLoaded = d_gameState.getD_map() != null;

        d_gameState.updateLog(l_command.getD_command(), "command");

        switch (l_rootCommand) {
            case "editmap": {
                performMapEdit(l_command, p_player);
                break;
            }
            case "editcontinent": {
                performEditContinent(l_command, p_player);
                break;
            }
            case "savemap": {
                performSaveMap(l_command, p_player);
                break;
            }
            case "loadmap": {
                performLoadMap(l_command, p_player);
                break;
            }
            case "validatemap": {
                performValidateMap(l_command, p_player);
                break;
            }
            case "editcountry": {
                performEditCountry(l_command, p_player);
                break;
            }
            case "editneighbor": {
                performEditNeighbours(l_command, p_player);
                break;
            }
            case "gameplayer": {
                createPlayers(l_command, p_player);
                break;
            }
            case "assigncountries": {
                performAssignCountries(l_command, p_player, false, d_gameState);
                break;
            }
            case "showmap": {
                performShowMap(l_command, p_player);
                break;
            }
            case "deploy": {
                performCreateDeploy(p_enteredCommand, p_player);
                break;
            }
            case "advance": {
                performAdvance(p_enteredCommand, p_player);
                break;
            }
            case "savegame": {
                performSaveGame(l_command, p_player);
                break;
            }
            case "loadgame": {
                performLoadGame(l_command, p_player);
                break;
            }
            case "airlift":
            case "blockade":
            case "negotiate":
            case "bomb": {
                performCardHandle(p_enteredCommand, p_player);
                break;
            }
            case "tournament": {
                tournamentGamePlay(l_command);
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
     * Constructor 
     * @param p_gameEngine game engine
     * @param p_gameState - game state
     */
    public Phase(GameEngine p_gameEngine, GameState p_gameState) {
        d_gameEngine = p_gameEngine;
        d_gameState = p_gameState;
    }

    /**
     * Gets the game state
     * @return - the game state
     */
    public GameState getD_gameState() {
        return d_gameState;
    }

    /**
     * Setter for game state
     * @param p_gameState - sets the game state
     */
    public void setD_gameState(GameState p_gameState) {
        d_gameState = p_gameState;
    }

    /**
     * Handles a command entered by the player.
     *
     * @param p_enteredCommand The command entered by the player.
     * @throws MapValidationException     If map validation fails.
     * @throws CommandValidationException If command validation fails.
     * @throws IOException                If an I/O error occurs.
     */
    public void handleCommand(String p_enteredCommand)
            throws MapValidationException, CommandValidationException, IOException {
        commandHandler(p_enteredCommand, null);
    }

    /**
     * Handles a command entered by the player.
     *
     * @param p_enteredCommand The command entered by the player.
     * @param p_player         The player associated with the command.
     * @throws MapValidationException     If map validation fails.
     * @throws CommandValidationException If command validation fails.
     * @throws IOException                If an I/O error occurs.
     */
    public void handleCommand(String p_enteredCommand, ModelPlayer p_player)
            throws MapValidationException, CommandValidationException, IOException {
        commandHandler(p_enteredCommand, p_player);
    }

    /**
     * Initializes the game phase.
     *
     * @param p_isTournamentMode Indicates whether the game is in tournament mode.
     */
    public abstract void initPhase(boolean p_isTournamentMode);

    /**
     * Prints a message for invalid commands in the current state.
     */
    public void printCommandValidationExceptionInState() {
        d_gameEngine.setD_gameEngineLog("Invalid Command in Current State", "effect");
    }

    /**
     * Performs the actions associated with loading a game.
     *
     * @param p_command The command handler for loading the game.
     * @param p_player  The player initiating the load game action.
     * @throws CommandValidationException If command validation fails.
     * @throws MapValidationException     If map validation fails.
     * @throws IOException                If an I/O error occurs.
     */
    protected abstract void performLoadGame(CommandHandler p_command, ModelPlayer p_player)
            throws CommandValidationException, MapValidationException, IOException;

    /**
     * Performs the actions associated with saving a game.
     *
     * @param p_command The command handler for saving the game.
     * @param p_player  The player initiating the save game action.
     * @throws CommandValidationException If command validation fails.
     * @throws MapValidationException     If map validation fails.
     * @throws IOException                If an I/O error occurs.
     */
    protected abstract void performSaveGame(CommandHandler p_command, ModelPlayer p_player)
            throws CommandValidationException, MapValidationException, IOException;

    /**
     * Performs the actions associated with handling a card command.
     *
     * @param p_enteredCommand The card command entered by the player.
     * @param p_player         The player initiating the card command.
     * @throws IOException If an I/O error occurs.
     */
    protected abstract void performCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException;

    /**
     * Performs the actions associated with showing the map.
     *
     * @param p_command The command handler for showing the map.
     * @param p_player  The player initiating the show map action.
     * @throws CommandValidationException If command validation fails.
     * @throws MapValidationException     If map validation fails.
     * @throws IOException                If an I/O error occurs.
     */
    protected abstract void performShowMap(CommandHandler p_command, ModelPlayer p_player)
            throws CommandValidationException, IOException, MapValidationException;

    /**
     * Performs the actions associated with advancing the game state.
     *
     * @param p_command The command for advancing the game state.
     * @param p_player  The player initiating the advance action.
     * @throws IOException If an I/O error occurs.
     */
    protected abstract void performAdvance(String p_command, ModelPlayer p_player) throws IOException;

    /**
     * Performs the actions associated with a tournament gameplay.
     *
     * @param p_command The command for tournament gameplay.
     * @throws CommandValidationException If command validation fails.
     * @throws MapValidationException     If map validation fails.
     */
    protected abstract void tournamentGamePlay(CommandHandler p_command)
            throws CommandValidationException, MapValidationException;

    /**
     * Performs the actions associated with creating a deploy order.
     *
     * @param p_command The deploy command entered by the player.
     * @param p_player  The player initiating the deploy action.
     * @throws IOException If an I/O error occurs.
     */
    protected abstract void performCreateDeploy(String p_command, ModelPlayer p_player) throws IOException;

    /**
     * Performs the actions associated with assigning countries to players.
     *
     * @param p_command          The command handler for assigning countries.
     * @param p_player           The player initiating the assign countries action.
     * @param p_isTournamentMode Indicates whether the game is in tournament mode.
     * @param p_gameState        The current game state.
     * @throws CommandValidationException If command validation fails.
     * @throws IOException                If an I/O error occurs.
     * @throws MapValidationException     If map validation fails.
     */
    protected abstract void performAssignCountries(CommandHandler p_command, ModelPlayer p_player,
            boolean p_isTournamentMode, GameState p_gameState)
            throws CommandValidationException, IOException, MapValidationException;

    /**
     * Performs the actions associated with creating players.
     *
     * @param p_command The command handler for creating players.
     * @param p_player  The player initiating the create players action.
     * @throws CommandValidationException If command validation fails.
     * @throws IOException                If an I/O error occurs.
     * @throws MapValidationException     If map validation fails.
     */
    protected abstract void createPlayers(CommandHandler p_command, ModelPlayer p_player)
            throws CommandValidationException, IOException, MapValidationException;

    /**
     * Performs the actions associated with editing neighbors of a country.
     *
     * @param p_command The command handler for editing neighbors.
     * @param p_player  The player initiating the edit neighbors action.
     * @throws CommandValidationException If command validation fails.
     * @throws MapValidationException     If map validation fails.
     * @throws IOException                If an I/O error occurs.
     */
    protected abstract void performEditNeighbours(CommandHandler p_command, ModelPlayer p_player)
            throws CommandValidationException, MapValidationException, IOException;

    /**
     * Performs the actions associated with editing a country.
     *
     * @param p_command The command handler for editing a country.
     * @param p_player  The player initiating the edit country action.
     * @throws CommandValidationException If command validation fails.
     * @throws MapValidationException     If map validation fails.
     * @throws IOException                If an I/O error occurs.
     */
    protected abstract void performEditCountry(CommandHandler p_command, ModelPlayer p_player)
            throws CommandValidationException, MapValidationException, IOException;

    /**
     * Performs the actions associated with validating the map.
     *
     * @param p_command The command handler for validating the map.
     * @param p_player  The player initiating the validate map action.
     * @throws MapValidationException     If map validation fails.
     * @throws CommandValidationException If command validation fails.
     * @throws IOException                If an I/O error occurs.
     */
    protected abstract void performValidateMap(CommandHandler p_command, ModelPlayer p_player)
            throws MapValidationException, CommandValidationException, IOException;

    /**
     * Performs the actions associated with loading a map.
     *
     * @param p_command The command handler for loading the map.
     * @param p_player  The player initiating the load map action.
     * @throws CommandValidationException If command validation fails.
     * @throws MapValidationException     If map validation fails.
     * @throws IOException                If an I/O error occurs.
     */
    protected abstract void performLoadMap(CommandHandler p_command, ModelPlayer p_player)
            throws CommandValidationException, MapValidationException, IOException;

    /**
     * Performs the actions associated with saving a map.
     *
     * @param p_command The command handler for saving the map.
     * @param p_player  The player initiating the save map action.
     * @throws CommandValidationException If command validation fails.
     * @throws MapValidationException     If map validation fails.
     * @throws IOException                If an I/O error occurs.
     */
    protected abstract void performSaveMap(CommandHandler p_command, ModelPlayer p_player)
            throws CommandValidationException, MapValidationException, IOException;

    /**
     * Performs the actions associated with editing a continent.
     *
     * @param p_command The command handler for editing a continent.
     * @param p_player  The player initiating the edit continent action.
     * @throws IOException                If an I/O error occurs.
     * @throws CommandValidationException If command validation fails.
     * @throws MapValidationException     If map validation fails.
     */
    protected abstract void performEditContinent(CommandHandler p_command, ModelPlayer p_player)
            throws IOException, CommandValidationException, MapValidationException;

    /**
     * Performs the actions associated with editing the map.
     *
     * @param p_command The command handler for editing the map.
     * @param p_player  The player initiating the edit map action.
     * @throws IOException                If an I/O error occurs.
     * @throws CommandValidationException If command validation fails.
     * @throws MapValidationException     If map validation fails.
     */
    protected abstract void performMapEdit(CommandHandler p_command, ModelPlayer p_player)
            throws IOException, CommandValidationException, MapValidationException;

}
