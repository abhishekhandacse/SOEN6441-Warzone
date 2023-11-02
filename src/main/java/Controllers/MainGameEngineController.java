package Controllers;

import Models.Deploy;
import Views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Logger.ConsoleLogger;
import Models.Player;
import Models.State;
import Utils.CommandHandler;
import Utils.CommonUtil;


/**
 * This class severs as the starting point of the game.
 *
 * @author Anurag Teckchandani
 */
public class MainGameEngineController {

    /**
     * d_consoleLogger: This object is used to write logs on the console.
     */
    ConsoleLogger d_consoleLogger = new ConsoleLogger();

    /**
     * d_state: It is used to manage the state of the game.
     */
    State d_state = new State();

    /**
     * d_mapController: It is used to access and modify the current map.
     */
    MapController d_mapController = new MapController();

    /**
     * d_playerController: It is used to update and access the gameplayers.
     */
    GamePlayerController d_playerController = new GamePlayerController();

    /**
     * Main Method: Accepts commands from the players and map them to corresponding logical actions.
     *
     * @param p_args the input arguments
     */
    public static void main(String[] p_args) {
        MainGameEngineController l_mainGameEngineController = new MainGameEngineController();
        l_mainGameEngineController.initializeWarzoneGamePlay();
    }

    /**
     * Getter method for d_state
     *
     * @return the d_state
     */
    public State getD_state() {
        return d_state;
    }

