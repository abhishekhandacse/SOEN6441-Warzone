package Models;

import Constants.ApplicationConstants;
import Controllers.GameEngine;
import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Services.GameService;
import Utils.CommandHandler;
import Utils.CommonUtil;
import Utils.LogHandlerException;
import Views.MapView;
import Views.TournamentView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * This class represents the initial startup phase of the game.
 */
public class InitStartUpPhase extends Phase {

	/**
	 * Constructor
	 * @param p_gameEngine - gameengine
	 * @param p_gameState - game state
	 */
	public InitStartUpPhase(GameEngine p_gameEngine, GameState p_gameState) {
		super(p_gameEngine, p_gameState);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performLoadGame(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException {
        List<java.util.Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_LOADGAME);
        }

        for (Map<String, String> l_map : l_operations_list) {
            if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)) {
                String l_filename = l_map.get(ApplicationConstants.ARGUMENTS);

                try{
                    Phase l_phase= GameService.loadGame(l_filename);

                    this.d_gameEngine.loadPhase(l_phase);
                } catch (ClassNotFoundException l_e) {
                    l_e.printStackTrace();
                }
            }
        }
    }

	/**
	 * {@inheritDoc}
	 */
	public void initPhase(boolean isTournamentMode) {
		BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));

		while (d_gameEngine.getD_CurrentPhase() instanceof InitStartUpPhase) {
			System.out.println("===================================================================================");
            System.out.println("List of commands for playing a game.");
            System.out.println("===================================================================================");
            System.out.println("Initiate the map: 'loadmap filename'.");
            System.out.println("Edit the map: 'editmap filename'.");
            System.out.println("Display the loaded map: use the command 'showmap'");
            System.out.println("Include or exclude a player : 'gameplayer -add playername -remove playername' ");
            System.out.println("Allocate countries : 'assigncountries' ");
            System.out.println("Save the map : 'savemap filename' ");
            System.out.println("Load the map : 'loadmap filename' ");
            System.out.println("===================================================================================");
            System.out.println("");
            System.out.println("");
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
	 * {@inheritDoc}
	 */
	public void performLoadMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException {
		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		Thread.setDefaultUncaughtExceptionHandler(new LogHandlerException(d_gameState));
		if (null == l_operations_list || l_operations_list.isEmpty()) {
			throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_LOADMAP);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)) {
					// Loads the map if it is valid or resets the game state
					Models.Map l_mapToLoad = d_mapService.loadMap(d_gameState,
							l_map.get(ApplicationConstants.ARGUMENTS));
					if (l_mapToLoad.Validate()) {
						d_gameState.setD_loadCommand();
						d_gameEngine.setD_gameEngineLog(
								l_map.get(ApplicationConstants.ARGUMENTS) + " has been loaded to start the game",
								"effect");
					} else {
						d_mapService.resetMap(d_gameState, l_map.get(ApplicationConstants.ARGUMENTS));
					}
				} else {
					throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_LOADMAP);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void performMapEdit(CommandHandler p_command, ModelPlayer p_player) throws IOException, CommandValidationException, MapValidationException {
		List<java.util.Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		Thread.setDefaultUncaughtExceptionHandler(new LogHandlerException(d_gameState));

		if (l_operations_list == null || l_operations_list.isEmpty()) {
			throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_EDITMAP);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)) {
					d_mapService.editMap(d_gameState, l_map.get(ApplicationConstants.ARGUMENTS));
				} else {
					throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_EDITMAP);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void performEditContinent(CommandHandler p_command, ModelPlayer p_player)
			throws IOException, CommandValidationException, MapValidationException {
		if (!l_isMapLoaded) {
			d_gameEngine.setD_gameEngineLog("Can not Edit Continent, please perform `editmap` first", "effect");
			return;
		}

		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		Thread.setDefaultUncaughtExceptionHandler(new LogHandlerException(d_gameState));
		if (l_operations_list == null || l_operations_list.isEmpty()) {
			throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCONTINENT);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)
						&& p_command.checkRequiredKeysPresent(ApplicationConstants.OPERATION, l_map)) {
					d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstants.ARGUMENTS),
							l_map.get(ApplicationConstants.OPERATION), 1);
				} else {
					throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCONTINENT);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void performEditCountry(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException {
		if (!l_isMapLoaded) {
			d_gameEngine.setD_gameEngineLog("Can not Edit Country, please perform `editmap` first", "effect");
			return;
		}

		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		Thread.setDefaultUncaughtExceptionHandler(new LogHandlerException(d_gameState));
		if (null == l_operations_list || l_operations_list.isEmpty()) {
			throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCOUNTRY);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)
						&& p_command.checkRequiredKeysPresent(ApplicationConstants.OPERATION, l_map)) {
					d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstants.OPERATION),
							l_map.get(ApplicationConstants.ARGUMENTS), 2);
				} else {
					throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCOUNTRY);
				}
			}
		}
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performSaveGame(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException {
        List<java.util.Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        Thread.setDefaultUncaughtExceptionHandler(new LogHandlerException(d_gameState));

        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_SAVEGAME);
        }

        for (Map<String, String> l_map : l_operations_list) {
            if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)) {
                String l_filename = l_map.get(ApplicationConstants.ARGUMENTS);
                GameService.saveGame(this, l_filename);
                d_gameEngine.setD_gameEngineLog("Game Saved Successfully to "+l_filename, "effect");
            } else {
                throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_SAVEGAME);
            }
        }
    }

	/**
	 * {@inheritDoc}
	 */
	public void performEditNeighbours(CommandHandler p_command, ModelPlayer p_player)
			throws CommandValidationException, MapValidationException, IOException {
		if (!l_isMapLoaded) {
			d_gameEngine.setD_gameEngineLog("Can not Edit Neighbors, please perform `editmap` first", "effect");
			return;
		}

		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		Thread.setDefaultUncaughtExceptionHandler(new LogHandlerException(d_gameState));
		if (null == l_operations_list || l_operations_list.isEmpty()) {
			throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCOUNTRY);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)
						&& p_command.checkRequiredKeysPresent(ApplicationConstants.OPERATION, l_map)) {
					d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstants.OPERATION),
							l_map.get(ApplicationConstants.ARGUMENTS), 3);
				} else {
					throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCOUNTRY);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void performSaveMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException {
		if (!l_isMapLoaded) {
			d_gameEngine.setD_gameEngineLog("No map found to save, Please `editmap` first", "effect");
			return;
		}

		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		Thread.setDefaultUncaughtExceptionHandler(new LogHandlerException(d_gameState));
		if (null == l_operations_list || l_operations_list.isEmpty()) {
			throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_SAVEMAP);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)) {
					boolean l_fileUpdateStatus = d_mapService.saveMap(d_gameState,
							l_map.get(ApplicationConstants.ARGUMENTS));
					if (l_fileUpdateStatus) {
						d_gameEngine.setD_gameEngineLog("Required changes have been made in map file", "effect");
					} else
						System.out.println(d_gameState.getError());
				} else {
					throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_SAVEMAP);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void performValidateMap(CommandHandler p_command, ModelPlayer p_player) throws MapValidationException, CommandValidationException {
		if (!l_isMapLoaded) {
			d_gameEngine.setD_gameEngineLog("No map found to validate, Please `loadmap` & `editmap` first", "effect");
			return;
		}

		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		Thread.setDefaultUncaughtExceptionHandler(new LogHandlerException(d_gameState));
		if (null == l_operations_list || l_operations_list.isEmpty()) {
			Models.Map l_currentMap = d_gameState.getD_map();
			if (l_currentMap == null) {
				throw new MapValidationException(ApplicationConstants.INVALID_MAP_ERROR_EMPTY);
			} else {
				if (l_currentMap.Validate()) {
					d_gameEngine.setD_gameEngineLog(ApplicationConstants.VALID_MAP, "effect");
				} else {
					throw new MapValidationException("Failed to Validate map!");
				}
			}
		} else {
			throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_VALIDATEMAP);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void performAssignCountries(CommandHandler p_command, ModelPlayer p_player, boolean p_istournamentmode,
                                          GameState p_gameState) throws CommandValidationException {
		if(d_gameState != null && d_gameState.d_players != null && d_gameState.d_players.size() < 2){
				throw new CommandValidationException("Cannot Assign countries with only player");
		}
		else if (p_gameState.getD_loadCommand()) {
			List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
			Thread.setDefaultUncaughtExceptionHandler(new LogHandlerException(d_gameState));
			
			if (CommonUtil.isCollectionEmpty(l_operations_list) || p_istournamentmode) {
				d_gameEngine.setD_gameState(p_gameState);
				d_gameEngine.setD_isTournamentMode(p_istournamentmode);
				if(d_playerService.assignCountries(p_gameState)) {
					d_playerService.assignColors(p_gameState);
					d_playerService.assignArmies(p_gameState);
					d_gameEngine.setIssueOrderPhase(p_istournamentmode);
				}
			} else {
				throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_ASSIGNCOUNTRIES);
			}
		} else {
			d_gameEngine.setD_gameEngineLog("Please load a valid map first via loadmap command!", "effect");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void performCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException {
        printCommandValidationExceptionInState();
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performShowMap(CommandHandler p_command, ModelPlayer p_player) {
		MapView l_mapView = new MapView(d_gameState);
		l_mapView.showMap();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performAdvance(String p_command, ModelPlayer p_player) {
		printCommandValidationExceptionInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performCreateDeploy(String p_command, ModelPlayer p_player) {
		printCommandValidationExceptionInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void tournamentGamePlay(CommandHandler p_command) throws CommandValidationException, MapValidationException {

		if (d_gameState.getD_players() != null && d_gameState.getD_players().size() > 1) {
			List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
			boolean l_parsingSuccessful = false;
			Thread.setDefaultUncaughtExceptionHandler(new LogHandlerException(d_gameState));

			if (CommonUtil.isCollectionEmpty(l_operations_list)
					&& !d_tournament.requiredTournamentArgPresent(l_operations_list, p_command)) {
				throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_TOURNAMENT_MODE);
			} else {
				for (Map<String, String> l_map : l_operations_list) {
					if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)
							&& p_command.checkRequiredKeysPresent(ApplicationConstants.OPERATION, l_map)) {
						l_parsingSuccessful = d_tournament.parseTournamentCommand(d_gameState,
								l_map.get(ApplicationConstants.OPERATION), l_map.get(ApplicationConstants.ARGUMENTS),
								d_gameEngine);
						if (!l_parsingSuccessful)
							break;

					} else {
						throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_TOURNAMENT_MODE);
					}
				}
			}
			if (l_parsingSuccessful) {
				for (GameState l_gameState : d_tournament.getD_gameStateList()) {
					d_gameEngine.setD_gameEngineLog(
							"\nStarting New Game on map : " + l_gameState.getD_map().getD_mapFile() + " .........\n",
							"effect");
					performAssignCountries(new CommandHandler("assigncountries"), null, true, l_gameState);

					d_gameEngine.setD_gameEngineLog(
							"\nGame Completed on map : " + l_gameState.getD_map().getD_mapFile() + " .........\n",
							"effect");
				}
				d_gameEngine.setD_gameEngineLog("************ Tournament Completed ************", "effect");
				TournamentView l_tournamentView = new TournamentView(d_tournament);
				l_tournamentView.viewTournament();
				d_tournament = new Tournament();
			}
		} else {
			d_gameEngine.setD_gameEngineLog("Please add 2 or more players first in the game.", "effect");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void createPlayers(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, IOException {

		List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

		Thread.setDefaultUncaughtExceptionHandler(new LogHandlerException(d_gameState));
		if (CommonUtil.isCollectionEmpty(l_operations_list)) {
			throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_GAMEPLAYER);
		} else {
			for (Map<String, String> l_map : l_operations_list) {
				if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)
						&& p_command.checkRequiredKeysPresent(ApplicationConstants.OPERATION, l_map)) {
					d_playerService.updatePlayers(d_gameState, l_map.get(ApplicationConstants.OPERATION),
							l_map.get(ApplicationConstants.ARGUMENTS));
				} else {
					throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_ERROR_GAMEPLAYER);
				}
			}
		}
	}
	
}