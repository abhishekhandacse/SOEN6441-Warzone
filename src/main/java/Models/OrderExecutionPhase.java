package Models;

import Controllers.GameEngine;
import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Utils.CommandHandler;
import Utils.CommonUtil;
import Views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Order Execution Phase implementation for GamePlay using State Pattern.
 */
public class OrderExecutionPhase extends Phase {

	/**
	 * It's a constructor that init the GameEngine context in Phase class.
	 *
	 * @param p_gameEngine GameEngine Context
	 * @param p_gameState  current Game State
	 */
	public OrderExecutionPhase(GameEngine p_gameEngine, GameState p_gameState) {
		super(p_gameEngine, p_gameState);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performingCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException {
		printInvalidCommandInState();
	}

	/**
	 * {@inheritDoc}
	*/
	@Override
	protected void performingAdvance(String p_command, ModelPlayer p_player) {
		printInvalidCommandInState();
	}

	/**
	 * Main command that will initialize the current phase of the game
	 */
	@Override
	public void initPhase() {
		while (d_gameEngine.getD_CurrentPhase() instanceof OrderExecutionPhase) {
			executeOrders();

			MapView l_map_view = new MapView(d_gameState);
			l_map_view.showMap();

			if (this.checkEndOftheGame(d_gameState))
				break;

            while (!CommonUtil.isNullOrEmptyCollection(d_gameState.getD_playersList())) {
                System.out.println("Press Y/y if you want to continue for next turn or else press N/n");
                BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));

				try {
					String l_continue = l_reader.readLine();

                    if (l_continue.equalsIgnoreCase("N")) {
                        break;
                    } else if(l_continue.equalsIgnoreCase("Y")){
                        d_playerService.assignArmies(d_gameState);
                        d_gameEngine.setIssueOrderPhase();
                    } else {
                        System.out.println("Invalid Input");
                    }
                } catch (IOException l_e) {
                    System.out.println("Invalid Input");
                }
            }
        }
    }

	/**
	 * Invokes order execution logic for all unexecuted orders.
	 */
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

	/**
	 * Add neutral player to game state.
	 *
	 * @param p_gameState GameState
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performingShowMap(CommandHandler p_command, ModelPlayer p_player) {
		MapView l_mapView = new MapView(d_gameState);
		l_mapView.showMap();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performingCreateDeploy(String p_command, ModelPlayer p_player) {
		printInvalidCommandInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performingAssignCountries(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, IOException {
		printInvalidCommandInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void creatingPlayers(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException {
		printInvalidCommandInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performingEditNeighbour(CommandHandler p_command, ModelPlayer p_player)
			throws CommandValidationException, MapValidationException, IOException {
		printInvalidCommandInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performingEditCountry(CommandHandler p_command, ModelPlayer p_player)
			throws CommandValidationException, MapValidationException, IOException {
		printInvalidCommandInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performingValidateMap(CommandHandler p_command, ModelPlayer p_player) throws MapValidationException, CommandValidationException {
		printInvalidCommandInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performingLoadMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException {
		printInvalidCommandInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performingSaveMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException {
		printInvalidCommandInState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performingEditContinent(CommandHandler p_command, ModelPlayer p_player)
			throws IOException, CommandValidationException, MapValidationException {
		printInvalidCommandInState();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performingMapEdit(CommandHandler p_command, ModelPlayer p_player) throws IOException, CommandValidationException, MapValidationException {
        printInvalidCommandInState();
    }

	/**
	 * Checks if single player has conquered all countries of the map to indicate end of the game.
	 *
	 * @param p_gameState Current State of the game
	 * @return true if game has to be ended else false
	 */
	protected Boolean checkEndOftheGame(GameState p_gameState) {
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
}
