package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import Constants.ApplicationConstants;
import Controllers.GameEngine;
import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Services.MapService;
import Utils.CommandHandler;

/**
 * The Tournament class represents a tournament in the Risk game.
 * It manages multiple game states, each associated with a specific map and player strategies.
 */

public class Tournament implements Serializable {

	MapService d_mapService = new MapService();
	List<GameState> d_gameStateList = new ArrayList<GameState>();
	/**
     * Gets the list of game states associated with the tournament.
     *
     * @return The list of game states.
     */
	public List<GameState> getD_gameStateList() {
		return d_gameStateList;
	}
	/**
     * Sets the list of game states associated with the tournament.
     *
     * @param d_gameStateList The list of game states to set.
     */
	public void setD_gameStateList(List<GameState> d_gameStateList) {
		this.d_gameStateList = d_gameStateList;
	}
	/**
     * Parses the tournament command and performs the associated actions.
     *
     * @param p_gameState  The current game state.
     * @param p_operation  The operation part of the tournament command.
     * @param p_argument   The argument part of the tournament command.
     * @param p_gameEngine The game engine managing the game.
     * @return True if the command is successfully parsed and executed, false otherwise.
     * @throws MapValidationException     If map validation fails.
     * @throws CommandValidationException If command validation fails.
     */
	public boolean parseTournamentCommand(GameState p_gameState, String p_operation, String p_argument,
			GameEngine p_gameEngine) throws MapValidationException, CommandValidationException {

		// tournament -M listofmapfiles -P listofplayerstrategies -G numberofgames -D
		// maxnumberofturns

		if (p_operation.equalsIgnoreCase("M")) {
			return parseMapArguments(p_argument, p_gameEngine);
		}
		if (p_operation.equalsIgnoreCase("P")) {
			return parseStrategyArguments(p_gameState, p_argument, p_gameEngine);
		}
		if (p_operation.equalsIgnoreCase("G")) {
			return parseNoOfGameArgument(p_argument, p_gameEngine);
		}
		if (p_operation.equalsIgnoreCase("D")) {
			return pasrseNoOfTurnArguments(p_argument, p_gameEngine);
		}
		throw new CommandValidationException(ApplicationConstants.INVALID_COMMAND_TOURNAMENT_MODE);
	}
	/**
     * Handles parsing and validation of the "D" (max number of turns) part of the tournament command.
     *
     * @param p_argument   The argument containing the max number of turns.
     * @param p_gameEngine The game engine managing the game.
     * @return True if the argument is valid and processed, false otherwise.
     */
	private boolean pasrseNoOfTurnArguments(String p_argument, GameEngine p_gameEngine) {
		int l_maxTurns = Integer.parseInt(p_argument.split(" ")[0]);
		if (l_maxTurns >= 10 && l_maxTurns <= 50) {
			for (GameState l_gameState : d_gameStateList) {
				l_gameState.setD_maxNumberOfTurns(l_maxTurns);
				l_gameState.setD_numberOfTurnsLeft(l_maxTurns);
			}
			return true;
		} else {
			p_gameEngine.setD_gameEngineLog(
					"User entered invalid number of turns in command, Range of turns :- 10<=number of turns<=50",
					"effect");
			return false;
		}
	}
	/**
     * Handles parsing and validation of the "G" (number of games) part of the tournament command.
     *
     * @param p_argument   The argument containing the number of games.
     * @param p_gameEngine The game engine managing the game.
     * @return True if the argument is valid and processed, false otherwise.
     * @throws MapValidationException If map validation fails.
     */
	private boolean parseNoOfGameArgument(String p_argument, GameEngine p_gameEngine) throws MapValidationException {
		int l_noOfGames = Integer.parseInt(p_argument.split(" ")[0]);

		if (l_noOfGames >= 1 && l_noOfGames <= 5) {
			List<GameState> l_additionalGameStates = new ArrayList<>();

			for (int l_gameNumber = 0; l_gameNumber < l_noOfGames - 1; l_gameNumber++) {
				for (GameState l_gameState : d_gameStateList) {
					GameState l_gameStateToAdd = new GameState();
					Models.Map l_loadedMap = d_mapService.loadMap(l_gameStateToAdd,
							l_gameState.getD_map().getD_mapFile());
					l_loadedMap.setD_mapFile(l_gameState.getD_map().getD_mapFile());

					List<ModelPlayer> l_playersToCopy = getPlayersToAdd(l_gameState.getD_players());
					l_gameStateToAdd.setD_players(l_playersToCopy);

					l_gameStateToAdd.setD_loadCommand();
					l_additionalGameStates.add(l_gameStateToAdd);
				}
			}
			d_gameStateList.addAll(l_additionalGameStates);
			return true;
		} else {
			p_gameEngine.setD_gameEngineLog(
					"User entered invalid number of games in command, Range of games :- 1<=number of games<=5",
					"effect");
			return false;
		}
	}
	/**
     * Creates a new list of player instances based on the given list, copying their strategies.
     *
     * @param p_playersList The list of players to copy.
     * @return A new list of players with copied strategies.
     */
	private List<ModelPlayer> getPlayersToAdd(List<ModelPlayer> p_playersList) {
		List<ModelPlayer> p_playersToCopy = new ArrayList<>();
		for (ModelPlayer l_pl : p_playersList) {
			ModelPlayer l_player = new ModelPlayer(l_pl.getPlayerName());

			if (l_pl.getD_playerBehaviorStrategy() instanceof AggressivePlayer)
				l_player.setStrategy(new AggressivePlayer());
			else if (l_pl.getD_playerBehaviorStrategy() instanceof RandomPlayer)
				l_player.setStrategy(new RandomPlayer());
			else if (l_pl.getD_playerBehaviorStrategy() instanceof BenevolentPlayer)
				l_player.setStrategy(new BenevolentPlayer());
			else if (l_pl.getD_playerBehaviorStrategy() instanceof CheaterPlayer)
				l_player.setStrategy(new CheaterPlayer());

			p_playersToCopy.add(l_player);
		}
		return p_playersToCopy;
	}
	/**
     * Parses and validates the player strategies part of the tournament command.
     *
     * @param p_gameState  The current game state.
     * @param p_argument   The argument containing player strategies.
     * @param p_gameEngine The game engine managing the game.
     * @return True if the argument is valid and processed, false otherwise.
     * @throws MapValidationException     If map validation fails.
     * @throws CommandValidationException If command validation fails.
     */
	private boolean parseStrategyArguments(GameState p_gameState, String p_argument, GameEngine p_gameEngine) {
		String[] l_listofplayerstrategies = p_argument.split(" ");
		int l_playerStrategiesSize = l_listofplayerstrategies.length;
		List<ModelPlayer> l_playersInTheGame = new ArrayList<>();
		List<String> l_uniqueStrategies = new ArrayList<>();
		
		for (String l_strategy : l_listofplayerstrategies) {
			if(l_uniqueStrategies.contains(l_strategy)) {
				p_gameEngine.setD_gameEngineLog(
						"Repeatative strategy : " + l_strategy + " given. Kindly provide set of unique strategies.",
						"effect");
				return false;
			}
			l_uniqueStrategies.add(l_strategy);
			if (!ApplicationConstants.TOURNAMENT_PLAYER_BEHAVIORS.contains(l_strategy)) {
				p_gameEngine.setD_gameEngineLog(
						"Invalid Strategy passed in command. Only Aggressive, Benevolent, Random, Cheater strategies are allowed.",
						"effect");
				return false;
			}
		}
		if (l_playerStrategiesSize >= 2 && l_playerStrategiesSize <= 4) {
			setTournamentPlayers(p_gameEngine, l_listofplayerstrategies, p_gameState.getD_players(),
					l_playersInTheGame);
		} else {
			p_gameEngine.setD_gameEngineLog(
					"User entered invalid number of strategies in command, Range of strategies :- 2<=strategy<=4",
					"effect");
			return false;
		}
		if (l_playersInTheGame.size() < 2) {
			p_gameEngine.setD_gameEngineLog(
					"There has to be atleast 2 or more non human players eligible to play the tournament.", "effect");
			return false;
		}
		for (GameState l_gameState : d_gameStateList) {
			l_gameState.setD_players(getPlayersToAdd(l_playersInTheGame));
		}
		return true;
	}

