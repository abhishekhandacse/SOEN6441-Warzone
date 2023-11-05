package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Controllers.GameEngine;
import Utils.Command;
import Utils.CommonUtil;
import Views.MapView;

public class OrderExecutionPhase extends Phase{
    // Constructor
	public OrderExecutionPhase(GameEngine p_gameEngine, GameState p_gameState) {
		super(p_gameEngine, p_gameState);
	}

    @Override
	public void initPhase() {
		while (d_gameEngine.getD_CurrentPhase() instanceof OrderExecutionPhase) {
			executeOrders();

			MapView l_mapView = new MapView(d_gameState);
			l_mapView.showMap();

			if (this.checkEndGame(d_gameState))
				break;

			while (!CommonUtil.isNullOrEmptyCollection(d_gameState.getD_playersList())) {
				System.out.println("Press Y/y if you want to continue to next turn or else press N/n to exit");
				BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));

				try {
					String l_continue = l_reader.readLine();

					if (l_continue.equalsIgnoreCase("N")) {
						break;
					} else if (l_continue.equalsIgnoreCase("Y")) {
						d_playerService.assignArmies(d_gameState);
						d_gameEngine.setIssueOrderPhase();
					} else {
						System.out.println("Invalid Input");
					}
				} catch (IOException e) {
					System.out.println("Invalid Input");
				}
			}
		}
	}

    @Override
	protected void createPlayers(Command p_command, ModelPlayer p_player) throws InvalidCommand {
		printInvalidCommandInState();
	}

	@Override
	protected void performShowMap(Command p_command, ModelPlayer p_player) {
		MapView l_mapView = new MapView(d_gameState);
		l_mapView.showMap();
	}

	@Override
	protected void performEditNeighbour(Command p_command, ModelPlayer p_player)
			throws InvalidCommand, InvalidMap, IOException {
		printInvalidCommandInState();
	}

	@Override
	protected void performEditCountry(Command p_command, ModelPlayer p_player)
			throws InvalidCommand, InvalidMap, IOException {
		printInvalidCommandInState();
	}

	@Override
	protected void performEditContinent(Command p_command, ModelPlayer p_player)
			throws IOException, InvalidCommand, InvalidMap {
		printInvalidCommandInState();
	}

	@Override
	protected void performMapEdit(Command p_command, ModelPlayer p_player)
			throws IOException, InvalidCommand, InvalidMap {
		printInvalidCommandInState();
	}

	@Override
	protected void performAssignCountries(Command p_command, ModelPlayer p_player) throws InvalidCommand, IOException {
		printInvalidCommandInState();
	}

	@Override
	protected void performCreateDeploy(String p_command, ModelPlayer p_player) {
		printInvalidCommandInState();
	}

	@Override
	protected void performValidateMap(Command p_command, ModelPlayer p_player) throws InvalidMap, InvalidCommand {
		printInvalidCommandInState();
	}

	@Override
	protected void performLoadMap(Command p_command, ModelPlayer p_player) throws InvalidCommand, InvalidMap {
		printInvalidCommandInState();
	}

	@Override
	protected void performSaveMap(Command p_command, ModelPlayer p_player) throws InvalidCommand, InvalidMap {
		printInvalidCommandInState();
	}

	@Override
	protected void performCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException {
		printInvalidCommandInState();
	}

	@Override
	protected void performAdvance(String p_command, ModelPlayer p_player) {
		printInvalidCommandInState();
	}

    protected Boolean checkEndGame(GameState p_gameState) {
		Integer l_totalCountries = p_gameState.getD_map().getD_allCountries().size();
		for (ModelPlayer l_player : p_gameState.getD_playersList()) {
			if (l_player.getD_coutriesOwned().size() == l_totalCountries) {
				d_gameEngine.setD_gameEngineLog("Player : " + l_player.getPlayerName()
						+ " has won the Game by conquering all countries. Exiting the Game .....", "end");
				return true;
			}
		}
		return false;
	}

    protected void executeOrders() {
		addNeutralPlayer(d_gameState);
		// Executing orders
		d_gameEngine.setD_gameEngineLog("\nStarting Execution Of Orders.....", "start");
		while (d_playerService.unexecutedOrdersExists(d_gameState.getD_playersList())) {
			for (ModelPlayer l_player : d_gameState.getD_playersList()) {
				Order l_order = l_player.next_order();
				if (l_order != null) {
					l_order.printOrder();
					d_gameState.updateLog(l_order.orderExecutionLog(), "effect");
					l_order.execute(d_gameState);
				}
			}
		}
		d_playerService.resetPlayersFlag(d_gameState.getD_playersList());
	}

	public void addNeutralPlayer(GameState p_gameState) {
		ModelPlayer l_player = p_gameState.getD_playersList().stream()
				.filter(l_pl -> l_pl.getPlayerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);
		if (CommonUtil.isNullObject(l_player)) {
			ModelPlayer l_neutralPlayer = new ModelPlayer("Neutral");
			l_neutralPlayer.setD_moreOrders(false);
			p_gameState.getD_playersList().add(l_neutralPlayer);
		} else {
			return;
		}
	}
}
