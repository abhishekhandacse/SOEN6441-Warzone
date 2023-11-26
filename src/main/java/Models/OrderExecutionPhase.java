package Models;

import Controllers.GameEngine;
import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Services.GameService;
import Utils.CommandHandler;
import Utils.CommonUtil;
import Utils.LogHandlerException;
import Views.MapView;
import java.util.Map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import Constants.ApplicationConstants;

/**
 * Order Execution Phase implementation for GamePlay using State Pattern.
 */
public class OrderExecutionPhase extends Phase {

	public OrderExecutionPhase(GameEngine p_gameEngine, GameState p_gameState) {
		super(p_gameEngine, p_gameState);
	}

	@Override
	public void initPhase(boolean isTournamentMode) {
		executeOrders();

		MapView l_map_view = new MapView(d_gameState);
		l_map_view.showMap();

		if (this.checkEndOftheGame(d_gameState))
			return;


		try {
			String l_continue = this.continueForNextTurn(isTournamentMode);
			if (l_continue.equalsIgnoreCase("N") && isTournamentMode) {
				d_gameEngine.setD_gameEngineLog("Start Up Phase", "phase");
				d_gameEngine.setD_CurrentPhase(new InitStartUpPhase(d_gameEngine, d_gameState));
			} else if (l_continue.equalsIgnoreCase("N") && !isTournamentMode) {
				d_gameEngine.setStartUpPhase();

			} else if (l_continue.equalsIgnoreCase("Y")) {
				System.out.println("\n" + d_gameState.getD_numberOfTurnsLeft() + " Turns are left for this game. Continuing for next Turn.\n");
				d_playerService.assignArmies(d_gameState);
				d_gameEngine.setIssueOrderPhase(isTournamentMode);
			} else {
				System.out.println("Invalid Input");
			}
		}  catch (IOException l_e) {
			System.out.println("Invalid Input");
		}

	}

	/**
	 * Invokes order execution logic for all unexecuted orders.
	 */
	protected void executeOrders() {
		addNeutralPlayer(d_gameState);
		// Executing orders
		d_gameEngine.setD_gameEngineLog("\nStarting Execution Of Orders.....", "start");
		while (d_playerService.unexecutedOrdersExists(d_gameState.getD_players())) {
			for (ModelPlayer l_player : d_gameState.getD_players()) {
				Order l_order = l_player.next_order();
				if (l_order != null) {
					l_order.printOrder();
					d_gameState.updateLog(l_order.orderExecutionLog(), "effect");
					l_order.execute(d_gameState);
				}
			}
		}
		d_playerService.resetPlayersFlag(d_gameState.getD_players());
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
	 * Add neutral player to game state.
	 *
	 * @param p_gameState GameState
	 */
	public void addNeutralPlayer(GameState p_gameState) {
		ModelPlayer l_player = p_gameState.getD_players().stream()
				.filter(l_pl -> l_pl.getPlayerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);
		if (CommonUtil.isNull(l_player)) {
			ModelPlayer l_neutralPlayer = new ModelPlayer("Neutral");
			l_neutralPlayer.setStrategy(new HumanPlayer());
			l_neutralPlayer.setD_moreOrders(false);
			p_gameState.getD_players().add(l_neutralPlayer);
		} else {
			return;
		}
	}

	private String continueForNextTurn(boolean isTournamentMode) throws IOException {
		String l_continue = new String();
		if (isTournamentMode) {
			d_gameState.setD_numberOfTurnsLeft(d_gameState.getD_numberOfTurnsLeft() - 1);
			l_continue = d_gameState.getD_numberOfTurnsLeft() == 0 ? "N" : "Y";
		} else {
			System.out.println("Press Y/y if you want to continue for next turn or else press N/n");
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
			l_continue = l_reader.readLine();
		}
		return l_continue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performLoadGame(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException {
		printCommandValidationExceptionInState();
	}

	@Override
	protected void performShowMap(CommandHandler p_command, ModelPlayer p_player) {
		MapView l_mapView = new MapView(d_gameState);
		l_mapView.showMap();
	}

	@Override
	protected void performCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException {
		printCommandValidationExceptionInState();
	}

	@Override
	protected void performAdvance(String p_command, ModelPlayer p_player) {
		printCommandValidationExceptionInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createPlayers(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException {
		printCommandValidationExceptionInState();
	}

	@Override
	protected void performCreateDeploy(String p_command, ModelPlayer p_player) {
		printCommandValidationExceptionInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performAssignCountries(CommandHandler p_command, ModelPlayer p_player, boolean isTournamentMode,
                                             GameState p_gameState) throws CommandValidationException, IOException {
		printCommandValidationExceptionInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performEditNeighbours(CommandHandler p_command, ModelPlayer p_player)
			throws CommandValidationException, MapValidationException, IOException {
		printCommandValidationExceptionInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performEditCountry(CommandHandler p_command, ModelPlayer p_player)
			throws CommandValidationException, MapValidationException, IOException {
		printCommandValidationExceptionInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performEditContinent(CommandHandler p_command, ModelPlayer p_player)
			throws IOException, CommandValidationException, MapValidationException {
		printCommandValidationExceptionInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performLoadMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException {
		printCommandValidationExceptionInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performMapEdit(CommandHandler p_command, ModelPlayer p_player) throws IOException, CommandValidationException, MapValidationException {
		printCommandValidationExceptionInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performSaveMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException {
		printCommandValidationExceptionInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performValidateMap(CommandHandler p_command, ModelPlayer p_player) throws MapValidationException, CommandValidationException {
		printCommandValidationExceptionInState();
	}

	protected Boolean checkEndOftheGame(GameState p_gameState) {
		Integer l_totalCountries = p_gameState.getD_map().getD_countries().size();
		d_playerService.updatePlayersInGame(p_gameState);
		for (ModelPlayer l_player : p_gameState.getD_players()) {
			if (l_player.getD_coutriesOwned().size() == l_totalCountries) {
				d_gameState.setD_winner(l_player);
				d_gameEngine.setD_gameEngineLog("Player : " + l_player.getPlayerName()
						+ " has won the Game by conquering all countries. Exiting the Game .....", "end");
				return true;
			}
		}
		return false;
	}

	@Override
	protected void tournamentGamePlay(CommandHandler p_enteredCommand) {
//		d_gameEngine.setD_gameEngineLog("\nStarting Execution Of Tournament Mode.....", "start");
//		d_tournament.executeTournamentMode();
//		d_tournament.printTournamentModeResult();
	}
}
