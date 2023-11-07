package Controllers;

import Models.GameState;
import Models.IssueOrderPhase;
import Models.OrderExecutionPhase;
import Models.Phase;
import Models.InitialStartUpPhase;


public class GameEngine {

    GameState d_stateOfGame = new GameState();


    Phase d_presentPhaseGame = new InitialStartUpPhase(this, d_stateOfGame);


    private void setD_CurrentPhase(Phase p_phase){
        d_presentPhaseGame = p_phase;
    }

    public Phase getD_CurrentPhase(){
        return d_presentPhaseGame;
    }

    public void setOrderExecutionPhase(){
        this.setD_gameEngineLog("Execution of Order Phase", "phase");
        setD_CurrentPhase(new OrderExecutionPhase(this, d_stateOfGame));
        getD_CurrentPhase().initPhase();
    }

    public void setD_gameEngineLog(String p_logForGameEngine, String p_typeLog) {
        d_presentPhaseGame.getD_gameState().updateLog(p_logForGameEngine, p_typeLog);
        String l_consoleLogger = p_typeLog.equalsIgnoreCase("phase")
                ? "\n====================" + p_logForGameEngine + " ====================\n"
                : p_logForGameEngine;
        System.out.println(l_consoleLogger);
    }

    public void setIssueOrderPhase(){
        this.setD_gameEngineLog("Issue of Order Phase", "phase");
        setD_CurrentPhase(new IssueOrderPhase(this, d_stateOfGame));
        getD_CurrentPhase().initPhase();
    }

    public static void main(String[] p_args) {
        GameEngine l_game = new GameEngine();

        l_game.getD_CurrentPhase().getD_gameState().updateLog("Game is being initialized ......"+System.lineSeparator(), "start");
        l_game.setD_gameEngineLog("Startup of Game Phase", "phase");
        l_game.getD_CurrentPhase().initPhase();
    }
}
