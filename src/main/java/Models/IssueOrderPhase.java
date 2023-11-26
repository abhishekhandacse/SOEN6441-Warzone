package Models;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import Constants.ApplicationConstants;
import Controllers.GameEngine;
import Exceptions.CommandValidationException;
import Exceptions.MapValidationException;
import Services.GameService;
import Utils.CommandHandler;
import Utils.LogHandlerException;
import Views.MapView;

/**
 * The IssueOrderPhase class represents the phase of the game where players issue orders.
 * It extends the abstract class Phase.
 */

public class IssueOrderPhase extends Phase {
    /**
     * Constructor for the IssueOrderPhase class.
     *
     * @param p_gameEngine The game engine managing the game phases.
     * @param p_gameState  The current state of the game.
     */
	public IssueOrderPhase(GameEngine p_gameEngine, GameState p_gameState) {
		super(p_gameEngine, p_gameState);
	}
    /**
     * Initializes the issue order phase of the game.
     *
     * @param p_isTournamentMode Indicates whether the game is in tournament mode.
     */
	@Override
	public void initPhase(boolean p_isTournamentMode) {
		while (d_gameEngine.getD_CurrentPhase() instanceof IssueOrderPhase) {
			issueOrders(p_isTournamentMode);
		}
	}
    /**
     * Performs the action of loading a game.
     *
     * @param p_command The command for loading the game.
     * @param p_player  The player initiating the action.
     * @throws CommandValidationException If the command is invalid.
     * @throws MapValidationException     If the map is invalid.
     * @throws IOException    If an I/O error occurs.
     */
	@Override
    protected void performLoadGame(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException {
        printCommandValidationExceptionInState();
        askForOrder(p_player);
    }
    /**
     * Performs the action of saving the game.
     *
     * @param p_command The command for saving the game.
     * @param p_player  The player initiating the action.
     * @throws CommandValidationException If the command is invalid.
     * @throws MapValidationException     If the map is invalid.
     * @throws IOException    If an I/O error occurs.
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
     * Performs the action of handling card commands.
     *
     * @param p_enteredCommand The entered command for handling cards.
     * @param p_player         The player initiating the action.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void performCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException {
		if(p_player.getD_cardsOwnedByPlayer().contains(p_enteredCommand.split(" ")[0])) {
			p_player.handleCardCommands(p_enteredCommand, d_gameState);
		}
    }
    /**
     * Performs the action of showing the game map.
     *
     * @param p_command The command for showing the game map.
     * @param p_player  The player initiating the action.
     * @throws CommandValidationException If the command is invalid.
     * @throws IOException    If an I/O error occurs.
     * @throws MapValidationException     If the map is invalid.
     */
	@Override
	protected void performShowMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, IOException, MapValidationException {
		MapView l_mapView = new MapView(d_gameState);
		l_mapView.showMap();

		askForOrder(p_player);
	}

    /**
     * Performs the action of advancing armies.
     *
     * @param p_command The command for advancing armies.
     * @param p_player  The player initiating the action.
     * @throws IOException If an I/O error occurs.
     */
	@Override
	protected void performAdvance(String p_command, ModelPlayer p_player) throws IOException {
		p_player.createAdvanceOrder(p_command, d_gameState);
		d_gameState.updateLog(p_player.getD_playerLog(), "effect");
	}
        /**
     * Performs the action of creating deploy orders.
     *
     * @param p_command The command for creating deploy orders.
     * @param p_player  The player initiating the action.
     * @throws IOException If an I/O error occurs.
     */
	@Override
	protected void performCreateDeploy(String p_command, ModelPlayer p_player) throws IOException {
		p_player.createDeployOrder(p_command);
		d_gameState.updateLog(p_player.getD_playerLog(), "effect");
	}
        /**
     * Performs the action of assigning countries to players.
     *
     * @param p_command                     The command for assigning countries.
     * @param p_player                      The player initiating the action.
     * @param isTournamentMode              Indicates whether the game is in tournament mode.
     * @param p_gameState                   The current state of the game.
     * @throws CommandValidationException   If the command is invalid.
     * @throws IOException                  If an I/O error occurs.
     * @throws MapValidationException       If the map is invalid.
     */
	@Override
	protected void performAssignCountries(CommandHandler p_command, ModelPlayer p_player, boolean isTournamentMode, GameState p_gameState)
			throws CommandValidationException, IOException, MapValidationException {
		printCommandValidationExceptionInState();
		askForOrder(p_player);
	}
    /**
     * Performs the action of editing neighbors of a country.
     *
     * @param p_command The command for editing neighbors.
     * @param p_player  The player initiating the action.
     * @throws CommandValidationException If the command is invalid.
     * @throws MapValidationException     If the map is invalid.
     * @throws IOException    If an I/O error occurs.
     */
	@Override
	protected void performEditNeighbours(CommandHandler p_command, ModelPlayer p_player)
			throws CommandValidationException, MapValidationException, IOException {
		printCommandValidationExceptionInState();
		askForOrder(p_player);
	}
    /**
     * Performs the action of editing a country.
     *
     * @param p_command The command for editing a country.
     * @param p_player  The player initiating the action.
     * @throws CommandValidationException If the command is invalid.
     * @throws MapValidationException     If the map is invalid.
     * @throws IOException    If an I/O error occurs.
     */
	@Override
	protected void performEditCountry(CommandHandler p_command, ModelPlayer p_player)
			throws CommandValidationException, MapValidationException, IOException {
		printCommandValidationExceptionInState();
		askForOrder(p_player);
	}
    /**
     * Performs the action of validating the map.
     *
     * @param p_command The command for validating the map.
     * @param p_player  The player initiating the action.
     * @throws MapValidationException     If the map is invalid.
     * @throws CommandValidationException If the command is invalid.
     * @throws IOException    If an I/O error occurs.
     */
	@Override
	protected void performValidateMap(CommandHandler p_command, ModelPlayer p_player)
			throws MapValidationException, CommandValidationException, IOException {
		printCommandValidationExceptionInState();
		askForOrder(p_player);
	}
    /**
     * Performs the action of loading the map.
     *
     * @param p_command The command for loading the map.
     * @param p_player  The player initiating the action.
     * @throws CommandValidationException If the command is invalid.
     * @throws MapValidationException     If the map is invalid.
     * @throws IOException    If an I/O error occurs.
     */
	@Override
	protected void performLoadMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException {
		printCommandValidationExceptionInState();
		askForOrder(p_player);
	}
        /**
     * Performs the action of saving the map.
     *
     * @param p_command The command for saving the map.
     * @param p_player  The player initiating the action.
     * @throws CommandValidationException If the command is invalid.
     * @throws MapValidationException     If the map is invalid.
     * @throws IOException    If an I/O error occurs.
     */
	@Override
	protected void performSaveMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException {
		printCommandValidationExceptionInState();
		askForOrder(p_player);
	}
    /**
     * Performs the action of editing a continent.
     *
     * @param p_command The command for editing a continent.
     * @param p_player  The player initiating the action.
     * @throws IOException       If an I/O error occurs.
     * @throws CommandValidationException    If the command is invalid.
     * @throws MapValidationException        If the map is invalid.
     */
	@Override
	protected void performEditContinent(CommandHandler p_command, ModelPlayer p_player)
			throws IOException, CommandValidationException, MapValidationException {
		printCommandValidationExceptionInState();
		askForOrder(p_player);
	}
    /**
     * Performs the action of editing the map.
     *
     * @param p_command The command for editing the map.
     * @param p_player  The player initiating the action.
     * @throws IOException       If an I/O error occurs.
     * @throws CommandValidationException    If the command is invalid.
     * @throws MapValidationException        If the map is invalid.
     */
	@Override
	protected void performMapEdit(CommandHandler p_command, ModelPlayer p_player) throws IOException, CommandValidationException, MapValidationException {
		printCommandValidationExceptionInState();
		askForOrder(p_player);
	}
    /**
     * Issues orders for each player in the game.
     *
     * @param p_isTournamentMode Indicates whether the game is in tournament mode.
     */
	protected void issueOrders(boolean p_isTournamentMode){
        // issue orders for each player
        do {
            for (ModelPlayer l_player : d_gameState.getD_players()) {
				//System.out.println("l_player :" + l_player.getPlayerName());
				if(l_player.getD_coutriesOwned().size()==0){
					l_player.setD_moreOrders(false);
				}
                if (l_player.getD_moreOrders() && !l_player.getPlayerName().equals("Neutral")) {
                    try {
                        l_player.issue_order(this);
                        l_player.checkForMoreOrders(p_isTournamentMode);
                    } catch (CommandValidationException | IOException | MapValidationException l_exception) {
                        d_gameEngine.setD_gameEngineLog(l_exception.getMessage(), "effect");
                    }
                }
            }
        } while (d_playerService.checkForMoreOrders(d_gameState.getD_players()));
        d_gameEngine.setOrderExecutionPhase();
    }
    /**
     * Asks the player for their orders and handles the entered command.
     *
     * @param p_player The player for whom the order is being issued.
     * @throws CommandValidationException If the entered command is invalid.
     * @throws IOException    If an I/O error occurs.
     * @throws MapValidationException     If the map is invalid.
     */
    public void askForOrder(ModelPlayer p_player) throws CommandValidationException, IOException, MapValidationException{
        String l_commandEntered = p_player.getPlayerOrder(d_gameState);
        if(l_commandEntered == null) return;

        d_gameState.updateLog("(Player: "+p_player.getPlayerName()+") "+ l_commandEntered, "order");
        handleCommand(l_commandEntered, p_player);
    }
    /**
     * Overrides the tournament gameplay for the issue order phase.
     *
     * @param p_enteredCommand The entered command for tournament gameplay.
     */
	@Override
	protected void tournamentGamePlay(CommandHandler p_enteredCommand) {
		// printCommandValidationExceptionInState();
	}
    /**
     * Performs the action of creating players.
     *
     * @param p_command The command for creating players.
     * @param p_player  The player initiating the action.
     * @throws CommandValidationException If the command is invalid.
     * @throws IOException    If an I/O error occurs.
     * @throws MapValidationException     If the map is invalid.
     */
	@Override
	protected void createPlayers(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, IOException, MapValidationException {
		printCommandValidationExceptionInState();
		askForOrder(p_player);
	}

}
