package Controllers;

import Utils.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 *  This class severs as the starting point of the game.
 */
public class MainGameEngineController {
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
            System.out.println("Enter the game commands or type 'exit' for quitting");
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
        String l_rootCommand = l_commandHandler.fetchRootCommand();

        switch (l_rootCommand) {
            case "editcontinent":
                EditContinent();
                break;
            case "editcountry":
                EditCountry();
                break;
            case "editneighbor":
                EditNeighbor();
                break;
            case "showmap":
                ShowMap();
                break;
            case "savemap":
                SaveMap();
                break;
            case "editmap":
                EditMap();
                break;
            case "validatemap":
                ValidateMap();
                break;
            case "loadmap":
                LoadMap();
                break;
            case "gameplayer":
                AddOrRemovePlayer();
                break;
            case "assigncountries":
                AssignCountries();
                break;
        
            default:
                break;
        }
    }

    private void AssignCountries() {
    }

    private void AddOrRemovePlayer() {
    }

    private void LoadMap() {
    }

    private void ValidateMap() {
    }

    private void EditMap() {
    }

    private void SaveMap() {
    }

    private void ShowMap() {
    }

    private void EditNeighbor() {
    }

    private void EditCountry() {
    }

    private void EditContinent() {
    }


}
