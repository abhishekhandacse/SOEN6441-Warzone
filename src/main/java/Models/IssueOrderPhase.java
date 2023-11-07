package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import Controllers.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.CommandHandler;
import Views.MapView;

public class IssueOrderPhase extends Phase{

    public IssueOrderPhase(GameEngine p_gameEngine, GameState p_gameState) {
        super(p_gameEngine, p_gameState);
    }

    @Override
    protected void performingAssignCountries(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, IOException, InvalidMap {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    @Override
    protected void creatingPlayers(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, IOException, InvalidMap {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    @Override
    protected void performingEditNeighbour(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    @Override
    protected void performingEditCountry(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    @Override
    protected void performingValidateMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidMap, InvalidCommand, IOException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    @Override
    protected void performingLoadMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    @Override
    protected void performingSaveMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, InvalidMap, IOException {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    @Override
    protected void performingEditContinent(CommandHandler p_command, ModelPlayer p_player) throws IOException, InvalidCommand, InvalidMap {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    @Override
    protected void performingMapEdit(CommandHandler p_command, ModelPlayer p_player) throws IOException, InvalidCommand, InvalidMap {
        printInvalidCommandInState();
        askForOrder(p_player);
    }

    @Override
    protected void performingCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException {
        if(p_player.getD_cardsOwnedByPlayer().contains(p_enteredCommand.split(" ")[0])) {
                p_player.handleCardCommands(p_enteredCommand, d_gameState);
                d_gameEngine.setD_gameEngineLog(p_player.d_playerLog, "effect");
        }  
        p_player.checkForMoreOrders();
    }

    @Override
    protected void performingShowMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, IOException, InvalidMap {
        MapView l_mapView = new MapView(d_gameState);
        l_mapView.showMap();

        askForOrder(p_player);
    }

    @Override
    protected void performingAdvance(String p_command, ModelPlayer p_player) throws IOException {
        p_player.createAdvanceOrder(p_command, d_gameState);
        d_gameState.updateLog(p_player.getD_playerLog(), "effect");
        p_player.checkForMoreOrders();
    }

    @Override
    public void initPhase(){
        while (d_gameEngine.getD_CurrentPhase() instanceof IssueOrderPhase) {
            issueOrders();
        }
    }

    @Override
    protected void performingCreateDeploy(String p_command, ModelPlayer p_player) throws IOException {
        p_player.createDeployOrder(p_command);
        d_gameState.updateLog(p_player.getD_playerLog(), "effect");
        p_player.checkForMoreOrders();
    }

    protected void issueOrders(){
        // issue orders for each player
        do {
            for (ModelPlayer l_player : d_gameState.getD_players()) {
                if (l_player.getD_moreOrders() && !l_player.getPlayerName().equals("Neutral")) {
                    try {
                        l_player.issue_order(this);
                    } catch (InvalidCommand | IOException | InvalidMap l_exception) {
                        d_gameEngine.setD_gameEngineLog(l_exception.getMessage(), "effect");
                    }
                }
            }
        } while (d_playerService.checkForMoreOrders(d_gameState.getD_players()));

        d_gameEngine.setOrderExecutionPhase();
    }

    public void askForOrder(ModelPlayer p_player) throws InvalidCommand, IOException, InvalidMap{
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nPlease enter command to issue order for player : " + p_player.getPlayerName()
                + " or give showmap command to view current state of the game.");
        String l_commandEntered = l_reader.readLine();

        d_gameState.updateLog("(Player: "+p_player.getPlayerName()+") "+ l_commandEntered, "order");

        handleCommand(l_commandEntered, p_player);
    }

}
