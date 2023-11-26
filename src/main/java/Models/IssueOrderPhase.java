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

public class IssueOrderPhase extends Phase {

	public IssueOrderPhase(GameEngine p_gameEngine, GameState p_gameState) {
		super(p_gameEngine, p_gameState);
	}

	@Override
	public void initPhase(boolean p_isTournamentMode) {
		while (d_gameEngine.getD_CurrentPhase() instanceof IssueOrderPhase) {
			issueOrders(p_isTournamentMode);
		}
	}

	@Override
    protected void performLoadGame(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

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

    @Override
    protected void performCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException {
		if(p_player.getD_cardsOwnedByPlayer().contains(p_enteredCommand.split(" ")[0])) {
			p_player.handleCardCommands(p_enteredCommand, d_gameState);
		}
    }

	@Override
	protected void performShowMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, IOException, MapValidationException {
		MapView l_mapView = new MapView(d_gameState);
		l_mapView.showMap();

		askForOrder(p_player);
	}

	@Override
	protected void performAdvance(String p_command, ModelPlayer p_player) throws IOException {
		p_player.createAdvanceOrder(p_command, d_gameState);
		d_gameState.updateLog(p_player.getD_playerLog(), "effect");
	}

	@Override
	protected void performCreateDeploy(String p_command, ModelPlayer p_player) throws IOException {
		p_player.createDeployOrder(p_command);
		d_gameState.updateLog(p_player.getD_playerLog(), "effect");
	}

	@Override
	protected void performAssignCountries(CommandHandler p_command, ModelPlayer p_player, boolean isTournamentMode, GameState p_gameState)
			throws CommandValidationException, IOException, MapValidationException {
		printInvalidCommandInState();
		askForOrder(p_player);
	}

	@Override
	protected void performEditNeighbours(CommandHandler p_command, ModelPlayer p_player)
			throws CommandValidationException, MapValidationException, IOException {
		printInvalidCommandInState();
		askForOrder(p_player);
	}

	@Override
	protected void performEditCountry(CommandHandler p_command, ModelPlayer p_player)
			throws CommandValidationException, MapValidationException, IOException {
		printInvalidCommandInState();
		askForOrder(p_player);
	}

	@Override
	protected void performValidateMap(CommandHandler p_command, ModelPlayer p_player)
			throws MapValidationException, CommandValidationException, IOException {
		printInvalidCommandInState();
		askForOrder(p_player);
	}

	@Override
	protected void performLoadMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException {
		printInvalidCommandInState();
		askForOrder(p_player);
	}

	@Override
	protected void performSaveMap(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, MapValidationException, IOException {
		printInvalidCommandInState();
		askForOrder(p_player);
	}

	@Override
	protected void performEditContinent(CommandHandler p_command, ModelPlayer p_player)
			throws IOException, CommandValidationException, MapValidationException {
		printInvalidCommandInState();
		askForOrder(p_player);
	}

	@Override
	protected void performMapEdit(CommandHandler p_command, ModelPlayer p_player) throws IOException, CommandValidationException, MapValidationException {
		printInvalidCommandInState();
		askForOrder(p_player);
	}

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

    public void askForOrder(ModelPlayer p_player) throws CommandValidationException, IOException, MapValidationException{
        String l_commandEntered = p_player.getPlayerOrder(d_gameState);
        if(l_commandEntered == null) return;

        d_gameState.updateLog("(Player: "+p_player.getPlayerName()+") "+ l_commandEntered, "order");
        handleCommand(l_commandEntered, p_player);
    }

	@Override
	protected void tournamentGamePlay(CommandHandler p_enteredCommand) {
		// printInvalidCommandInState();
	}

	@Override
	protected void createPlayers(CommandHandler p_command, ModelPlayer p_player) throws CommandValidationException, IOException, MapValidationException {
		printInvalidCommandInState();
		askForOrder(p_player);
	}

}
