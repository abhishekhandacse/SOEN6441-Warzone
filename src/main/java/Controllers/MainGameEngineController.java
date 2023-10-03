package Controllers;

import Utils.CommandHandler;
import Views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Logger.ConsoleLogger;
import Models.State;


/**
 *  This class severs as the starting point of the game.
 */
public class MainGameEngineController {

    ConsoleLogger consoleLogger = new ConsoleLogger();
    
    /**
     * Main Method: Accepts commands from the players and map them to corresponding logical actions.
     * 
     * @param p_args 
     */
    public static void main(String[] p_args) {
        MainGameEngineController l_mainGameEngineController = new MainGameEngineController();
        l_mainGameEngineController.initializeWarzoneGamePlay();
    }

    private void initializeWarzoneGamePlay(){
        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        var l_infiniteLoop = true;

        while (l_infiniteLoop){
            System.out.println("Input the game commands or input 'exit' to exit the game");
            try {
                String l_inputCommand = l_bufferedReader.readLine();

                commandHandler(l_inputCommand);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private  void commandHandler(final String p_inputCommand){
        CommandHandler l_commandHandler = new CommandHandler(p_inputCommand);
        String l_rootCommand = l_commandHandler.getRootCommand();
        boolean l_isMapAvailable = d_state.getD_map() != null;  

        if ("editmap".equals(l_rootCommand)) {
			editMap();
		} else if ("editcontinent".equals(l_rootCommand)) {
			if (!l_isMapAvailable) {
				consoleLogger.writeLog("Can't perform Editcontinent as Map is Not Available, please run 'editmap' command first.");
			} else {
				editContinent();
			}
		} else if ("editcountry".equals(l_rootCommand)) {
			if (!l_isMapAvailable) {
				consoleLogger.writeLog("Can't perform EditCountry as Map is Not Available, please run 'editmap' command first.");
			} else {
				editCountry();
			}
		} else if ("editneighbor".equals(l_rootCommand)) {
			if (!l_isMapAvailable) {
				consoleLogger.writeLog("Can't perform EditNeighbor as Map is Not Available, please run 'editmap' command first.");
			} else {
				editNeighbour();
			}
		} 
    }

    private void assignCountries() {
    }

    private void addOrRemovePlayer() {
    }

    private void loadMap() {
    }

    private void validateMap() {
    }

    private void editMap() {
    }

    private void saveMap() {
    }

    private void showMap() {
    }

    private void editNeighbor() {
    }

    private void editCountry() {
    }

    private void editContinent() {
    }


}
