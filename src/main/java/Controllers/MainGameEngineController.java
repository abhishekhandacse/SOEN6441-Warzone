package Controllers;

import Utils.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class MainGameEngineController {
    public static void main(String[] args) {
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
    }


}
