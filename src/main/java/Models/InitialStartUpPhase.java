package Models;

import Constants.ApplicationConstantsHardcoding;
import Controllers.GameEngine;
import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Utils.CommandHandler;
import Utils.CommonUtil;
import Utils.LogExceptionHandler;
import Views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * This class represents the initial startup phase of the game.
 */
public class InitialStartUpPhase extends Phase {

    /**
     * Constructor for the InitialStartUpPhase.
     *
     * @param p_gameEngine The game engine.
     * @param p_initialGameState The initial game state.
     */
    public InitialStartUpPhase(GameEngine p_gameEngine, GameState p_initialGameState) {
        super(p_gameEngine, p_initialGameState);
    }

    /**
     * Perform the action to show the map.
     *
     * @param p_command The command handler for showing the map.
     * @param p_nameOfPlayer The player's name.
     */
    @Override
    protected void performingShowMap(CommandHandler p_command, ModelPlayer p_nameOfPlayer) {
        MapView l_mapView = new MapView(d_gameState);
        l_mapView.showMap();
    }

    /**
     * Perform the action to create and deploy in the initial startup phase.
     *
     * @param p_command The command to create and deploy.
     * @param p_nameOfPlayer The player's name.
     */
    @Override
    protected void performingCreateDeploy(String p_command, ModelPlayer p_nameOfPlayer) {
        printInvalidCommandInState();
    }

    /**
     * Perform the action to handle cards in the initial startup phase.
     *
     * @param p_enteredCommand The entered command for card handling.
     * @param p_nameOfPlayer The player's name.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void performingCardHandle(String p_enteredCommand, ModelPlayer p_nameOfPlayer) throws IOException {
        printInvalidCommandInState();
    }

    /**
     * Perform the advance action in the initial startup phase.
     *
     * @param p_command The command for advancing.
     * @param p_nameOfPlayer The player's name.
     */
    @Override
    protected void performingAdvance(String p_command, ModelPlayer p_nameOfPlayer) {
        printInvalidCommandInState();
    }

