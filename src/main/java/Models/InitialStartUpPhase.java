package Models;

import Constants.ApplicationConstantsHardcoding;
import Controllers.GameEngine;
import Exceptions.InvalidCommand;
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

public class InitialStartUpPhase extends Phase{

    public InitialStartUpPhase(GameEngine p_gameEngine, GameState p_initialGameState){
        super(p_gameEngine, p_initialGameState);
    }

    @Override
    protected void performingShowMap(CommandHandler p_command, ModelPlayer p_nameOfPlayer) {
        MapView l_mapView = new MapView(d_gameState);
        l_mapView.showMap();
    }

    @Override
    protected void performingCreateDeploy(String p_command, ModelPlayer p_nameOfPlayer) {
        printInvalidCommandInState();
    }

    @Override
    protected void performingCardHandle(String p_enteredCommand, ModelPlayer p_nameOfPlayer) throws IOException {
        printInvalidCommandInState();
    }

    @Override
    protected void performingAdvance(String p_command, ModelPlayer p_nameOfPlayer) {
        printInvalidCommandInState();
    }

    public void performingEditContinent(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer) throws IOException, InvalidCommand, MapValidationException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Continent, please perform `editmap` first", "effect");
            return;
        }
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCONTINENT);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)
                        && p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.OPERATION_REQUESTED, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED),
                            l_map.get(ApplicationConstantsHardcoding.OPERATION_REQUESTED), 1);
                } else {
                    throw new InvalidCommand(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCONTINENT);
                }
            }
        }
    }

    public void performingMapEdit(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer) throws IOException, InvalidCommand, MapValidationException {
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)) {
                    d_mapService.editMap(d_gameState, l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                } else {
                    throw new InvalidCommand(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITMAP);
                }
            }
        }
    }

    public void performingLoadMap(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer) throws InvalidCommand, MapValidationException {
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        boolean l_flagValidate = false;
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstantsHardcoding.VALIDATION_FAILED_ERROR_LOADMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)) {
                    // Loads the map if it is valid or resets the game state
                    Models.Map l_mapToLoad = d_mapService.loadMap(d_gameState,
                            l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                    if (l_mapToLoad.Validate()) {
                        l_flagValidate = true;
                        d_gameState.setD_loadCommand();
                        d_gameEngine.setD_gameEngineLog(l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED)+ " has been loaded to start the game", "effect" );
                    } else {
                        d_mapService.mapReset(d_gameState, l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                    }
                    if(!l_flagValidate){
                        d_mapService.mapReset(d_gameState, l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                    }
                } else {
                    throw new InvalidCommand(ApplicationConstantsHardcoding.VALIDATION_FAILED_ERROR_LOADMAP);
                }
            }
        }
    }

    public void performingSaveMap(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer) throws InvalidCommand, MapValidationException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("No map found to save, Please `editmap` first", "effect");
            return;
        }
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_SAVEMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)) {
                    boolean l_fileUpdateStatus = d_mapService.mapSave(d_gameState,
                            l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                    if (l_fileUpdateStatus) {
                        d_gameEngine.setD_gameEngineLog("Required changes have been made in map file", "effect");
                    } else
                        System.out.println(d_gameState.getError());
                } else {
                    throw new InvalidCommand(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_SAVEMAP);
                }
            }
        }
    }

    public void performingValidateMap(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer) throws MapValidationException, InvalidCommand {
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
            throw new InvalidCommand(ApplicationConstantsHardcoding.VALIDATION_FAILED_ERROR_VALIDATEMAP);
        }
    }

    public void performingEditNeighbour(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer) throws InvalidCommand, MapValidationException, IOException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Neighbors, please perform `editmap` first", "effect");
            return;
        }
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCOUNTRY);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)
                        && p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.OPERATION_REQUESTED, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstantsHardcoding.OPERATION_REQUESTED),
                            l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED), 3);
                } else {
                    throw new InvalidCommand(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCOUNTRY);
                }
            }
        }
    }

    public void performingEditCountry(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer) throws InvalidCommand, MapValidationException, IOException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Country, please perform `editmap` first", "effect");
            return;
        }
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCOUNTRY);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)
                        && p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.OPERATION_REQUESTED, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstantsHardcoding.OPERATION_REQUESTED),
                            l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED), 2);
                } else {
                    throw new InvalidCommand(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_EDITCOUNTRY);
                }
            }
        }
    }


    public void initPhase()  {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        while (d_gameEngine.getD_CurrentPhase() instanceof InitialStartUpPhase) {
            try {
                System.out.println("Enter Game Commands or type 'exit' for quitting");
                String l_commandEntered = l_reader.readLine();

                handleCommand(l_commandEntered);
            } catch (InvalidCommand | MapValidationException | IOException l_exception) {
                d_gameEngine.setD_gameEngineLog(l_exception.getMessage(), "effect");
            }
        }
    }

    public void creatingPlayers(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer) throws InvalidCommand {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("No map found, Please `loadmap` before adding game players", "effect");
            return;
        }
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (CommonUtil.isNullOrEmptyCollection(l_operations_list)) {
            throw new InvalidCommand(ApplicationConstantsHardcoding.VALIDATION_FAILED_ERROR_GAMEPLAYER);
        } else {
            if (d_gameState.getD_loadCommand()) {
                for (Map<String, String> l_map : l_operations_list) {
                    if (p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.ARGUMENTS_PASSED, l_map)
                            && p_givenCommand.checkRequiredKeysPresent(ApplicationConstantsHardcoding.OPERATION_REQUESTED, l_map)) {
                        d_playerService.updatePlayers(d_gameState, l_map.get(ApplicationConstantsHardcoding.OPERATION_REQUESTED),
                                l_map.get(ApplicationConstantsHardcoding.ARGUMENTS_PASSED));
                    } else {
                        throw new InvalidCommand(ApplicationConstantsHardcoding.VALIDATION_FAILED_ERROR_GAMEPLAYER);
                    }
                }
            } else {
                d_gameEngine.setD_gameEngineLog("Please load a valid map first via loadmap command!", "effect");
            }
        }
    }

    public void performingAssignCountries(CommandHandler p_givenCommand, ModelPlayer p_nameOfPlayer) throws InvalidCommand{
        List<Map<String, String>> l_operations_list = p_givenCommand.getOperationsAndArguments();
        Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(d_gameState));
        if (CommonUtil.isNullOrEmptyCollection(l_operations_list)) {
            d_playerService.assignCountries(d_gameState);
            d_playerService.assignColors(d_gameState);
            d_playerService.assignArmies(d_gameState);
            d_gameEngine.setIssueOrderPhase();
        } else {
            throw new InvalidCommand(ApplicationConstantsHardcoding.COMMAND_INVALID_ERROR_ASSIGNCOUNTRIES);
        }
    }


}
