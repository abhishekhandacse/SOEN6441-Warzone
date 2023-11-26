package Controllers;

import Models.*;

/**
 * The `GameEngine` class represents the core engine of the game. It manages different game phases and provides
 * methods to switch between them.
 */
public class GameEngine {

    /**
     * The current state of the game.
     */
    private GameState d_stateOfGame = new GameState();

    public GameState getD_gameState() {
        return d_stateOfGame;
    }

    public void setD_gameState(GameState p_gameState) {
        this.d_stateOfGame = p_gameState;
    }

    /**
     * The present phase of the game.
     */
    private Phase d_presentPhaseGame = new InitStartUpPhase(this, d_stateOfGame);

    /**
     * Sets the current phase of the game.
     *
     * @param p_phase The new phase to set.
     */
    public void setD_CurrentPhase(Phase p_phase) {
        d_presentPhaseGame = p_phase;
    }

    /**
     * Gets the current phase of the game.
     *
     * @return The current game phase.
     */
    public Phase getD_CurrentPhase() {
        return d_presentPhaseGame;
    }

    static boolean d_isTournamentMode = false;

    public boolean isD_isTournamentMode() {
        return d_isTournamentMode;
    }

    public void setD_isTournamentMode(boolean p_isTournamentMode) {
        GameEngine.d_isTournamentMode = p_isTournamentMode;
    }

    public void loadPhase(Phase p_phase){
        d_presentPhaseGame = p_phase;
        d_stateOfGame = p_phase.getD_gameState();
        getD_CurrentPhase().initPhase(d_isTournamentMode);
    }

    public void setStartUpPhase(){
        this.setD_gameEngineLog("Start Up Phase", "phase");
        setD_CurrentPhase(new InitStartUpPhase(this, d_stateOfGame));
        getD_CurrentPhase().initPhase(d_isTournamentMode);
    }


    /**
     * Sets the game to the Order Execution phase.
     */
    public void setOrderExecutionPhase() {
        this.setD_gameEngineLog("Execution of Order Phase", "phase");
        setD_CurrentPhase(new OrderExecutionPhase(this, d_stateOfGame));
        getD_CurrentPhase().initPhase(d_isTournamentMode);
    }

    /**
     * Updates the game engine's log and the game state log.
     *
     * @param p_logForGameEngine The log message for the game engine.
     * @param p_typeLog          The type of log (e.g., "phase" or other).
     */
    public void setD_gameEngineLog(String p_logForGameEngine, String p_typeLog) {
        d_presentPhaseGame.getD_gameState().updateLog(p_logForGameEngine, p_typeLog);
        String l_consoleLogger = p_typeLog.equalsIgnoreCase("phase")
                ? "\n====================" + p_logForGameEngine + " ====================\n"
                : p_logForGameEngine;
        System.out.println(l_consoleLogger);
    }

    /**
     * Sets the game to the Issue Order phase.
     */
    public void setIssueOrderPhase(boolean p_isTournamentMode) {
        this.setD_gameEngineLog("Issue of Order Phase", "phase");
        setD_CurrentPhase(new IssueOrderPhase(this, d_stateOfGame));
        getD_CurrentPhase().initPhase(p_isTournamentMode);
    }

    /**
     * The main method for the game engine.
     *
     * @param p_args Command-line arguments (not used in this example).
     */
    public static void main(String[] p_args) {
        GameEngine l_game = new GameEngine();

        l_game.getD_CurrentPhase().getD_gameState().updateLog("Game is being initialized ....." + System.lineSeparator(), "start");
        l_game.setD_gameEngineLog("Startup of Game Phase", "phase");
        l_game.getD_CurrentPhase().initPhase(d_isTournamentMode);
    }
}