    /**
     * Perform the action to edit a continent.
     *
     * @param p_givenCommand The command handler for editing a continent.
     * @param p_nameOfPlayer The player's name.
     * @throws IOException If an I/O error occurs.
     * @throws CommandValidationException If the command validation fails.
     * @throws MapValidationException If the map validation fails.
     */
    public void performingEditContinent(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer)
            throws IOException, CommandValidationException, MapValidationException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Continent, please perform `editmap` first", "effect");
            return;
        }
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new CommandValidationException(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCONTINENT);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)
                        && p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.OPERATION_REQUESTED, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED),
                            l_map.get(ApplicationConstantsHardcoding.OPERATION_REQUESTED), 1);
                } else {
                    throw new CommandValidationException(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCONTINENT);
                }
            }
        }
    }

    /**
     * Perform the action to edit the map.
     *
     * @param p_givenCommand The command handler for editing the map.
     * @param p_nameOfPlayer The player's name.
     * @throws IOException If an I/O error occurs.
     * @throws CommandValidationException If the command validation fails.
     * @throws MapValidationException If the map validation fails.
     */
    public void performingMapEdit(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer)
            throws IOException, CommandValidationException, MapValidationException {
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new CommandValidationException(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)) {
                    d_mapService.editMap(d_gameState, l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                } else {
                    throw new CommandValidationException(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITMAP);
                }
            }
        }
    }

    /**
     * Perform the action to load a map.
     *
     * @param p_givenCommand The command handler for loading a map.
     * @param p_nameOfPlayer The player's name.
     * @throws CommandValidationException If the command validation fails.
     * @throws MapValidationException If the map validation fails.
     */
    public void performingLoadMap(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer)
            throws CommandValidationException, MapValidationException {
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        boolean l_flagValidate = false;
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new CommandValidationException(ApplicationConstantsHardcoding.VALIDATION_FAILED_ERROR_LOADMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)) {
                    // Loads the map if it is valid or resets the game state
                    Models.Map l_mapToLoad = d_mapService.loadMap(d_gameState,
                            l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                    if (l_mapToLoad.Validate()) {
                        l_flagValidate = true;
                        d_gameState.setD_loadCommand();
                        d_gameEngine.setD_gameEngineLog(l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED) + " has been loaded to start the game", "effect");
                    } else {
                        d_mapService.mapReset(d_gameState, l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                    }
                    if (!l_flagValidate) {
                        d_mapService.mapReset(d_gameState, l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                    }
                } else {
                    throw new CommandValidationException(ApplicationConstantsHardcoding.VALIDATION_FAILED_ERROR_LOADMAP);
                }
            }
        }
    }

    /**
     * Perform the action to save the map.
     *
     * @param p_givenCommand The command handler for saving the map.
     * @param p_nameOfPlayer The player's name.
     * @throws CommandValidationException If the command validation fails.
     * @throws MapValidationException If the map validation fails.
     */
    public void performingSaveMap(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer)
            throws CommandValidationException, MapValidationException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("No map found to save, Please `editmap` first", "effect");
            return;
        }
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new CommandValidationException(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_SAVEMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)) {
                    boolean l_fileUpdateStatus = d_mapService.mapSave(d_gameState,
                            l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                    if (l_fileUpdateStatus) {
                        d_gameEngine.setD_gameEngineLog("Required changes have been made in the map file", "effect");
                    } else
                        System.out.println(d_gameState.getError());
                } else {
                    throw new CommandValidationException(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_SAVEMAP);
                }
            }
        }
    }

    /**
     * Perform the action to validate the map.
     *
     * @param p_givenCommand The command handler for validating the map.
     * @param p_nameOfPlayer The player's name.
     * @throws MapValidationException If the map validation fails.
     * @throws CommandValidationException If the command validation fails.
     */
    public void performingValidateMap(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer)
            throws MapValidationException, CommandValidationException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("No map found to validate, Please `loadmap` & `editmap` first", "effect");
            return;
        }
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            Models.Map l_currentMap = d_gameState.getD_map();
            if (l_currentMap == null) {
                throw new MapValidationException(ApplicationConstantsHardcoding.VALIDATION_FAILD_MAP_ERROR_EMPTY);
            } else {
                if (l_currentMap.Validate()) {
                    d_gameEngine.setD_gameEngineLog(ApplicationConstantsHardcoding.VALID_MAP_MESSAGE, "effect");
                } else {
                    throw new MapValidationException("Failed to Validate map!");
                }
            }
        } else {
            throw new CommandValidationException(ApplicationConstantsHardcoding.VALIDATION_FAILED_ERROR_VALIDATEMAP);
        }
    }

    /**
     * Perform the action to edit neighbors.
     *
     * @param p_givenCommand The command handler for editing neighbors.
     * @param p_nameOfPlayer The player's name.
     * @throws CommandValidationException If the command validation fails.
     * @throws MapValidationException If the map validation fails.
     * @throws IOException If an I/O error occurs.
     */
    public void performingEditNeighbour(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer)
            throws CommandValidationException, MapValidationException, IOException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Neighbors, please perform `editmap` first", "effect");
            return;
        }
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new CommandValidationException(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCOUNTRY);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)
                        && p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.OPERATION_REQUESTED, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstantsHardcoding.OPERATION_REQUESTED),
                            l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED), 3);
                } else {
                    throw new CommandValidationException(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCOUNTRY);
                }
            }
        }
    }

    /**
     * Performs the editing of country data based on the given command and player.
     *
     * @param p_givenCommand The command to perform the edit.
     * @param p_nameOfPlayer The player attempting the edit.
     * @throws CommandValidationException If the command validation fails.
     * @throws MapValidationException If the map validation fails.
     * @throws IOException If an I/O exception occurs.
     */
    public void performingEditCountry(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer)
            throws CommandValidationException, MapValidationException, IOException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Country, please perform `editmap` first", "effect");
            return;
        }
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new CommandValidationException(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCOUNTRY);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)
                        && p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.OPERATION_REQUESTED, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstantsHardcoding.OPERATION_REQUESTED),
                            l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED), 2);
                } else {
                    throw new CommandValidationException(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCOUNTRY);
                }
            }
        }
    }

    /**
     * Initializes the initial startup phase of the game, allowing players to enter commands.
     */
    public void initPhase()  {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        while (d_gameEngine.getD_CurrentPhase() instanceof InitialStartUpPhase) {
            try {
                System.out.println("Enter Game Commands or type 'exit' for quitting");
                String l_commandEntered = l_reader.readLine();

                handleCommand(l_commandEntered);
            } catch (CommandValidationException | MapValidationException | IOException l_exception) {
                d_gameEngine.setD_gameEngineLog(l_exception.getMessage(), "effect");
            }
        }
    }

    /**
     * Creates players for the game based on the given command and player.
     *
     * @param p_givenCommand The command to create players.
     * @param p_nameOfPlayer The player creating the players.
     * @throws CommandValidationException If the command validation fails.
     */
    public void creatingPlayers(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer) throws CommandValidationException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("No map found, Please `loadmap` before adding game players", "effect");
            return;
        }
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (CommonUtil.isNullOrEmptyCollection(l_operations_list)) {
            throw new CommandValidationException(ApplicationConstantsHardcoding.VALIDATION_FAILED_ERROR_GAMEPLAYER);
        } else {
            if (d_gameState.getD_loadCommand()) {
                for (Map<String, String> l_map : l_operations_list) {
                    if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)
                            && p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.OPERATION_REQUESTED, l_map)) {
                        d_playerService.updatePlayers(d_gameState, l_map.get(ApplicationConstantsHardcoding.OPERATION_REQUESTED),
                                l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                    } else {
                        throw new CommandValidationException(ApplicationConstantsHardcoding.VALIDATION_FAILED_ERROR_GAMEPLAYER);
                    }
                }
            } else {
                d_gameEngine.setD_gameEngineLog("Please load a valid map first via loadmap command!", "effect");
            }
        }
    }

    /**
     * Performs the assignment of countries to players based on the given command.
     *
     * @param p_givenCommand The command to perform country assignment.
     * @param p_nameOfPlayer The player assigning countries.
     * @throws CommandValidationException If the command validation fails.
     */
    public void performingAssignCountries(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer) throws CommandValidationException {
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (CommonUtil.isNullOrEmptyCollection(l_operations_list)) {
            d_playerService.assignCountries(d_gameState);
            d_playerService.assignColors(d_gameState);
            d_playerService.assignArmies(d_gameState);
            d_gameEngine.setIssueOrderPhase();
        } else {
            throw new CommandValidationException(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_ASSIGNCOUNTRIES);
        }
    }




}
