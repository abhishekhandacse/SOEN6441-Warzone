package Controllers;

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
import Models.Order;
import Models.Player;
import Models.State;
import Utils.CommandHandler;
import Utils.CommonUtil;


/**
 *  This class severs as the starting point of the game.
 */
public class MainGameEngineController {

    ConsoleLogger consoleLogger = new ConsoleLogger();

    State d_state = new State();

	MapController d_mapController = new MapController();

	GamePlayerController d_playerController = new GamePlayerController();

	public State getD_state() {
		return d_state;
	}

    
    /**
     * Main Method: Accepts commands from the players and map them to corresponding logical actions.
     * 
     * @param p_args 
     */
    public static void main(String[] p_args) {
        MainGameEngineController l_mainGameEngineController = new MainGameEngineController();
        l_mainGameEngineController.initializeWarzoneGamePlay();
    }

    private void initializeWarzoneGamePlay(){
        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        var l_infiniteLoop = true;

        while (l_infiniteLoop){
            System.out.println("Input the game commands or input 'exit' to exit the game");
            try {
                String l_inputCommand = l_bufferedReader.readLine();

                commandHandler(l_inputCommand);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    private  void commandHandler(final String p_inputCommand) throws MapValidationException, CommandValidationException, IOException{
        CommandHandler l_commandHandler = new CommandHandler(p_inputCommand);
        String l_rootCommand = l_commandHandler.getRootCommand();
        boolean l_isMapAvailable = d_state.getD_map() != null;  

        if ("editmap".equals(l_rootCommand)) {
			editMap(l_commandHandler);
		} else if ("editcontinent".equals(l_rootCommand)) {
			if (!l_isMapAvailable) {
				consoleLogger.writeLog("Can't perform Editcontinent as Map is Not Available, please run 'editmap' command first.");
			} else {
				editContinent(l_commandHandler);
			}
		} else if ("editcountry".equals(l_rootCommand)) {
			if (!l_isMapAvailable) {
				consoleLogger.writeLog("Can't perform EditCountry as Map is Not Available, please run 'editmap' command first.");
			} else {
				editCountry(l_commandHandler);
			}
		} else if ("editneighbor".equals(l_rootCommand)) {
			if (!l_isMapAvailable) {
				consoleLogger.writeLog("Can't perform EditNeighbor as Map is Not Available, please run 'editmap' command first.");
			} else {
				editNeighbor(l_commandHandler);
			}
		}else if ("savemap".equals(l_rootCommand)) {
			if (!l_isMapAvailable) {
				consoleLogger.writeLog("Can't perform EditCountry as No Map is Available, please run 'editmap' command first.");
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
				consoleLogger.writeLog("No map available for validation; please run the  'editmap' or 'loadmap' command first.");
			} else {
				validateMap(l_commandHandler);
			}
		} else if ("assigncountries".equals(l_rootCommand)) {
			assignCountries(l_commandHandler);
		} else if ("gameplayer".equals(l_rootCommand)) {
			if (!l_isMapAvailable) {
				consoleLogger.writeLog("Can't add Game players, run 'loadmap' first because there is no map currently loaded.");
			} else {
				addOrRemovePlayer(l_commandHandler);
			}
		} else if ("exit".equals(l_rootCommand)) {
			consoleLogger.writeLog("Exit Command Entered");
			System.exit(0);
		} else {
			consoleLogger.writeLog("Invalid Command");
		} 
    }

    private void assignCountries(CommandHandler p_command) throws CommandValidationException, IOException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();
		if (CommonUtil.isCollectionEmpty(l_operationsList)) {
			d_playerController.assignCountries(d_state);
			d_playerController.assignColors(d_state);

			while (!CommonUtil.isCollectionEmpty(d_state.getD_players())) {
				consoleLogger.writeLog("\n============================ Main Game Loop Starts ============================\n");
				// Assigning armies to players
				d_playerController.assignArmies(d_state);

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
						Order l_order = l_player.nextOrder();
						if (l_order != null)
							l_order.execute(d_state, l_player);
					}
				}
				MapView l_mapView = new MapView(d_state, d_state.getD_players());
				l_mapView.showMap();
				consoleLogger.writeLog("To continue for next turn, input 'Y'/'y', or to exit, input 'N'/'n'.");
				BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
				String l_next = l_reader.readLine();
				if (l_next.equalsIgnoreCase("N"))
					break;
			}
		} else {
			throw new CommandValidationException("Invalid command. To assign countries please execute the 'assigncountries' command.");
		}
    }

    private void addOrRemovePlayer(CommandHandler p_command) throws CommandValidationException  {
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

    private void loadMap(CommandHandler p_command) throws CommandValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();

		if (Objects.isNull(l_operationsList)  || l_operationsList.isEmpty()) {
			throw new CommandValidationException("Invalid command. Please execute the 'loadmap' command in the provided format: loadmap filename");
		} else {
			for (Map<String, String> l_map : l_operationsList) {
				if (p_command.checkRequiredKeysPresent("arguments", l_map)) {
					try {

						// Loads the map if it is valid or resets the game state
						Models.Map l_mapToLoad = d_mapController.loadMap(d_state,
								l_map.get("arguments"));
						if (l_mapToLoad.Validate()) {
							consoleLogger.writeLog("Map loaded successfully.\n");
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

    private void validateMap(CommandHandler p_command) throws MapValidationException, CommandValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();
		if (Objects.isNull(l_operationsList)  || l_operationsList.isEmpty()) {
			Models.Map l_currentMap = d_state.getD_map();
			if (l_currentMap == null) {
				throw new MapValidationException("Map Not found. Please load a valid map!");
			} else {
				if (l_currentMap.Validate()) {
					consoleLogger.writeLog("Validated Map Successfully!");
				} else {
					consoleLogger.writeLog("Map validation is Unsuccessfull.");
				}
			}
		} else {
			throw new CommandValidationException("Invalid command. The 'validatemap' cannot have any arguements.");
		}
    }

    private void editMap(CommandHandler p_command) throws IOException, CommandValidationException{
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

    private void saveMap(CommandHandler p_command) throws CommandValidationException, MapValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();

		if (l_operationsList == null  || l_operationsList.isEmpty()) {
			throw new CommandValidationException("Invalid command. Please execute the 'savemap' command in the provided format: savemap filename.");
		} else {
			for (Map<String, String> l_map : l_operationsList) {
				if (p_command.checkRequiredKeysPresent("arguments", l_map)) {
					boolean l_fileUpdateStatus = d_mapController.saveMap(d_state,
							l_map.get("arguments"));
					if (l_fileUpdateStatus)
						consoleLogger.writeLog("The map file is updated with the changes.");
					else
						consoleLogger.writeLog(d_state.getError());
				} else {
					throw new CommandValidationException("Invalid command. Please execute the 'savemap' command in the provided format: savemap filename.");
				}
			}
		}
    }

    private void showMap() {
    }

    private void editNeighbor(CommandHandler p_command) throws CommandValidationException, MapValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();
		if (Objects.isNull(l_operationsList)  || l_operationsList.isEmpty()) {
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

    private void editCountry(CommandHandler p_command) throws IOException, CommandValidationException, MapValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();
		if (Objects.isNull(l_operationsList)  || l_operationsList.isEmpty()) {
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

    private void editContinent(CommandHandler p_command) throws IOException, CommandValidationException, MapValidationException {
        List<Map<String, String>> l_operationsList = p_command.getOperationsAndArguments();

		if (l_operationsList == null  || l_operationsList.isEmpty()) {
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
