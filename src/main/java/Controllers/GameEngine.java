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

    /**
     * The present phase of the game.
     */
    private Phase d_presentPhaseGame = new InitialStartUpPhase(this, d_stateOfGame);

    /**
     * Sets the current phase of the game.
     *
     * @param p_phase The new phase to set.
     */
    private void setD_CurrentPhase(Phase p_phase) {
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

    /**
     * Sets the game to the Order Execution phase.
     */
    public void setOrderExecutionPhase() {
        this.setD_gameEngineLog("Execution of Order Phase", "phase");
        setD_CurrentPhase(new OrderExecutionPhase(this, d_stateOfGame));
        getD_CurrentPhase().initPhase();
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
    public void setIssueOrderPhase() {
        this.setD_gameEngineLog("Issue of Order Phase", "phase");
        setD_CurrentPhase(new IssueOrderPhase(this, d_stateOfGame));
        getD_CurrentPhase().initPhase();
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
        l_game.getD_CurrentPhase().initPhase();
    }
}
