package Models;

import java.io.IOException;

import Controllers.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Services.MapService;
import Services.PlayerService;
import Utils.CommandHandler;

public abstract class Phase {

    GameState d_gameState;
    GameEngine d_gameEngine;
    MapService d_mapService = new MapService();
    PlayerService d_playerService = new PlayerService();

    boolean l_isMapLoaded;

    public Phase(GameEngine p_gameEngine, GameState p_gameState){
        d_gameEngine = p_gameEngine;
        d_gameState = p_gameState;
    }

    public void setD_gameState(GameState p_gameState) {
        d_gameState = p_gameState;
    }

    public GameState getD_gameState() {
        return d_gameState;
    }

    public void handleCommand(String p_enteredCommand) throws InvalidMap, InvalidCommand, IOException {
        commandHandler(p_enteredCommand, null);
    }

    public void handleCommand(String p_enteredCommand, ModelPlayer p_player) throws InvalidMap, InvalidCommand, IOException {
        commandHandler(p_enteredCommand, p_player);
    }

    private void commandHandler(String p_enteredCommand, ModelPlayer p_player) throws InvalidMap, InvalidCommand, IOException {
        CommandHandler l_command = new CommandHandler(p_enteredCommand);
        String l_rootCommand = l_command.getRootCommand();
        l_isMapLoaded = d_gameState.getD_map() != null;

        d_gameState.updateLog(l_command.getD_command(), "command");

        switch (l_rootCommand) {
            case "editmap": {
                performingMapEdit(l_command, p_player);
                break;
            }
            case "editcontinent": {
                performingEditContinent(l_command, p_player);
                break;
            }
            case "savemap": {
                performingSaveMap(l_command, p_player);
                break;
            }
            case "loadmap": {
                performingLoadMap(l_command, p_player);
                break;
            }
            case "validatemap": {
                performingValidateMap(l_command, p_player);
                break;
            }
            case "editcountry": {
                performingEditCountry(l_command, p_player);
                break;
            }
            case "editneighbor": {
                performingEditNeighbour(l_command, p_player);
                break;
            }
            case "gameplayer": {
                creatingPlayers(l_command, p_player);
                break;
            }
            case "assigncountries": {
                performingAssignCountries(l_command, p_player);
                break;
            }
            case "showmap": {
                performingShowMap(l_command, p_player);
                break;
            }
            case "deploy": {
                performingCreateDeploy(p_enteredCommand, p_player);
                break;
            }
            case "advance": {
                performingAdvance(p_enteredCommand, p_player);
                break;
            }
            case "airlift":
            case "blockade":
            case "negotiate":
            case "bomb":
            {
                performingCardHandle(p_enteredCommand, p_player);
                break;
            }

            case "exit": {
                d_gameEngine.setD_gameEngineLog("Exit Command Entered, Game Ends!", "effect");
                System.exit(0);
                break;
            }
            default: {
                d_gameEngine.setD_gameEngineLog("Invalid Command", "effect");
                break;
            }
        }
    }

    protected abstract void performingCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException;

    protected abstract void performingShowMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, IOException, InvalidMap;

    protected abstract void performingAdvance(String p_command, ModelPlayer p_player) throws IOException;

    protected abstract void performingCreateDeploy(String p_command, ModelPlayer p_player) throws IOException;

    protected abstract void performingAssignCountries(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, IOException, InvalidMap;

    protected abstract void performingEditNeighbour(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, InvalidMap, IOException;

    protected abstract void performingEditCountry(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, InvalidMap, IOException;

    protected abstract void performingValidateMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidMap, InvalidCommand, IOException;

    protected abstract void performingLoadMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, InvalidMap, IOException;

    protected abstract void performingSaveMap(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, InvalidMap, IOException;

    protected abstract void performingEditContinent(CommandHandler p_command, ModelPlayer p_player) throws IOException, InvalidCommand, InvalidMap;

    protected abstract void performingMapEdit(CommandHandler p_command, ModelPlayer p_player) throws IOException, InvalidCommand, InvalidMap;

    protected abstract void creatingPlayers(CommandHandler p_command, ModelPlayer p_player) throws InvalidCommand, IOException, InvalidMap;

    public void printInvalidCommandInState(){
        d_gameEngine.setD_gameEngineLog("Invalid Command in Current State", "effect");
    }

    public abstract void initPhase();
}