    /**
     * This method displays the commands available to the player and handles the commands entered by them.
     */
    private void initializeWarzoneGamePlay() {
        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        boolean l_infiniteLoop = true;

        while (l_infiniteLoop) {
            d_consoleLogger.writeLog("=========================================================================================");
            d_consoleLogger.writeLog("List of commands for playing a game.:");
            d_consoleLogger.writeLog("=========================================================================================");
            d_consoleLogger.writeLog("Initiate the map: 'loadmap filename'.");
            d_consoleLogger.writeLog("Edit the map: 'editmap filename'.");
            d_consoleLogger.writeLog("Display the loaded map: use the command 'showmap'");
            d_consoleLogger.writeLog("Include or exclude a player : gameplayer -add playername -remove playername");
            d_consoleLogger.writeLog("Allocate countries : assigncountries");
            d_consoleLogger.writeLog("Save the map : savemap filename");
            d_consoleLogger.writeLog("Load the map : loadmap filename");
            d_consoleLogger.writeLog("=========================================================================================");
            d_consoleLogger.writeLog("");
            d_consoleLogger.writeLog("");
            d_consoleLogger.writeLog("Input the game commands or input 'exit' to exit the game");

            try {
                String l_inputCommand = l_bufferedReader.readLine();

                commandHandler(l_inputCommand);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /***
     * commandHandler handles all the commands present to the player and maps them to the corresponding methods. If the user inputs 'exit', it will break the loop and exit the game.
     *
     * @param p_inputCommand: The command inputted by the user.
     *
     * @throws MapValidationException
     * @throws CommandValidationException
     * @throws IOException
     */
    private void commandHandler(final String p_inputCommand) throws MapValidationException, CommandValidationException, IOException {
        CommandHandler l_commandHandler = new CommandHandler(p_inputCommand);
        String l_rootCommand = l_commandHandler.getRootCommand();
        boolean l_isMapAvailable = d_state.getD_map() != null;

        if ("editmap".equals(l_rootCommand)) {
            editMap(l_commandHandler);
        } else if ("editcontinent".equals(l_rootCommand)) {
            if (!l_isMapAvailable) {
                d_consoleLogger.writeLog("Can't perform Editcontinent as Map is Not Available, please run 'editmap' command first.");
            } else {
                editContinent(l_commandHandler);
            }
        } else if ("editcountry".equals(l_rootCommand)) {
            if (!l_isMapAvailable) {
                d_consoleLogger.writeLog("Can't perform EditCountry as Map is Not Available, please run 'editmap' command first.");
            } else {
                editCountry(l_commandHandler);
            }
        } else if ("editneighbor".equals(l_rootCommand)) {
            if (!l_isMapAvailable) {
                d_consoleLogger.writeLog("Can't perform EditNeighbor as Map is Not Available, please run 'editmap' command first.");
            } else {
                editNeighbor(l_commandHandler);
            }
        } else if ("savemap".equals(l_rootCommand)) {
            if (!l_isMapAvailable) {
                d_consoleLogger.writeLog("Can't perform EditCountry as No Map is Available, please run 'editmap' command first.");
            } else {
                saveMap(l_commandHandler);
            }
        } else if ("loadmap".equals(l_rootCommand)) {
            loadMap(l_commandHandler);
        } else if ("showmap".equals(l_rootCommand)) {
            MapView l_mapView = new MapView(d_state);
            l_mapView.showMap();
        } else if ("validatemap".equals(l_rootCommand)) {
            if (!l_isMapAvailable) {
                d_consoleLogger.writeLog("No map available for validation; please run the  'editmap' or 'loadmap' command first.");
            } else {
                validateMap(l_commandHandler);
            }
        } else if ("assigncountries".equals(l_rootCommand)) {
            assignCountries(l_commandHandler);
        } else if ("gameplayer".equals(l_rootCommand)) {
            if (!l_isMapAvailable) {
                d_consoleLogger.writeLog("Can't add Game players, run 'loadmap' first because there is no map currently loaded.");
            } else {
                addOrRemovePlayer(l_commandHandler);
            }
        } else if ("exit".equals(l_rootCommand)) {
            d_consoleLogger.writeLog("Exit Command Entered");
            System.exit(0);
        } else {
            d_consoleLogger.writeLog("Invalid Command");
        }
    }

    /**
     * This method contans the logic to assign countries to the players and start a game loop to deploy the armies for each player.
     *
     * @param p_command Command entered by the player.
     * @throws CommandValidationException the command validation exception
     * @throws IOException                the io exception
     */
    public void assignCountries(CommandHandler p_command) throws CommandValidationException, IOException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();
        if (CommonUtil.isCollectionEmpty(l_operationsList)) {
            d_playerController.assignCountries(d_state);
            d_playerController.assignColors(d_state);

            while (!CommonUtil.isCollectionEmpty(d_state.getD_players())) {
                d_consoleLogger.writeLog("\n============================ Main Game Loop Starts ============================\n");
                // Assigning armies to players
                d_playerController.assignArmies(d_state);
                d_consoleLogger.writeLog("\nTo deploy armies use command: deploy countryID num \n");
                // Issuing order for players
                while (d_playerController.unassignedArmiesExists(d_state.getD_players())) {
                    for (Player l_player : d_state.getD_players()) {
                        if (l_player.getD_noOfUnallocatedArmies() != null && l_player.getD_noOfUnallocatedArmies() != 0)
                            l_player.issueOrder();
                    }
                }

                // Executing orders
                while (d_playerController.unexecutedOrdersExists(d_state.getD_players())) {
                    for (Player l_player : d_state.getD_players()) {
                        Deploy l_order = l_player.nextOrder();
                        if (l_order != null)
                            l_order.execute(d_state, l_player);
                    }
                }
                MapView l_mapView = new MapView(d_state, d_state.getD_players());
                l_mapView.showMap();
                d_consoleLogger.writeLog("To continue for next turn, input 'Y'/'y', or to exit, input 'N'/'n'.");
                BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
                String l_next = l_reader.readLine();
                if (l_next.equalsIgnoreCase("N"))
                    break;
            }
        } else {
            throw new CommandValidationException("Invalid command. To assign countries please execute the 'assigncountries' command.");
        }
    }

    /**
     * This method adds or remove players in the game using the 'gameplayer' command.
     *
     * @param p_command: Command entered by the player.
     * @throws CommandValidationException
     */
    private void addOrRemovePlayer(CommandHandler p_command) throws CommandValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();
        if (CommonUtil.isCollectionEmpty(l_operationsList)) {
            throw new CommandValidationException("Invalid command. To manage game players, please execute the 'gameplayer' command in the provided format: gameplayer -add playername -remove playername");
        } else {
            for (Map<String, String> l_map : l_operationsList) {
                if (p_command.checkRequiredKeysPresent("arguments", l_map)
                        && p_command.checkRequiredKeysPresent("operation", l_map)) {
                    d_playerController.updatePlayers(d_state, l_map.get("operation"),
                            l_map.get("arguments"));
                } else {
                    throw new CommandValidationException("Invalid command. To manage game players, please execute the 'gameplayer' command in the provided format: gameplayer -add playername -remove playername");
                }
            }
        }
    }

    /**
     * loadMap method loads the map already present in the resources to begin the game. If the map doesn't exist it will throw an error message.
     *
     * @param p_command Command entered by the player.
     * @throws CommandValidationException
     */
    public void loadMap(CommandHandler p_command) throws CommandValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();

        if (Objects.isNull(l_operationsList) || l_operationsList.isEmpty()) {
            throw new CommandValidationException("Invalid command. Please execute the 'loadmap' command in the provided format: loadmap filename");
        } else {
            for (Map<String, String> l_map : l_operationsList) {
                if (p_command.checkRequiredKeysPresent("arguments", l_map)) {
                    try {

                        // Loads the map if it is valid or resets the game state
                        Models.Map l_mapToLoad = d_mapController.loadMap(d_state,
                                l_map.get("arguments"));
                        if (l_mapToLoad.Validate()) {
                            d_consoleLogger.writeLog("Map loaded successfully.\n");
                        } else {
                            d_mapController.resetMap(d_state);
                        }
                    } catch (MapValidationException l_e) {
                        d_mapController.resetMap(d_state);
                    }
                } else {
                    throw new CommandValidationException("Invalid command. Please execute the 'loadmap' command in the provided format: loadmap filename.");
                }
            }
        }
    }