	private void setTournamentPlayers(GameEngine p_gameEngine, String[] p_listofplayerstrategies,
                                    List<ModelPlayer> p_listOfPlayers, List<ModelPlayer> p_playersInTheGame) {
		for (String l_strategy : p_listofplayerstrategies) {
			for (ModelPlayer l_pl : p_listOfPlayers) {
				if (l_pl.getD_playerBehaviorStrategy().getPlayerBehavior().equalsIgnoreCase(l_strategy)) {
					p_playersInTheGame.add(l_pl);
					p_gameEngine.setD_gameEngineLog("Player:  " + l_pl.getPlayerName() + " with strategy: " + l_strategy
							+ " has been added in tournament.", "effect");
				}
			}
		}
	}
	/**
     * Parses and validates the map files part of the tournament command.
     *
     * @param p_argument   The argument containing map file names.
     * @param p_gameEngine The game engine managing the game.
     * @return True if the argument is valid and processed, false otherwise.
     * @throws MapValidationException If map validation fails.
     */
	private boolean parseMapArguments(String p_argument, GameEngine p_gameEngine) throws MapValidationException {
		String[] l_listOfMapFiles = p_argument.split(" ");
		int l_mapFilesSize = l_listOfMapFiles.length;

		if (l_mapFilesSize >= 1 & l_mapFilesSize <= 5) {
			for (String l_mapToLoad : l_listOfMapFiles) {
				GameState l_gameState = new GameState();
				// Loads the map if it is valid or resets the game state
				Models.Map l_loadedMap = d_mapService.loadMap(l_gameState, l_mapToLoad);
				l_loadedMap.setD_mapFile(l_mapToLoad);
				if (l_loadedMap.Validate()) {
					l_gameState.setD_loadCommand();
					p_gameEngine.setD_gameEngineLog(l_mapToLoad + " has been loaded to start the game", "effect");
					d_gameStateList.add(l_gameState);
				} else {
					d_mapService.resetMap(l_gameState, l_mapToLoad);
					return false;
				}
			}
		} else {
			p_gameEngine.setD_gameEngineLog("User entered invalid number of maps in command, Range of map :- 1<=map<=5",
					"effect");
			return false;
		}
		return true;
	}
	/**
     * Checks if the required arguments are present in the tournament command.
     *
     * @param p_operations_list The list of operations and arguments.
     * @param p_command         The tournament command.
     * @return True if the required arguments are present, false otherwise.
     */
	public boolean requiredTournamentArgPresent(List<Map<String, String>> p_operations_list, Command p_command) {
		String l_argumentKey = new String();
		if (p_operations_list.size() != 4)
			return false;

		for (Map<String, String> l_map : p_operations_list) {
			if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)
					&& p_command.checkRequiredKeysPresent(ApplicationConstants.OPERATION, l_map)) {
				l_argumentKey.concat(l_map.get(ApplicationConstants.OPERATION));
			}
		}
		if (!l_argumentKey.equalsIgnoreCase("MPGD"))
			return false;
		return true;
	}

}
