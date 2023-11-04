package Models;

import java.io.IOException;

import Controllers.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.MapValidationException;
import Services.MapService;
import Services.PlayerService;
import Utils.Command;

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

    public void handleCommand(String p_enteredCommand) throws MapValidationException, InvalidCommand, IOException {
        commandHandler(p_enteredCommand, null);
    }

    public void handleCommand(String p_enteredCommand, ModelPlayer p_player) throws MapValidationException, InvalidCommand, IOException {
        commandHandler(p_enteredCommand, p_player);
    }

    private void commandHandler(String p_enteredCommand, ModelPlayer p_player) throws MapValidationException, InvalidCommand, IOException {
        Command l_command = new Command(p_enteredCommand);
        String l_rootCommand = l_command.getRootCommand();
        l_isMapLoaded = d_gameState.getD_map() != null;

        d_gameState.updateLog(l_command.getD_command(), "command");

        switch (l_rootCommand) {
            case "editmap": {
                performMapEdit(l_command, p_player);
                break;
            }
            case "editcontinent": {
                performEditContinent(l_command, p_player);
                break;
            }
            case "savemap": {
                performSaveMap(l_command, p_player);
                break;
            }
            case "loadmap": {
                performLoadMap(l_command, p_player);
                break;
            }
            case "validatemap": {
                performValidateMap(l_command, p_player);
                break;
            }
            case "editcountry": {
                performEditCountry(l_command, p_player);
                break;
            }
            case "editneighbor": {
                performEditNeighbour(l_command, p_player);
                break;
            }
            case "gameplayer": {
                createPlayers(l_command, p_player);
                break;
            }
            case "assigncountries": {
                performAssignCountries(l_command, p_player);
                break;
            }
            case "showmap": {
                performShowMap(l_command, p_player);
                break;
            }
            case "deploy": {
                performCreateDeploy(p_enteredCommand, p_player);
                break;
            }
            case "advance": {
                performAdvance(p_enteredCommand, p_player);
                break;
            }
            case "airlift":
            case "blockade":
            case "negotiate":
            case "bomb":
            {
                performCardHandle(p_enteredCommand, p_player);
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

    protected abstract void performCardHandle(String p_enteredCommand, ModelPlayer p_player) throws IOException;

    protected abstract void performShowMap(Command p_command, ModelPlayer p_player) throws InvalidCommand, IOException, MapValidationException;

    protected abstract void performAdvance(String p_command, ModelPlayer p_player) throws IOException;

    protected abstract void performCreateDeploy(String p_command, ModelPlayer p_player) throws IOException;

    protected abstract void performAssignCountries(Command p_command, ModelPlayer p_player) throws InvalidCommand, IOException, MapValidationException;

    protected abstract void performEditNeighbour(Command p_command, ModelPlayer p_player) throws InvalidCommand, MapValidationException, IOException;

    protected abstract void performEditCountry(Command p_command, ModelPlayer p_player) throws InvalidCommand, MapValidationException, IOException;

    protected abstract void performValidateMap(Command p_command, ModelPlayer p_player) throws MapValidationException, InvalidCommand, IOException;

    protected abstract void performLoadMap(Command p_command, ModelPlayer p_player) throws InvalidCommand, MapValidationException, IOException;

    protected abstract void performSaveMap(Command p_command, ModelPlayer p_player) throws InvalidCommand, MapValidationException, IOException;

    protected abstract void performEditContinent(Command p_command, ModelPlayer p_player) throws IOException, InvalidCommand, MapValidationException;

    protected abstract void performMapEdit(Command p_command, ModelPlayer p_player) throws IOException, InvalidCommand, MapValidationException;

    protected abstract void createPlayers(Command p_command, ModelPlayer p_player) throws InvalidCommand, IOException, MapValidationException;

    public void printInvalidCommandInState(){
        d_gameEngine.setD_gameEngineLog("Invalid Command in Current State", "effect");
    }

    public abstract void initPhase();
}