    /**
     * This method validates the map edited by the player, if the map is validated user can proceed else player has to make changes to validate the map.
     *
     * @param p_command: Command entered by the player.
     * @throws MapValidationException
     * @throws CommandValidationException
     */
    private void validateMap(CommandHandler p_command) throws MapValidationException, CommandValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();
        if (Objects.isNull(l_operationsList) || l_operationsList.isEmpty()) {
            Models.Map l_currentMap = d_state.getD_map();
            if (l_currentMap == null) {
                throw new MapValidationException("Map Not found. Please load a valid map!");
            } else {
                if (l_currentMap.Validate()) {
                    d_consoleLogger.writeLog("Validated Map Successfully!");
                } else {
                    d_consoleLogger.writeLog("Map validation is Unsuccessfull.");
                }
            }
        } else {
            throw new CommandValidationException("Invalid command. The 'validatemap' cannot have any arguements.");
        }
    }

    /**
     * EditMap will take the filename by the player, if the file doesn't exist it will
     *
     * @param p_command: Command entered by the player.
     * @throws IOException
     * @throws CommandValidationException
     */
    private void editMap(CommandHandler p_command) throws IOException, CommandValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();

        if (Objects.isNull(l_operationsList) || l_operationsList.isEmpty()) {
            throw new CommandValidationException("Invalid command. Please execute the 'editmap' command in the provided format: editmap filename.");
        } else {
            for (Map<String, String> l_map : l_operationsList) {
                if (p_command.checkRequiredKeysPresent("arguments", l_map)) {
                    d_mapController.editMap(d_state, l_map.get("arguments"));
                } else {
                    throw new CommandValidationException("Invalid command. Please execute the 'editmap' command in the provided format: editmap filename.");
                }
            }
        }
    }

    /**
     * Save map.
     *
     * @param p_command the p command
     * @throws CommandValidationException the command validation exception
     * @throws MapValidationException     the map validation exception
     */
    public void saveMap(CommandHandler p_command) throws CommandValidationException, MapValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();

        if (l_operationsList == null || l_operationsList.isEmpty()) {
            d_consoleLogger.writeLog("Invalid command. Please execute the 'savemap' command in the provided format: savemap filename.");
        } else {
            for (Map<String, String> l_map : l_operationsList) {
                if (p_command.checkRequiredKeysPresent("arguments", l_map)) {
                    boolean l_fileUpdateStatus = d_mapController.saveMap(d_state,
                            l_map.get("arguments"));
                    if (l_fileUpdateStatus)
                        d_consoleLogger.writeLog("The map file is updated with the changes.");
                    else
                        d_consoleLogger.writeLog(d_state.getError());
                } else {
                    d_consoleLogger.writeLog("Invalid command. Please execute the 'savemap' command in the provided format: savemap filename.");
                }
            }
        }
    }

    /**
     * This method contains the logic add neibhor to a country and if neighbor doesn't exist, show an error message.
     *
     * @param p_command: Command entered by the player.
     * @throws CommandValidationException
     * @throws MapValidationException
     */
    private void editNeighbor(CommandHandler p_command) throws CommandValidationException, MapValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();
        if (Objects.isNull(l_operationsList) || l_operationsList.isEmpty()) {
            throw new CommandValidationException("Invalid command. Please execute the 'editcountry' command in the provided format: editcountry -add countryID continentID -remove countryID");
        } else {
            for (Map<String, String> l_map : l_operationsList) {
                if (p_command.checkRequiredKeysPresent("arguments", l_map)
                        && p_command.checkRequiredKeysPresent("operation", l_map)) {
                    d_mapController.editNeighbour(d_state, l_map.get("operation"),
                            l_map.get("arguments"));
                } else {
                    throw new CommandValidationException("Invalid command. Please execute the 'editcountry' command in the provided format: editcountry -add countryID continentID -remove countryID");
                }
            }
        }
    }

    /**
     * This Method adds/remove the country from a continent using the 'editcountry' command.
     *
     * @param p_command Command entered by the player.
     * @throws IOException
     * @throws CommandValidationException
     * @throws MapValidationException
     */
    public void editCountry(CommandHandler p_command) throws IOException, CommandValidationException, MapValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();
        if (Objects.isNull(l_operationsList) || l_operationsList.isEmpty()) {
            throw new CommandValidationException("Invalid command. Please execute the 'editcountry' command in the provided format: editcountry -add countryID continentID -remove countryID");
        } else {
            for (Map<String, String> l_map : l_operationsList) {
                if (p_command.checkRequiredKeysPresent("arguments", l_map)
                        && p_command.checkRequiredKeysPresent("operation", l_map)) {
                    d_mapController.editCountry(d_state, l_map.get("operation"),
                            l_map.get("arguments"));
                } else {
                    throw new CommandValidationException("Invalid command. Please execute the 'editcountry' command in the provided format: editcountry -add countryID continentID -remove countryID");
                }
            }
        }
    }

    /**
     * This method contains the logic to add new continent to the map. The player has to input the continentID and the continentValue correctly inorder to add a continent.
     *
     * @param p_command Command entered by the player.
     * @throws IOException
     * @throws CommandValidationException
     * @throws MapValidationException
     */
    public void editContinent(CommandHandler p_command) throws IOException, CommandValidationException, MapValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();

        if (l_operationsList == null || l_operationsList.isEmpty()) {
            throw new CommandValidationException("Invalid command. Please execute the 'editcontinent' command in the provided format: editcontinent -add continentID continentValue -remove continentID");
        } else {
            for (Map<String, String> l_map : l_operationsList) {
                if (p_command.checkRequiredKeysPresent("arguments", l_map) && p_command.checkRequiredKeysPresent("operation", l_map)) {
                    d_mapController.editContinent(d_state, l_map.get("arguments"),
                            l_map.get("operation"));
                } else {
                    throw new CommandValidationException("Invalid command. Please execute the 'editcontinent' command in the provided format: editcontinent -add continentID continentValue -remove continentID");
                }
            }
        }
    }


}
