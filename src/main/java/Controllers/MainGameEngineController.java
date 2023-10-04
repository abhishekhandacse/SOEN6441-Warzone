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
import Models.State;
import Utils.CommandHandler;


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
				saveMap();
			}
		} else if ("loadmap".equals(l_rootCommand)) {
			loadMap();
		} else if ("showmap".equals(l_rootCommand)) {
			MapView l_mapView = new MapView(d_state);
			l_mapView.showMap();
		} else if ("validatemap".equals(l_rootCommand)) {
			if (!l_isMapAvailable) {
				consoleLogger.writeLog("No map available for validation; please run the  'editmap' or 'loadmap' command first.");
			} else {
				validateMap();
			}
		} else if ("assigncountries".equals(l_rootCommand)) {
			assignCountries();
		} else if ("gameplayer".equals(l_rootCommand)) {
			if (!l_isMapAvailable) {
				consoleLogger.writeLog("Can't add Game players, run 'loadmap' first because there is no map currently loaded.");
			} else {
				addOrRemovePlayer();
			}
		} else if ("exit".equals(l_rootCommand)) {
			consoleLogger.writeLog("Exit Command Entered");
			System.exit(0);
		} else {
			consoleLogger.writeLog("Invalid Command");
		} 
    }

    private void assignCountries() {
    }

    private void addOrRemovePlayer() {
    }

    private void loadMap() {
    }

    private void validateMap() {
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

    private void saveMap() {
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
